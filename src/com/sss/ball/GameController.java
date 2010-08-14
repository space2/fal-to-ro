package com.sss.ball;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameController {

    private static final int PREFERRED_WIDTH = 1024;
    private static final int PREFERRED_HEIGHT = 768;
    private boolean mRunning;
    private long mLastTick = System.currentTimeMillis();
    private int mTexBalls;
    private int mTexRackets;

    /**
     * Main application loop.
     */
    public void run() {
        initDisplay();
        initTextures();
        mRunning = true;
        while (mRunning) {
            // measure last frame time
            long now = System.currentTimeMillis();
            int delta = (int) (now - mLastTick);
            mLastTick = now;

            handleEvents();
            tick(delta);
            render();

            if (Display.isCloseRequested()) {
                mRunning = false;
            }
        }
    }

    private void initTextures() {
        mTexBalls = TextureUtil.loadTexture("/balls.png");
        mTexRackets = TextureUtil.loadTexture("/rackets.png");
        System.out.println("mTexBalls=" + mTexBalls);
        System.out.println("mTexRackets=" + mTexRackets);
    }

    private void handleEvents() {
        // TODO Auto-generated method stub
    }

    private void tick(int delta) {
        // TODO Auto-generated method stub
    }

    private void render() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render a red square
        G.drawImage(mTexRackets, 100, 100, 300, 100, 0, 0, 128/256.0f, 32/256.0f);
        G.drawImage(mTexRackets, 150, 150, 300, 100, 0, 0, 128/256.0f, 32/256.0f);

        // Update the screen
        Display.update();
    }

    private void initDisplay() {
        try {
            // try to find the best display mode
            DisplayMode best = null;
            int score = Integer.MAX_VALUE;
            DisplayMode modes[] = Display.getAvailableDisplayModes();
            for (DisplayMode m : modes) {
                int dw = m.getWidth() - PREFERRED_WIDTH;
                int dh = m.getHeight() - PREFERRED_HEIGHT;
                int dd = Math.abs(dw) + Math.abs(dh);
                if (dd < score) {
                    best = m;
                    score = dd;
                }
            }

            // Set the display mode
            Display.setDisplayMode(best);
            Display.setFullscreen(false); // while debugging it's better to not use fullscreen
            Display.setTitle("A simple brickbreaker game");
            Display.setVSyncEnabled(true);
            Display.create();

            // Setup OpenGL
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, PREFERRED_WIDTH, PREFERRED_HEIGHT, 0, -1, 1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glViewport(0, 0, best.getWidth(), best.getHeight());
            GL11.glEnable(GL11.GL_TEXTURE);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glClearColor(1.0f, 0, 0, 1.0f);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

    }

}
