package com.sss.ball;

public class GameState extends State {

    private int mTexBalls;
    private int mTexRackets;

    protected GameState() {
        super(STATE_GAME);
    }

    @Override
    public void create() {
        super.create();
        mTexBalls = TextureUtil.loadTexture("/balls.png");
        mTexRackets = TextureUtil.loadTexture("/rackets.png");
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
    }

    @Override
    public void render() {
        super.render();
        // Render an image
        G.drawImage(mTexRackets, 100, 100, 300, 100, 0, 0, 128/256.0f, 32/256.0f);
    }

    @Override
    public void tick(int delta) {
        // TODO Auto-generated method stub
        super.tick(delta);
    }

}
