package com.sss.ball.bricks;

import com.sss.ball.GameState;

public class MultiHitBrick extends SimpleBrick {

    private int mHitCount = 1;

    public MultiHitBrick(GameState gameState, int type) {
        super(gameState, type);
        switch (type) {
            case 5: mHitCount = 2;
            default: // TODO: unknown brick type
        }
    }

    @Override
    public boolean onHit() {
        // Change type
        switch (getBrickType()) {
        case 5: setBrickType(6); break;
        }
        // Count hits
        mHitCount--;
        return mHitCount == 0;
    }

}
