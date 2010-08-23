package com.sss.ball;

public abstract class Sprite {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_RACKET = 1;
    public static final int TYPE_BALL = 2;
    public static final int TYPE_BRICK = 4;
    public static final int TYPE_BONUS = 8;

    private float mX, mY, mW, mH;
    private int mType;
    private GameState mGameState;

    protected Sprite(int type, GameState gameState) {
        mType = type;
        mGameState = gameState;
    }

    public int getType() {
        return mType;
    }

    public GameState getGameState() {
        return mGameState;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getWidth() {
        return mW;
    }

    public void setWidth(float w) {
        mW = w;
    }

    public float getHeight() {
        return mH;
    }

    public void setHeight(float h) {
        mH = h;
    }

    public void render() { }
    public void tick(int delta) { }
    public void handleEvent() { }
}
