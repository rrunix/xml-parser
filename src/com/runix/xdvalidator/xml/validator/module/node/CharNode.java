package com.runix.xdvalidator.xml.validator.module.node;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.IntegerWrapper;
import com.runix.xdvalidator.xml.validator.module.XMLValidationContext;

public class CharNode extends Node {

	private ActionNode action;

	public CharNode(String regexStart) {
		super(regexStart);
	}

	@Override
	public Result handle(IntegerWrapper pointer, char[] raw,
			XMLValidationContext context) throws ValidationException {
		pointer.value++;
		if (action != null) {
			action.onFinish(context);
		}
		return Result.OK;
	}

	public String toString() {
		return super.toString() + " " + this.getRegexStart();
	}

	public void setAction(ActionNode action) {
		this.action = action;
	}
}
