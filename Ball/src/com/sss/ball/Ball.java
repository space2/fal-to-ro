package com.sss.ball;

public class Ball extends Sprite {

    public static final int STATE_IDLE = 0;
    public static final int STATE_MOVING = 1;

    private static final float BALL_SIZE = 32;

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

        // check collision with left
        if (nx < 0) {
            nx = 0;
            mVX = -mVX;
        }
        // check collision with right
        if (nx > GameState.GAME_AREA_W - BALL_SIZE) {
            nx = GameState.GAME_AREA_W - BALL_SIZE;
            mVX = -mVX;
        }
        // check collision with top
        if (ny < 0) {
            ny = 0;
            mVY = -mVY;
        }

        if (ny > GameState.RACKET_Y) {
            // Too late to hit back, ball lost
            getGameState().onBallLost(this);
            return;
        }

        // check collision with racket
        if (ny > GameState.RACKET_Y - BALL_SIZE) {
            Racket r = getGameState().getRacket();
            if (nx >= r.getX() - BALL_SIZE/2 && nx < r.getX() + r.getWidth() + BALL_SIZE/2) {
                // succesfull bounce
                ny = GameState.RACKET_Y - BALL_SIZE;
                if (mVY > 0) {
                    mVY = -mVY;
                }
            }
        }

        // check collision with bricks
        final GameState gs = getGameState();
        for (int i = gs.getBrickCount()-1; i >= 0; i--) {
            Brick brick = gs.getBrick(i);
            int collision = CollisionUtil.checkCollisionCircleBox(
                    nx + BALL_SIZE/2, ny + BALL_SIZE/2, BALL_SIZE/2,
                    brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            if (collision != CollisionUtil.COL_NONE) {
                // There was some sort of collision
                // Bounce
                switch (collision) {
                case CollisionUtil.COL_TOP:
                    // Adjust Y and VY
                    ny = brick.getY() - BALL_SIZE;
                    if (mVY > 0) {
                        mVY = -mVY;
                    }
                    break;
                case CollisionUtil.COL_BOTTOM:
                    // Adjust Y and VY
                    ny = brick.getY() + brick.getHeight();
                    if (mVY < 0) {
                        mVY = -mVY;
                    }
                    break;
                case CollisionUtil.COL_LEFT:
                    // Adjust X and VX
                    nx = brick.getX() - BALL_SIZE;
                    if (mVX > 0) {
                        mVX = -mVX;
                    }
                    break;
                case CollisionUtil.COL_RIGHT:
                    // Adjust X and VX
                    nx = brick.getX() + brick.getWidth();
                    if (mVX < 0) {
                        mVX = -mVX;
                    }
                    break;
                // TODO: corners
                default:
                    break;
                }
                // Remove brick
                getGameState().onBrickHit(brick);
            }
        }

        // set new position
        setX(nx);
        setY(ny);
    }

    public void initPos(Racket r) {
        // center it on the racket
        setY(r.getY() - BALL_SIZE);
        setX(r.getX() + (r.getWidth() - BALL_SIZE) / 2);
    }

    public void setVX(float vx) {
        mVX = vx;
    }

    public void setVY(float vy) {
        mVY = vy;
    }

}
