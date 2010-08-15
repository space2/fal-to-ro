package com.sss.ball.bg;

import java.util.Vector;

import com.sss.ball.Background;
import com.sss.ball.G;
import com.sss.ball.GameState;
import com.sss.ball.Util;
import com.sss.ball.xml.XMLNode;

public class GradientBackground extends Background {

    static private class ColorItem {
        float mPos;
        int mColor;
        public ColorItem(float pos, int color) {
            mPos = pos;
            mColor = color;
        }
    }

    private Vector<ColorItem> mColors = new Vector<ColorItem>();

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        for (XMLNode child : xml) {
            float pos = Float.parseFloat(child.getAttr("pos"));
            int color = Util.parseColor(child.getAttr("color"));
            mColors.add(new ColorItem(pos, color));
        }
    }

    @Override
    public void render(float x, float y, float w, float h) {
        for (int i = 0; i < mColors.size() - 1; i++) {
            ColorItem from = mColors.get(i);
            ColorItem to = mColors.get(i+1);
            float y0 = h * from.mPos / 100.0f;
            float y1 = h * to.mPos / 100.0f;
            if (y1 > y0) {
                G.drawRectVGrad(x, y0, w, y1 - y0, from.mColor, to.mColor);
            }
        }
    }

}
