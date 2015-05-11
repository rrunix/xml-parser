package com.runix.xdvalidator.xml.validator.module;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.node.ActionNode;
import com.runix.xdvalidator.xml.validator.module.node.CharNode;
import com.runix.xdvalidator.xml.validator.module.node.Node;
import com.runix.xdvalidator.xml.validator.module.node.SkipWhiteSpacesNode;
import com.runix.xdvalidator.xml.validator.module.node.StringNode;
import com.runix.xdvalidator.xml.validator.module.node.StringNodeAction;
import com.runix.xdvalidator.xml.validator.module.node.StringNodeWSpaces;

public class XMLValidatorBuilder {

	public XMLValidatorBuilder() {

	}

	public Node build() {
		Node[] rootElement = element();
		SkipWhiteSpacesNode swsn1 = new SkipWhiteSpacesNode();
		SkipWhiteSpacesNode swsn2 = new SkipWhiteSpacesNode();
		Node pcdata = pcData();

		// Final nodes
		rootElement[3].markAsFinal();
		swsn2.markAsFinal();
		rootElement[1].markAsFinal();

		// spaces before <
		swsn1.addChild(rootElement[0]);

		// two element without spaces ><
		rootElement[1].addChild(rootElement[0]);
		rootElement[2].addChild(rootElement[0]);
		rootElement[3].addChild(rootElement[0]);

		// two element with spaces, added swsn2 because pcdata can also has
		// spaces
		rootElement[1].addChild(swsn2);
		rootElement[2].addChild(swsn2);
		rootElement[3].addChild(swsn2);

		rootElement[1].addChild(rootElement[0]);
		rootElement[2].addChild(rootElement[0]);
		rootElement[3].addChild(rootElement[0]);

		rootElement[1].addChild(pcdata);
		rootElement[2].addChild(pcdata);
		rootElement[3].addChild(pcdata);

		swsn2.addChild(rootElement[0]);
		swsn2.addChild(pcdata);

		// After pcdata must be a element
		pcdata.addChild(rootElement[0]);

		return swsn1;
	}

	public Node pcData() {
		StringNode pcData = new StringNode();
		pcData.setAction(new PCDATA());
		return pcData;
	}

	public Node[] element() {
		SkipWhiteSpacesNode node = new SkipWhiteSpacesNode();
		CharNode cNode = new CharNode("<");
		StringNodeWSpaces elementName = new StringNodeWSpaces();
		StringNodeWSpaces elementCloseName = new StringNodeWSpaces();
		SkipWhiteSpacesNode swsn1 = new SkipWhiteSpacesNode();
		SkipWhiteSpacesNode swsn2 = new SkipWhiteSpacesNode();
		CharNode close = new CharNode(">");
		CharNode closeClose = new CharNode(">");
		CharNode slash = new CharNode("/");
		CharNode closeSlash = new CharNode(">");
		CharNode startSlash = new CharNode("/");

		Node[] atStarEnd = attribute();
		Node attributeStart = atStarEnd[0];
		Node attributeEnd = atStarEnd[1];

		close.setAction(new CloseElementDef());
		closeSlash.setAction(new CloseElementOnDef());
		elementName.setAction(new OpenElement());
		elementCloseName.setAction(new CloseElement());

		{
			// Handle </hola>
			cNode.addChild(startSlash);
			startSlash.addChild(elementCloseName);
			elementCloseName.addChild(swsn2);
			elementCloseName.addChild(closeClose);
			swsn2.addChild(closeClose);
			elementCloseName.addChild(closeClose);
		}
		{
			// [white spaces] <
			node.addChild(cNode);

			// <
			cNode.addChild(elementName);
			// <elementName
			elementName.addChild(swsn1);

			// <elementName> | <elementName/>
			elementName.addChild(close);
			elementName.addChild(slash);

			// (elementName />|>|attribute) | />|>|attribute
			swsn1.addChild(attributeStart);
			swsn1.addChild(close);
			swsn1.addChild(slash);
			// />
			slash.addChild(closeSlash);

			// />|>|attribute
			attributeEnd.addChild(swsn1);
			attributeEnd.addChild(slash);
			attributeEnd.addChild(close);
		}

		return new Node[] { cNode, closeSlash, close, closeClose };
	}

	public Node[] attribute() {
		StringNodeWSpaces name = new StringNodeWSpaces();
		SkipWhiteSpacesNode wsn1 = new SkipWhiteSpacesNode();
		SkipWhiteSpacesNode wsn2 = new SkipWhiteSpacesNode();
		CharNode equals = new CharNode("=");
		CharNode firstCom = new CharNode("\"");
		CharNode secondCom = new CharNode("\"");
		StringNode value = new StringNode();

		name.setAction(new AttributeName());
		value.setAction(new AttributeValue());

		// value= | value =
		name.addChild(wsn1);
		name.addChild(equals);

		// value =
		wsn1.addChild(equals);

		// =" | = "
		equals.addChild(wsn2);
		equals.addChild(firstCom);

		// = "
		wsn2.addChild(firstCom);

		// "characters-here
		firstCom.addChild(value);

		// characters-here"
		value.addChild(secondCom);
		return new Node[] { name, secondCom };
	}

	class AttributeValue implements StringNodeAction {
		@Override
		public void onStringReaded(String readed, XMLValidationContext context)
				throws ValidationException {
			context.addAttributeValue(readed);
		}
	}

	class PCDATA implements StringNodeAction {
		@Override
		public void onStringReaded(String readed, XMLValidationContext context)
				throws ValidationException {
			context.addPCData(readed);
		}
	}

	class AttributeName implements StringNodeAction {
		@Override
		public void onStringReaded(String readed, XMLValidationContext context) {
			context.addAttributeName(readed);
		}
	}

	class OpenElement implements StringNodeAction {
		@Override
		public void onStringReaded(String readed, XMLValidationContext context) {
			context.openElement(readed);
		}
	}

	class CloseElementOnDef implements ActionNode {
		@Override
		public void onFinish(XMLValidationContext context) {
			context.closeElementOnDef();
		}
	}

	class CloseElementDef implements ActionNode {
		@Override
		public void onFinish(XMLValidationContext context) {
			context.closeElementDef();
		}
	}

	class CloseElement implements StringNodeAction {

		@Override
		public void onStringReaded(String readed, XMLValidationContext context) {
			context.closeElement(readed);
		}

	}
}
