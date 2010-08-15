package com.sss.ball.bg;

import com.sss.ball.Background;
import com.sss.ball.G;
import com.sss.ball.GameState;
import com.sss.ball.Util;
import com.sss.ball.xml.XMLNode;

public class ColorBackground extends Background {

    private int mColor = 0xff000000;

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        mColor = Util.parseColor(xml.getAttr("color"));
    }

    @Override
    public void render(float x, float y, float w, float h) {
        G.drawRect(x, y, w, h, mColor);
    }

}
