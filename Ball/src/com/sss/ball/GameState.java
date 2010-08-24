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
    private int mLives = 4 + 1;

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

        loadLevel("/packs/def/level01.xml");
    }

    private void loadLevel(String name) {
        // TODO: clean the scene
        resetLevel();

        // Load new level using level loader
        if (!mLevelLoader.load(name)) {
            System.out.println("! Failed to load level: " + name);
        }
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
        for (Sprite sprite : mSprites) {
            sprite.handleEvent();
        }

        // Check for mouse click to release the balls
        if (Mouse.isButtonDown(0)) {
            // release balls
            releaseBalls();
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

        // // This doesn't work, since the tick changes mSprites
        // for (Sprite sprite : mSprites) {
        //     sprite.tick(delta);
        // }

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
        resetNewLife();
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
