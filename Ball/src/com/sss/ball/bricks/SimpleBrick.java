package com.sss.ball.bricks;

import com.sss.ball.Brick;
import com.sss.ball.G;
import com.sss.ball.GameState;

public class SimpleBrick extends Brick {

    private int mBrickType;

    public SimpleBrick(GameState gameState, int type, float x, float y, float width, float height) {
        super(gameState);
        mBrickType = type;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public SimpleBrick(GameState gameState, int type) {
        super(gameState);
        mBrickType = type;
    }

    @Override
    public void render() {
        super.render();
        drawBrickAt(0, 0);
    }

    protected void drawBrickAt(float dx, float dy) {
        int tmp = mBrickType - 1;
        int row = tmp >> 2;
        int col = tmp & 3;
        G.drawImage(getGameState().getTexBricks(),
                getX() + dx, getY() + dy, getWidth(), getHeight(),
                col * 64/256.0f, row * 32/256.0f, 64/256.0f, 32/256.0f);
    }

    public void setBrickType(int type) {
        mBrickType = type;
    }

    public int getBrickType() {
        return mBrickType;
    }

}
