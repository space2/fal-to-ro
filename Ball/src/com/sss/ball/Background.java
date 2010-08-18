package com.sss.ball;

import com.sss.ball.xml.XMLNode;

public abstract class Background {

    public static Background create(XMLNode bgNode, GameState gameState) {
        try {
            String className = "com.sss.ball.bg." + bgNode.getAttr("type");
            Class<?> clazz = Class.forName(className);
            Background bg = (Background) clazz.newInstance();
            bg.init(bgNode, gameState);
            return bg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    abstract protected void init(XMLNode xml, GameState mGameState);

    abstract public void render(float x, float y, float w, float h);

}
