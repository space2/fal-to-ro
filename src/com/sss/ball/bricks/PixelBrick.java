package com.sss.ball.bricks;

import com.sss.ball.Brick;
import com.sss.ball.G;
import com.sss.ball.GameState;

public class PixelBrick extends Brick {

    private int mBrickColor;

    public PixelBrick(GameState gameState, int color, float x, float y, float width, float height) {
        super(TYPE_BRICK, gameState);
        mBrickColor = color;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render() {
        super.render();
        G.drawImageColored(getGameState().getTexBricks(), mBrickColor,
                getX(), getY(), getWidth(), getHeight(),
                128/256.0f, 0.0f, 32/256.0f, 32/256.0f);
    }

}
