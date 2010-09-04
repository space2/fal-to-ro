package com.sss.ball;

public class Ball extends Sprite {

    public static final int STATE_IDLE = 0;
    public static final int STATE_MOVING = 1;

    private static final float BALL_SIZE = 16;

    private int mState = STATE_IDLE;
    private float mVX;
    private float mVY;

    public Ball(GameState gameState) {
        super(TYPE_BALL, gameState);

        setWidth(BALL_SIZE);
        setHeight(BALL_SIZE);
        setY(GameState.RACKET_Y - BALL_SIZE);
        setX((GameState.GAME_AREA_W - BALL_SIZE) / 2);

        // TODO: this values are only for test, need to change them
        mVX = 100;
        mVY = -245;
    }

    @Override
    public void render() {
        super.render();
        G.drawImage(getGameState().getTexBalls(), getX(), getY(), getWidth(), getHeight(), 0, 0, 32/64.0f, 32/64.0f);
    }

    public void onRacketMoved(float delta) {
        // if still resting on the racket, move with the racket
        if (mState == STATE_IDLE) {
            // Update X coordinate
            float x = getX() + delta;

            // But make sure we clamp it
            float w = getWidth();
            if (x < 0) {
                x = 0;
            } else if (x >= GameState.GAME_AREA_W - w) {
                x = GameState.GAME_AREA_W - w;
            }
            setX(x);
        }
    }

    public void fire() {
        if (mState != STATE_IDLE) return;

        mState = STATE_MOVING;
    }

    public void setState(int newState) {
        mState = newState;
    }

    @Override
    public void tick(int delta) {
        super.tick(delta);

        if (delta <= 0) return; // too short time interval
        if (mState != STATE_MOVING) return; // not moving

        float nx = getX() + mVX * delta / 1000.0f;
        float ny = getY() + mVY * delta / 1000.0f;

        final float size = getWidth();

        CollisionUtil.setupBall(nx + size/2, ny + size/2, size/2, mVX, mVY);

        CollisionUtil.doCollisionInBox(0, 0, GameState.GAME_AREA_W, GameState.GAME_AREA_H, CollisionUtil.EDGES_TOP_LEFT_RIGHT);

        // check collision with bricks
        final GameState gs = getGameState();
        for (int i = gs.getBrickCount()-1; i >= 0; i--) {
            Brick brick = gs.getBrick(i);
            int col = CollisionUtil.doCollisionWithBox(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            if (col != CollisionUtil.COL_NONE) {
                // Remove brick
                getGameState().onBrickHit(brick);
            }
        }

        nx = CollisionUtil.getNewX();
        ny = CollisionUtil.getNewY();
        float nvx = CollisionUtil.getNewVX();
        float nvy = CollisionUtil.getNewVY();

        if (ny > GameState.RACKET_Y + size/2) {
            // Too late to hit back, ball lost
            getGameState().onBallLost(this);
            return;
        }

        boolean hitRacket = false;
        Racket r = getGameState().getRacket();

        // check collision with racket
        if (ny > GameState.RACKET_Y - size/2) {
            if (nx >= r.getX()&& nx < r.getX() + r.getWidth()) {
                // succesfull bounce
                hitRacket = true;
                // adjust Y position
                ny = GameState.RACKET_Y - size/2;
                // do simple bouncing
                if (nvy > 0) {
                    nvy = -nvy;
                }
            }
        }

        // set new position
        setX(nx - size/2);
        setY(ny - size/2);
        setVX(nvx);
        setVY(nvy);

        // The racket might want to change the angle, or stick the ball, etc
        if (hitRacket) {
            r.onHit(this);
        }
    }

    public void initPos(Racket r) {
        // center it on the racket
        setY(r.getY() - getHeight());
        setX(r.getX() + (r.getWidth() - getWidth()) / 2);
    }

    public void setVX(float vx) {
        mVX = vx;
    }

    public void setVY(float vy) {
        mVY = vy;
    }

    public void resetNewLife() {
        // TODO: reset speed
    }

}
