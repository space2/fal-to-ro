package com.sss.ball;

import com.sss.ball.xml.XMLNode;

public abstract class Logic {

    private GameState mGameState = null;

    public static Logic create(XMLNode builderNode, GameState gameState) {
        try {
            String className = "com.sss.ball.logic." + builderNode.getAttr("type");
            Class<?> clazz = Class.forName(className);
            Logic logic = (Logic) clazz.newInstance();
            logic.mGameState = gameState;
            logic.init(builderNode, gameState);
            return logic;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameState getGameState() {
        return mGameState;
    }

    abstract protected void init(XMLNode xml, GameState gameState);

    public void onBrickHit(Brick brick, boolean removed) {
        // NOP
    }
}
