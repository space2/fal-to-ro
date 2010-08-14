package com.sss.ball;

import java.util.Vector;

import org.lwjgl.opengl.GL11;

public class GameState extends State {

    public static final float GAME_AREA_X = 32;
    public static final float GAME_AREA_Y = 32;
    public static final float GAME_AREA_H = 768 - 32;
    public static final float GAME_AREA_W = GAME_AREA_H;
    public static final float RACKET_Y = GAME_AREA_H - 32;

    private int mTexBalls;
    private int mTexRackets;
    private Vector<Sprite> mSprites = new Vector<Sprite>();
    private Racket mRacket = new Racket(this);
    private Vector<Ball> mBalls = new Vector<Ball>();

    protected GameState() {
        super(STATE_GAME);
    }

    @Override
    public void create() {
        super.create();
        mTexBalls = TextureUtil.loadTexture("/balls.png");
        mTexRackets = TextureUtil.loadTexture("/rackets.png");
        mSprites.add(mRacket);
        Ball ball = new Ball(this);
        mBalls.add(ball);
        mSprites.add(ball);
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
    }

    @Override
    public void render() {
        super.render();

        // render game area background
        G.drawRect(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_W, GAME_AREA_H, 0xff000000);

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
        for (Sprite sprite : mSprites) {
            sprite.tick(delta);
        }
    }

    public int getTexBalls() {
        return mTexBalls;
    }

    public int getTexRackets() {
        return mTexRackets;
    }

}
