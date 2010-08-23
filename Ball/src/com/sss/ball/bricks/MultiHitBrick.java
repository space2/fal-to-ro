package com.sss.ball.bricks;

import com.sss.ball.GameState;

public class MultiHitBrick extends SimpleBrick {

    private static final int HIT_TIME = 1000; // 1 second
    private static final float AMPL = 3.0f;
    private static final float PARAM1 = 3.1f;
    private static final float PARAM2 = 4.3f;

    private int mHitCount = 1;
    private long mHitTime = -1;

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
        if (mHitCount == 0) {
            return true; // last hit
        } else {
            mHitTime = System.currentTimeMillis();
            return false; // more hits needed
        }
    }

    @Override
    public void render() {
        super.render();
        float dx = 0, dy = 0;
        if (mHitTime > 0) {
            long now = System.currentTimeMillis();
            int delta = (int) (now - mHitTime);
            if (delta < HIT_TIME) {
                float ampl = (HIT_TIME - delta) * AMPL / HIT_TIME;
                dx = (float) (Math.sin(delta / PARAM1) * ampl);
                dy = (float) (Math.cos(delta / PARAM2) * ampl);
            } else {
                mHitTime = -1;
            }
        }

        drawBrickAt(dx, dy);
    }


}
