package com.sss.ball.xml;

public class XMLAttr {

    private String mKey;
    private String mValue;

    public XMLAttr() { }

    public XMLAttr(String key, String value) {
       mKey = key;
       mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

}
