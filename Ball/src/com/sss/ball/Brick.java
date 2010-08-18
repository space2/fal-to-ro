package com.sss.ball;

public abstract class Brick extends Sprite {

    protected Brick(int type, GameState gameState) {
        super(type, gameState);
    }

    public boolean onHit() {
        // by default one hit is enough to remove it
        return true;
    }

}
