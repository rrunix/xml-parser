package com.runix.xdvalidator.xml.validator.module.node;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public interface StringNodeAction {

	public void onStringReaded(String readed, XMLValidationContext context)
			throws ValidationException;
}
