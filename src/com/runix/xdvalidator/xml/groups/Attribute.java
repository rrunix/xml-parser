package com.runix.xdvalidator.xml.groups;

public class Attribute {

	private String name;

	private String value;

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Attribute(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return name + "=\"" + value + "\"";
	}
}
