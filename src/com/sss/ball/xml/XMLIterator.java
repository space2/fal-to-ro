package com.sss.ball.xml;

import java.util.Iterator;

public class XMLIterator implements Iterator<XMLNode> {

    private int mIdx = 0;
    private XMLNode mNode;

    public XMLIterator(XMLNode xmlNode) {
        mNode = xmlNode;
    }

    public boolean hasNext() {
        return mIdx < mNode.getChildCount();
    }

    public XMLNode next() {
        XMLNode ret = mNode.getChild(mIdx);
        mIdx++;
        return ret;
    }

    public void remove() {
        // NOP
    }

}
