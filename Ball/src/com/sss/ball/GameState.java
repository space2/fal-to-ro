package com.sss.ball;

import java.util.Vector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GameState extends State {

    public static final float GAME_AREA_X = 32;
    public static final float GAME_AREA_Y = 32;
    public static final float GAME_AREA_H = GameController.PREFERRED_HEIGHT - GAME_AREA_Y;
    public static final float GAME_AREA_W = 32*24;
    public static final float EXTRA_BOTTOM_GAP = 32; // can be used for home-shield
    public static final float RACKET_Y = GAME_AREA_H - Racket.RACKET_HEIGHT - EXTRA_BOTTOM_GAP;

    private static final float DIGIT_WIDTH = 48;
    private static final float DIGIT_HEIGHT = 64;

    private static final int GS_INIT = 0;
    private static final int GS_PLAY = 1;
    private static final int GS_LOST_BALL = 2;
    private static final int GS_LOST_GAME = 3;
    private static final int GS_ABOUT_TO_PLAY = 4;

    private static final String[] MSG_GAME_LOST = null;

    private static final int DEF_LIVES = 4;

    private int mTexBalls;
    private int mTexBonuses;
    private int mTexBricks;
    private int mTexDigits;
    private int mTexRackets;
    private Vector<Sprite> mSprites = new Vector<Sprite>();
    private Vector<Brick> mBricks = new Vector<Brick>();
    private Vector<Bonus> mBonuses = new Vector<Bonus>();
    private Vector<Logic> mLogic = new Vector<Logic>();
    private Racket mRacket = new Racket(this);
    private Vector<Ball> mBalls = new Vector<Ball>();
    private LevelLoader mLevelLoader = new LevelLoader(this);
    private Background mBg;
    private int mScore = 0;
    private int mLives = 0;

    private int mState = GS_INIT;
    private int mStateTime = 0;
    private int mLevel;

    public GameState() {
        super(STATE_GAME);
    }

    @Override
    public void create() {
        super.create();
        mTexBalls = TextureUtil.loadTexture("/gfx/balls.png");
        mTexBonuses = TextureUtil.loadTexture("/gfx/bonuses.png");
        mTexBricks = TextureUtil.loadTexture("/gfx/bricks.png");
        mTexDigits = TextureUtil.loadTexture("/gfx/digits.png");
        mTexRackets = TextureUtil.loadTexture("/gfx/rackets.png");

        startGame();
    }

    private void startGame() {
        // Reset game parameters
        mScore = 0;
        mLevel = 1;
        mLives = DEF_LIVES + 1;

        // Load the level
        loadLevel();
    }

    private void loadLevel() {
        // Build up the path to the level file
        String levelFile = "/packs/" + getGamePack() + "/level" + String.format("%02d", mLevel) + ".xml";
        loadLevel(levelFile);
    }

    private String getGamePack() {
        // TODO: implement selecting of game packs
        return "def";
    }

    private void loadLevel(String name) {
        // clean the scene
        resetLevel();

        // Load new level using level loader
        if (mLevelLoader.load(name)) {
            // Loaded successfully, so start playing
            setState(GS_ABOUT_TO_PLAY);
        } else {
            // TODO: probably no more levels
            System.out.println("! Failed to load level: " + name);
        }
    }

    private void setState(int newState) {
        mState = newState;
        mStateTime = Util.getUpTime();
    }

    /**
     * Reset the game to load a new level
     */
    private void resetLevel() {
        // remove everything
        mSprites.clear();
        mBalls.clear();
        mBricks.clear();
        mBonuses.clear();

        // add racket
        mSprites.add(mRacket);

        resetNewLife();
    }

    /**
     * Reset the game state to start a new life
     */
    private void resetNewLife() {
        mRacket.resetNewLife();
        removeAllBalls();
        mLives--;
        if (mLives > 0) {
            addNewBall();
        }
        setState(GS_PLAY);
    }

    private void removeAllBalls() {
        for (Ball ball : mBalls) {
            mSprites.remove(ball);
        }
        mBalls.clear();
    }

    @Override
    public void destroy() {
        super.destroy();
        mTexBalls = -1;
        mTexBonuses = -1;
        mTexBricks = -1;
        mTexDigits = -1;
        mTexRackets = -1;
    }

    @Override
    public void handleEvents() {
        super.handleEvents();

        switch (mState) {
        case GS_INIT:
            // NOP
            break;
        case GS_PLAY:
            handleEventsInPlay();
            break;
        case GS_LOST_GAME:
            handleEventsInLostGame();
            break;
        case GS_ABOUT_TO_PLAY:
            // NOP;
            break;
        }
    }

    private void handleEventsInPlay() {
        for (Sprite sprite : mSprites) {
            sprite.handleEvent();
        }

        // Check for mouse click to release the balls
        if (Mouse.isButtonDown(0)) {
            // release balls
            releaseBalls();
        }
    }

    private void handleEventsInLostGame() {
        // Check for mouse click to restart playing
        if (Mouse.isButtonDown(0)) {
            // start next life
            startGame();
        }
    }

    private void releaseBalls() {
        for (Ball ball : mBalls) {
            ball.fire();
        }
    }

    @Override
    public void render() {
        super.render();

        // Render the game itself
        renderGame();

        // Render the appropiate states as well
        switch (mState) {
        case GS_LOST_BALL:
            // Nothing extra, just wait a bit
            break;
        case GS_LOST_GAME:
            renderFade(false);
            renderMsg(MSG_GAME_LOST);
            break;
        case GS_ABOUT_TO_PLAY:
            if (renderFade(true)) {
                setState(GS_PLAY);
            }
            break;
        }
    }

    private void renderMsg(String msg[]) {
        // TODO Auto-generated method stub
    }

    private boolean renderFade(boolean in) {
        boolean ret = false;

        // calculate opacity from timer
        float alpha = (Util.getUpTime() - mStateTime) * 1.0f / 1000;
        if (alpha > 1.0f) {
            alpha = 1.0f;
            ret = true;
        }
        if (in) {
            alpha = 1.0f - alpha;
        }

        // Now render a solid rectangle
        int argb = ((int)(alpha * 128)) << 24;
        G.drawRect(0, 0, GameController.PREFERRED_WIDTH, GameController.PREFERRED_HEIGHT, argb);

        return ret;
    }

    private void renderGame() {
        // render game area background
        if (mBg == null) {
            G.drawRect(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_W, GAME_AREA_H, 0xff000000);
        } else {
            mBg.render(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_W, GAME_AREA_H);
        }

        // render sprites
        GL11.glPushMatrix();
        GL11.glTranslatef(GAME_AREA_X, GAME_AREA_Y, 0);

        // first render the bricks
        for (Brick brick: mBricks) {
            brick.render();
        }
        // then render the bonuses
        for (Bonus bonus : mBonuses) {
            bonus.render();
        }
        // then render the balls
        for (Ball ball: mBalls) {
            ball.render();
        }
        // The racket is always on top
        mRacket.render();

        GL11.glPopMatrix();

        // render the game state
        renderGameState(GAME_AREA_X + GAME_AREA_W + 32, GAME_AREA_Y + 32);
    }

    private void renderGameState(float x, float y) {
        // Draw score
        G.drawImage(mTexDigits, x, y, 256, 32, 0, 160/256.0f, 256/256.0f, 32/256.0f);
        y += 32;
        drawNumber(mScore, x, y);
        y += DIGIT_HEIGHT;
        // Gap
        y += 16;
        // Draw lives
        G.drawImage(mTexDigits, x, y, 256, 32, 0, 128/256.0f, 256/256.0f, 32/256.0f);
        y += 32;
        drawNumber(mLives, x, y);
        y += DIGIT_HEIGHT;

    }

    private void drawNumber(int score, float x, float y) {
        String s = Integer.toString(score);
        for (int i = 0; i < s.length(); i++) {
            int v = s.charAt(i) - '0';
            int row = v / 5;
            int col = v % 5;
            G.drawImage(mTexDigits, x, y, DIGIT_WIDTH, DIGIT_HEIGHT, col*48/256.0f, row*64/256.0f, 48/256.0f, 64/256.0f);
            x += DIGIT_WIDTH;
        }
    }

    @Override
    public void tick(int delta) {
        super.tick(delta);

        mRacket.tick(delta);
        for (int i = mBalls.size()-1; i >= 0; i--) {
            Ball ball = mBalls.get(i);
            ball.tick(delta);
        }
        for (Brick brick: mBricks) {
            brick.tick(delta);
        }
        for (int i = mBonuses.size()-1; i >= 0; i--) {
            Bonus bonus = mBonuses.get(i);
            bonus.tick(delta);
        }

        switch (mState) {
        case GS_LOST_BALL:
            tickLostBall();
            break;
        }

    }

    private void tickLostBall() {
        // After one second, start the next life
        if (mStateTime > 1000) {
            resetNewLife();
        }
    }

    public int getTexBalls() {
        return mTexBalls;
    }

    public int getTexBonuses() {
        return mTexBonuses;
    }

    public int getTexBricks() {
        return mTexBricks;
    }

    public int getTexDigits() {
        return mTexDigits;
    }

    public int getTexRackets() {
        return mTexRackets;
    }

    public void onRacketMoved(float delta) {
        for (Ball ball : mBalls) {
            ball.onRacketMoved(delta);
        }
    }

    public Racket getRacket() {
        return mRacket;
    }

    public void onBallLost(Ball ball) {
        // remove ball
        mBalls.remove(ball);
        mSprites.remove(ball);

        // if no balls left, then one life is lost
        if (mBalls.size() == 0) {
            onLifeLost();
        }
    }

    private void onLifeLost() {
        if (mLives == 1) {
            // Lost the last life
            setState(GS_LOST_GAME);
        } else {
            // There are still some lives left
            setState(GS_LOST_BALL);
        }
    }

    private void addNewBall() {
        Ball ball = new Ball(this);
        mBalls.add(ball);
        mSprites.add(ball);
        ball.initPos(mRacket);
    }

    public void setBackground(Background bg) {
        mBg = bg;
    }

    public void addBrick(Brick brick) {
        addSprite(brick);
        mBricks.add(brick);
    }

    public void addBonus(Bonus bonus) {
        addSprite(bonus);
        mBonuses.add(bonus);
    }

    public void removeBonus(Bonus bonus) {
        mSprites.remove(bonus);
        mBonuses.remove(bonus);
    }

    private void addSprite(Sprite sprite) {
        mSprites.add(sprite);
    }

    public int getBrickCount() {
        return mBricks.size();
    }

    public Brick getBrick(int idx) {
        return mBricks.get(idx);
    }

    public void onBrickHit(Brick brick) {
        boolean rm = false;
        if (brick.onHit()) {
            removeBrick(brick);
            rm = true;
        }

        for (Logic l : mLogic) {
            l.onBrickHit(brick, rm);
        }
    }

    private void removeBrick(Brick brick) {
        mBricks.remove(brick);
        mSprites.remove(brick);
    }

    public void addLogic(Logic logic) {
        mLogic.add(logic);
    }

}
