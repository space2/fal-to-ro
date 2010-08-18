package com.sss.ball;

public class Brick extends Sprite {

    public Brick(GameState gameState) {
        super(TYPE_BRICK, gameState);
    }

    public boolean onHit() {
        // by default one hit is enough to remove it
        return true;
    }

}
