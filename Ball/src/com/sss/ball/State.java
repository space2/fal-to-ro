package com.sss.ball;

public class State {

    public static final int STATE_GAME = 0;

    private int mId;

    protected State(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void handleEvents() { }

    public void tick(int delta) { }

    public void render() { }

    public void create() { }

    public void destroy() { }

    public void onExit() { }

    public void onEnter() { }

}
