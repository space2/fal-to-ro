package com.sss.ball;

public class Bonus extends Sprite {

    private static final float BONUS_SIZE = 64;

    public static final int BTYPE_INC_RACKET = 0;
    public static final int BTYPE_COUNT = 1;

    public static final String BTYPE_NAMES[] = {
        "rac+",
    };

    private static final float GRAVITY = 0.1f;

    private int mBonusType = -1;
    private float mVX;
    private float mVY;

    public Bonus(GameState gameState) {
        super(TYPE_BONUS, gameState);
        setWidth(BONUS_SIZE);
        setHeight(BONUS_SIZE);
    }

    public int getBonusType() {
        return mBonusType;
    }

    public void setBonusType(int type) {
        mBonusType = type;
    }

    @Override
    public void render() {
        super.render();
        int tmp = mBonusType;
        int row = tmp >> 3;
        int col = tmp & 7;
        G.drawImage(getGameState().getTexBonuses(),
                getX(), getY(), getWidth(), getHeight(),
                col * 32/256.0f, row * 32/256.0f, 32/256.0f, 32/256.0f);
    }

    public void setVX(float vx) {
        mVX = vx;
    }

    public void setVY(float vy) {
        mVY = vy;
    }

    public void centerOn(Sprite spr) {
        setX(spr.getX() + (spr.getWidth() - getWidth()) / 2);
        setY(spr.getY() + (spr.getHeight() - getHeight()) / 2);
    }

    @Override
    public void tick(int delta) {
        super.tick(delta);

        float nx = getX() + mVX;
        float ny = getY() + mVY;
        mVY += GRAVITY;
        setX(nx);
        setY(ny);

        Racket r = getGameState().getRacket();

        if (ny > r.getY() + r.getHeight()) {
            // Too late to hit back, ball lost
            getGameState().removeBonus(this);
            return;
        }

        // check collision with racket
        if (ny + getHeight() > r.getY()) {
            if (nx + getWidth() >= r.getX() && nx < r.getX() + r.getWidth()) {
                // bonus collected
                getGameState().removeBonus(this);
                applyBonus();
                return;
            }
        }

    }

    private void applyBonus() {
        switch (mBonusType) {
        case BTYPE_INC_RACKET:
            getGameState().getRacket().changeSize(+1);
            break;
        default:
            System.err.println("Unknown bonus type: " + mBonusType);
            break;
        }
    }

    public static int lookupType(String string) {
        return 0;
    }

}
