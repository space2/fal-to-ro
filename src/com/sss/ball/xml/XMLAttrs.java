package com.sss.ball.xml;

import java.util.Vector;

public class XMLAttrs {

    private Vector<XMLAttr> mAttrs = new Vector<XMLAttr>();

    public XMLAttrs() { }

    public String getValue(String key) {
        return getValue(findKey(key));
    }

    public String getValue(int idx) {
        if (idx < 0 || idx >= mAttrs.size()) {
            return null;
        }
        return mAttrs.get(idx).getValue();
    }

    public int findKey(String key) {
        if (key != null) {
            for (int i = 0; i < mAttrs.size(); i++) {
                XMLAttr attr = mAttrs.get(i);
                if (key.equals(attr.getKey())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void add(String key, String value) {
        int idx = findKey(key);
        if (idx < 0) {
            mAttrs.add(new XMLAttr(key, value));
        } else {
            mAttrs.get(idx).setValue(value);
        }
    }

    public void add(XMLAttr attr) {
        int idx = findKey(attr.getKey());
        if (idx < 0) {
            mAttrs.add(attr);
        } else {
            mAttrs.get(idx).setValue(attr.getValue());
        }
    }
}
