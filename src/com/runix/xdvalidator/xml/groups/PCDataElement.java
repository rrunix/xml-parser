package com.runix.xdvalidator.xml.groups;

public class PCDataElement extends Element {

	private String value;

	public PCDataElement(String elementName, String value) {
		super(elementName);
		this.value = value;
	}

	public String toString() {
		return value;
	}
}
