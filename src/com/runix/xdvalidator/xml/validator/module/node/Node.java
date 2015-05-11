package com.runix.xdvalidator.xml.validator.module.node;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.IntegerWrapper;
import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public abstract class Node {

	private String regexStart;

	protected List<Node> modules;

	protected boolean isFinal;

	public Node(String regexStart) {
		super();
		this.regexStart = regexStart;
		this.modules = new ArrayList<>();
	}

	public abstract Result handle(IntegerWrapper pointer, char[] raw,
			XMLValidationContext context) throws ValidationException;

	public boolean match(char start) {
		return (start + "").matches(regexStart);
	}

	public String getRegexStart() {
		return regexStart;
	}

	public void addChild(Node module) {
		this.modules.add(module);
	}

	public Node getModuleFor(char next) {
		for (Node module : modules) {
			if (module.match(next)) {
				return module;
			}
		}
		return null;
	}

	public String nextNodes() {
		String result = "[ ";
		for (Node module : modules) {
			result += module.getRegexStart() + " , ";
		}
		if (modules.size() > 0) {
			result = result.substring(0, result.length() - ", ".length());
		}
		result += " ]";
		return result;
	}

	public void markAsFinal() {
		this.isFinal = true;
	}

	public boolean isFinal() {
		return this.isFinal;
	}

	public List<Node> getModules() {
		return this.modules;
	}
}
