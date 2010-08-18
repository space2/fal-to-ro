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

    @Override
    public void render() {
        super.render();
        int tmp = mBrickType - 1;
        int row = tmp >> 2;
        int col = tmp & 3;
        G.drawImage(getGameState().getTexBricks(),
                getX(), getY(), getWidth(), getHeight(),
                col * 64/256.0f, row * 32/256.0f, 64/256.0f, 32/256.0f);
    }

}
