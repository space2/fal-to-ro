package com.sss.ball;

import org.lwjgl.input.Mouse;

public class Racket extends Sprite {

    public static final float RACKET_HEIGHT = 16;

    private static final int RACKET_MIN_SIZE = 4;
    private static final int RACKET_DEF_SIZE = 16;
    private static final int RACKET_MAX_SIZE = 32;
    private static final int RACKET_SIZE_CHANGE = 2;
    private static final float RACKET_UNIT_SIZE = 8;

    private int mSize;

    /** When set to true, the ball will stick to the racket */
    private boolean mMagnet = false;

    public Racket(GameState gameState) {
        super(TYPE_RACKET, gameState);

        setHeight(RACKET_HEIGHT);
        setY(GameState.RACKET_Y);
        setX((GameState.GAME_AREA_W - getWidth()) / 2);

        resetNewLife();
    }

    private void setSize(int size) {
        // Clamp size to sane values
        if (size < RACKET_MIN_SIZE) {
            size = RACKET_MIN_SIZE;
        }
        if (size > RACKET_MAX_SIZE) {
            size = RACKET_MAX_SIZE;
        }

        // update values
        mSize = size;
        float cx = getX() + getWidth() / 2;
        setWidth(size * RACKET_UNIT_SIZE);
        setX(cx - getWidth() / 2);

        // Make sure racket is in game area
        if (getX() < 0) {
            setX(0);
        }
        if (getX() + getWidth() > GameState.GAME_AREA_W) {
            setX(GameState.GAME_AREA_W - getWidth());
        }
    }

    public void changeSize(int delta) {
        setSize(mSize + delta * RACKET_SIZE_CHANGE);
    }


    @Override
    public void render() {
        super.render();
        int tex = getGameState().getTexRackets();
        float x = getX();
        float y = getY();
        float w = getWidth() - 2*RACKET_UNIT_SIZE;
        float h = getHeight();
        // render left edge
        G.drawImage(tex, x, y, RACKET_UNIT_SIZE, h, 0, 0, 16/256.0f, 32/256.0f);
        x += RACKET_UNIT_SIZE;
        // render middle
        if (w > 0) {
            G.drawImage(tex, x, y, w, h, 16/256.0f, 0, 96/256.0f, 32/256.0f);
            x += w;
        }
        // render right edge
        G.drawImage(tex, x, y, RACKET_UNIT_SIZE, h, 112/256.0f, 0, 16/256.0f, 32/256.0f);
    }

    @Override
    public void handleEvent() {
        super.handleEvent();

        // Fetch mouse position
        float x = Mouse.getX() - GameState.GAME_AREA_X;

        // clamp to valid area
        float w2 = getWidth() / 2;
        if (x < w2) {
            x = w2;
        } else if (x >= GameState.GAME_AREA_W - w2) {
            x = GameState.GAME_AREA_W - w2 - 1;
        }
        x -= w2;

        // Update position
        float delta = x - getX();
        if (delta != 0) {
            setX(x);
            getGameState().onRacketMoved(delta);
        }
    }

    /**
     * Called when a ball hits this racket
     * @param ball The ball which hit this racket
     */
    public void onHit(Ball ball) {
        // calculate angle
        float maxAngle = (float) (80 * Math.PI / 180);
        float cxr = getX() + getWidth() / 2;
        float cxb = ball.getX() + ball.getWidth() / 2;
        float alpha = (cxr - cxb) * maxAngle / (getWidth() / 2);
        float speed = 300;
        ball.setVX((float) (-Math.sin(alpha) * speed));
        ball.setVY((float) (-Math.cos(alpha) * speed));
        // Check if sticky
        if (mMagnet) {
            ball.setState(Ball.STATE_IDLE);
        }
    }

    public void setMagnet(boolean b) {
        mMagnet = b;
    }

    public void resetNewLife() {
        setSize(RACKET_DEF_SIZE);
        mMagnet = false;
    }

}
