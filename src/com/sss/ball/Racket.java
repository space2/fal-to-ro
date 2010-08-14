package com.sss.ball;

public class Racket extends Sprite {

    private static final float RACKET_HEIGHT = 32;
    private static final int RACKET_MIN_SIZE = 2;
    private static final int RACKET_DEF_SIZE = 8;
    private static final int RACKET_MAX_SIZE = 16;
    private static final float RACKET_UNIT_SIZE = 16;

    private int mSize;

    public Racket(GameState gameState) {
        super(TYPE_RACKET, gameState);

        setSize(RACKET_DEF_SIZE);
        setHeight(RACKET_HEIGHT);
        setY(GameState.RACKET_Y);
        setX((GameState.GAME_AREA_W - getWidth()) / 2);

    }

    private void setSize(int size) {
        if (size < RACKET_MIN_SIZE) {
            size = RACKET_MIN_SIZE;
        }
        if (size > RACKET_MAX_SIZE) {
            size = RACKET_MAX_SIZE;
        }
        mSize = size;
        setWidth(size * RACKET_UNIT_SIZE);
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


}
