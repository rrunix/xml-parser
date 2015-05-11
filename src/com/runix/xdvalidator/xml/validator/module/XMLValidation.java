package com.runix.xdvalidator.xml.validator.module;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.node.Node;
import com.runix.xdvalidator.xml.validator.module.node.Result;

public class XMLValidation {

	private Node start;

	private String xml;

	private XMLValidationContext context;

	public XMLValidation(Node start, String xml) {
		this.start = start;
		this.xml = xml;
		this.context = new XMLValidationContext();
	}

	public void validate() throws ValidationException {
		char[] raw = xml.toCharArray();
		IntegerWrapper wrapper = new IntegerWrapper();
		Node actual = start;
		boolean showLine = true;
		try {
			while (wrapper.value < raw.length) {
				Result result = actual.handle(wrapper, raw, this.context);
				if (wrapper.value == raw.length) {
					break;
				}
				Node next = actual.getModuleFor(raw[wrapper.value]);
				if (next == null) {
					throw new ValidationException("Unexpected character ( "
							+ raw[wrapper.value] + " ), expected ( "
							+ actual.nextNodes() + " )");
				}
				actual = next;
			}

			context.validate();
			if (!actual.isFinal()) {
				showLine = false;
				throw new ValidationException(
						"End reached in a non-final node; non closed nodes "
								+ context.nonClosedNodes());
			}
		} catch (ValidationException exception) {
			String start = "";
			if (showLine) {
				start += "In line [ " + resolveLine(wrapper, raw) + " ] =>";
			}
			throw new ValidationException(start + " " + exception.getMessage());
		}
	}

	private String resolveLine(IntegerWrapper iw, char[] xml) {
		int lpivot = iw.value;
		while (lpivot >= 0 && xml[lpivot] != '\n') {
			--lpivot;
		}
		int rpivot = iw.value;
		while (rpivot < xml.length && xml[rpivot] != '\n') {
			++rpivot;
		}

		String result = new String(xml, lpivot, (rpivot - lpivot));
		return result;
	}

	public XMLValidationContext getContext() {
		return this.context;
	}
}
