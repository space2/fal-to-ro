package com.sss.ball;

import java.util.Iterator;
import java.util.Vector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.sss.ball.bricks.SimpleBrick;

public class GameState extends State {

    public static final float GAME_AREA_X = 32;
    public static final float GAME_AREA_Y = 32;
    public static final float GAME_AREA_H = GameController.PREFERRED_HEIGHT - GAME_AREA_Y;
    public static final float GAME_AREA_W = 32*24;
    public static final float EXTRA_BOTTOM_GAP = 32; // can be used for home-shield
    public static final float RACKET_Y = GAME_AREA_H - Racket.RACKET_HEIGHT - EXTRA_BOTTOM_GAP;

    private int mTexBalls;
    private int mTexBricks;
    private int mTexRackets;
    private Vector<Sprite> mSprites = new Vector<Sprite>();
    private Vector<Brick> mBricks = new Vector<Brick>();
    private Racket mRacket = new Racket(this);
    private Vector<Ball> mBalls = new Vector<Ball>();
    private LevelLoader mLevelLoader = new LevelLoader(this);
    private Background mBg;
    private Builder mBuilder;

    protected GameState() {
        super(STATE_GAME);
    }

    @Override
    public void create() {
        super.create();
        mTexBalls = TextureUtil.loadTexture("/gfx/balls.png");
        mTexBricks = TextureUtil.loadTexture("/gfx/bricks.png");
        mTexRackets = TextureUtil.loadTexture("/gfx/rackets.png");

        loadLevel("/packs/def/level02.xml");

        mSprites.add(mRacket);
        addNewBall();
    }

    private void loadLevel(String name) {
        // TODO: clean the scene

        // Load new level using level loader
        if (!mLevelLoader.load(name)) {
            System.out.println("! Failed to load level: " + name);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mTexBalls = -1;
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
        for (Sprite sprite : mSprites) {
            sprite.render();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void tick(int delta) {
        super.tick(delta);

        // // This doesn't work, since the tick changes mSprites
        // for (Sprite sprite : mSprites) {
        //     sprite.tick(delta);
        // }

        mRacket.tick(delta);
        for (Ball ball: mBalls) {
            ball.tick(delta);
        }
        for (Brick brick: mBricks) {
            brick.tick(delta);
        }

    }

    public int getTexBalls() {
        return mTexBalls;
    }

    public int getTexBricks() {
        return mTexBricks;
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

        // if no balls left, add one
        if (mBalls.size() == 0) {
            addNewBall();
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

    public void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    public void addBrick(Brick brick) {
        addSprite(brick);
        mBricks.add(brick);
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
        if (brick.onHit()) {
            removeBrick(brick);
        }
    }

    private void removeBrick(Brick brick) {
        mBricks.remove(brick);
        mSprites.remove(brick);
    }

}