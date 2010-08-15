package com.sss.ball.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

    private XMLNode mRoot = null;
    private XMLNode mCur = null;

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mCur = mCur.getParent();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        XMLNode node = new XMLNode(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            XMLAttr attr = new XMLAttr(attributes.getQName(i), attributes.getValue(i));
            node.addAttr(attr);
        }
        if (mRoot == null) {
            mRoot = node;
        }
        if (mCur != null) {
            mCur.addChild(node);
        }
        mCur = node;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (mCur == null) return;
        String text = new String(ch, start, length);
        mCur.addAttr(new XMLAttr("_text", text));
    }

    public XMLNode getRootNode() {
        return mRoot;
    }

}
