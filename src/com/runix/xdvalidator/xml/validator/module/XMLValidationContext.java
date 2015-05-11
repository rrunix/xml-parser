package com.runix.xdvalidator.xml.validator.module;

import java.util.LinkedList;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.groups.Attribute;
import com.runix.xdvalidator.xml.groups.Element;
import com.runix.xdvalidator.xml.groups.PCDataElement;

public class XMLValidationContext {

	private LinkedList<Element> elements;

	private Attribute attribute;

	private Element element;

	private Element root;

	private boolean rootSet;

	public XMLValidationContext() {
		this.elements = new LinkedList<>();
		this.rootSet = false;
	}

	public void openElement(String name) {
		System.out.println("open element -> " + name);
		element = new Element(name);
		if (rootSet) {
			if (elements.size() == 0) {
				throw new RuntimeException(
						"Trying to add an element when the root element is closed");
			}
			elements.peek().addChild(element);
		} else {
			root = element;
			rootSet = true;
		}
	}

	public void closeElement(String name) {
		Element open = elements.peek();
		if (open != null) {
			if (open.getElementName().equals(name)) {
				elements.poll();
			} else {
				throw new RuntimeException(
						"Error when closing element; element expected to close was "
								+ open.getElementName() + " trying to close "
								+ name);
			}
		}
	}

	public void closeElementOnDef() {
		this.element.markAsClosedOnDef();
		this.element = null;
	}

	public void closeElementDef() {
		this.elements.addFirst(this.element);
		this.element = null;
	}

	public void addAttributeName(String name) {
		this.attribute = new Attribute(name);
	}

	public void addAttributeValue(String value) throws ValidationException {
		this.validateEntitiesIfAny(value);
		this.attribute.setValue(value);
		this.element.addAttribute(this.attribute);
		this.attribute = null;
	}

	public void addPCData(String value) throws ValidationException {
		this.validateEntitiesIfAny(value);
		PCDataElement element = new PCDataElement("#PCDATA", value);
		element.markAsClosedOnDef();
		this.elements.peek().addChild(element);
	}

	public void validate() throws ValidationException {
		StringBuilder errors = new StringBuilder();
		if (this.elements.size() > 0) {
			errors.append("The elements => [ ");
			while (elements.size() > 0) {
				errors.append(elements.poll().getElementName() + " ");
			}
			errors.append(" ] ");
			errors.append("are still open");
		}

		if (errors.length() > 0) {
			throw new ValidationException(errors.toString());
		}
	}

	public String nonClosedNodes() {
		StringBuilder errors = new StringBuilder();
		errors.append(" [ ");
		while (elements.size() > 0) {
			errors.append(elements.poll().getElementName() + " ");
		}
		errors.append(" ] ");
		return errors.toString();
	}

	public Element getRoot() {
		return this.root;
	}

	public void validateEntitiesIfAny(String toTest) throws ValidationException {
		int index = 0;
		while ((index = validateEntitiesIfAny(toTest, index)) >= 0)
			;
	}

	private int validateEntitiesIfAny(String toTest, int index)
			throws ValidationException {
		int indexAmpersand = toTest.indexOf("&", index);
		if (indexAmpersand != -1) {
			int indexSemicolon = toTest.indexOf(";", index);
			if (indexSemicolon == -1) {
				throw new ValidationException(
						"The & is only valid follow by a Entity, Missing ; . Entity structure is &name;");
			}
			if (indexSemicolon == (indexAmpersand + 1)) {
				throw new ValidationException("Missing entity name");
			}
			return indexSemicolon + 1;
		}
		return indexAmpersand;
	}
}
