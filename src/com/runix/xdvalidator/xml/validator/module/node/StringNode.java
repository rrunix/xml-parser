package com.runix.xdvalidator.xml.validator.module.node;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.IntegerWrapper;
import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public class StringNode extends Node {

	private StringNodeAction action;

	private static final String pattern = "[^<>\"]"; // "([a-zA-Z0-9:&;.-\\s]*)";

	public StringNode() {
		super(pattern);
	}

	@Override
	public Result handle(IntegerWrapper pointer, char[] raw,
			XMLValidationContext context) throws ValidationException {
		String readed = "";
		while (pointer.value < raw.length
				&& (raw[pointer.value] + "").matches(pattern)) {
			readed += raw[pointer.value];
			pointer.value++;
		}
		if (pointer.value == raw.length) {
			return Result.END_REACHED;
		}
		if (action != null) {
			action.onStringReaded(readed, context);
		}
		return Result.OK;
	}

	public void setAction(StringNodeAction action) {
		this.action = action;
	}
}
