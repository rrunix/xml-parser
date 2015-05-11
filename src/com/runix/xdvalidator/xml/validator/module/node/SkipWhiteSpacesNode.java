package com.runix.xdvalidator.xml.validator.module.node;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.IntegerWrapper;
import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public class SkipWhiteSpacesNode extends Node {

	public SkipWhiteSpacesNode() {
		super("\\s");
	}

	@Override
	public Result handle(IntegerWrapper pointer, char[] raw,
			XMLValidationContext context) throws ValidationException {
		while (pointer.value < raw.length
				&& (raw[pointer.value] + "").matches("\\s")) {
			pointer.value++;
		}

		if (pointer.value == raw.length) {
			return Result.END_REACHED;
		}
		return Result.OK;
	}

}
