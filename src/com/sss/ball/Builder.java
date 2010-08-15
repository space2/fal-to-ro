package com.sss.ball;

import com.sss.ball.xml.XMLNode;

public abstract class Builder {

    public static Builder create(XMLNode builderNode, GameState gameState) {
        try {
            String className = "com.sss.ball.builder." + builderNode.getAttr("type");
            Class<?> clazz = Class.forName(className);
            Builder builder = (Builder) clazz.newInstance();
            builder.init(builderNode, gameState);
            return builder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    abstract protected void init(XMLNode xml, GameState gameState);

    abstract public void run();
}
