package com.runix.xdvalidator.xml.validator.module.node;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.IntegerWrapper;
import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public class SingleCharNodeSplit extends Node {

	public SingleCharNodeSplit(String regexStart) {
		super(regexStart);
	}

	@Override
	public Result handle(IntegerWrapper pointer, char[] raw,
			XMLValidationContext context) throws ValidationException {
		pointer.value++;
		return Result.OK;
	}

}
