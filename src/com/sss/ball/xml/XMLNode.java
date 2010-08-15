package com.sss.ball.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLNode {

    private Vector<XMLNode> mChildren = new Vector<XMLNode>();
    private XMLNode mParent = null;
    private String mName;
    private XMLAttrs mAttrs = new XMLAttrs();

    private static XMLNode dummy = new XMLNode(null);
    private static ClassLoader cl = dummy.getClass().getClassLoader();
    private static SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    public XMLNode(String name) {
        mName = name;
    }

    public XMLNode getParent() {
        return mParent;
    }

    private void setParent(XMLNode parent) {
        mParent = parent;
    }

    public void addChild(XMLNode child) {
        child.setParent(this);
        mChildren.add(child);
    }

    public int getChildCount() {
        return mChildren.size();
    }

    public XMLNode getChild(int idx) {
        if (idx < 0 || idx >= mChildren.size()) {
            return null;
        }
        return mChildren.get(idx);
    }

    public void addAttr(XMLAttr attr) {
        mAttrs.add(attr);
    }

    public String getName() {
        return mName;
    }

    public String getAttr(String key) {
        return mAttrs.getValue(key);
    }

    public static XMLNode parse(String name) {
        InputStream is = cl.getResourceAsStream(name);
        if (is == null) {
            try {
                is = new FileInputStream("res" + name);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                is = null;
            }
        }
        if (is == null) {
            System.out.println("! Error opening xml file: " + name);
            return null;
        }

        return parse(is);
    }

    public static XMLNode parse(InputStream is) {
        try {
            SAXParser parser = parserFactory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            parser.parse(is, handler);
            return handler.getRootNode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
