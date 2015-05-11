package com.runix.xdvalidator.xml.groups;

import java.util.ArrayList;
import java.util.List;

public class Element {

	private String elementName;

	private List<Element> childs;

	private List<Attribute> attributes;

	private boolean closedOnDef;

	public Element(String elementName) {
		this.elementName = elementName;
		this.childs = new ArrayList<>();
		this.attributes = new ArrayList<>();
	}

	public void addChild(Element element) {
		this.childs.add(element);
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public String getElementName() {
		return this.elementName;
	}

	public Element getElement(String name) {
		for (Element element : this.childs) {
			if (element.getElementName().equals(name)) {
				return element;
			}
		}
		return null;
	}

	public Attribute getAttribute(String name) {
		for (Attribute attribute : this.attributes) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}

	public List<Element> getChilds() {
		return childs;
	}

	public void markAsClosedOnDef() {
		this.closedOnDef = true;
	}

	public boolean wasClosedOnDef() {
		return this.closedOnDef;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<" + elementName);
		if(attributes.size() > 0) {
			builder.append(" ");
		}
		for (Attribute attribute : this.attributes) {
			builder.append(attribute.toString() + " ");
		}
		if (this.wasClosedOnDef()) {
			builder.append("/>");
		} else {
			builder.append(">");
			for (Element element : this.childs) {
				builder.append("\n" + element.toString());
			}
			builder.append("\n");
			builder.append("</" + elementName + ">");
		}
		return builder.toString();
	}
}
