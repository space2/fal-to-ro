package com.sss.ball.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.sss.ball.Ball;
import com.sss.ball.Brick;
import com.sss.ball.GameState;

public class TestCollisionVisual extends VisualTestCase {

    private GameState mGameState;
    private Ball mBall;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mGameState = new GameState();
        mBall = null;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mGameState = null;
        mBall = null;
    }

    public void testBallMovement() {
        // Create a ball
        createBall(50, 50, 300, 400);

        // Do the simulation
        assertSim("", "Ball movement is incorrect");
    }

    public void testBallBrickCollisionTop() {
        // Create a ball
        createBall(50, 50, 500, 300);

        // create the racket
        createBrick(90, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top of the brick is incorrect");
    }

    public void testBallBrickCollisionBottom() {
        // Create a ball
        createBall(50, 250, 250, -300);

        // create the racket
        createBrick(90, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and bottom of the brick is incorrect");
    }

    public void testBallBrickCollisionLeft() {
        // Create a ball
        createBall(50, 50, 500, 300);

        // create the racket
        createBrick(150, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and left of the brick is incorrect");
    }

    public void testBallBrickCollisionRight() {
        // Create a ball
        createBall(300, 50, -500, 300);

        // create the racket
        createBrick(100, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and right of the brick is incorrect");
    }

    private void assertSim(String string, String string2) {
        Graphics2D g = getGraphics();

        for (int i = 0; i < 40; i++) {
            mBall.tick(25);
            int blue = 128 - i * 128 / 40;
            int green = i * 128 / 40;
            int col = 0x7f000000 | blue | (green << 8);
            g.setColor(new Color(col, true));
            g.drawArc((int)mBall.getX(), (int)mBall.getY(), (int)mBall.getWidth(), (int)mBall.getHeight(), 0, 360);
        }

        assertScreen("", "Collision between mBall and top of the brick is incorrect");
    }

    public void testBallBrickCollisionTopLeft0() {
        // Create a ball
        createBall(50, 50, 230, 150);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    public void testBallBrickCollisionTopLeft1() {
        // Create a ball
        createBall(50, 50, 250, 150);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    public void testBallBrickCollisionTopLeft2() {
        // Create a ball
        createBall(50, 50, 270, 150);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    public void testBallBrickCollisionTopRight0() {
        // Create a ball
        createBall(250, 50, -270, 150);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    public void testBallBrickCollisionBottomLeft0() {
        // Create a ball
        createBall(50, 200, 120, -160);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    public void testBallBrickCollisionBottomRight0() {
        // Create a ball
        createBall(250, 200, -170, -190);

        // create the racket
        createBrick(110, 100, 100, 50);

        // Do the simulation
        assertSim("", "Collision between ball and top-left corner of the brick is incorrect");
    }

    private void createBall(float x, float y, float vx, float vy) {
        mBall = new Ball(mGameState);
        mBall.setX(x);
        mBall.setY(y);
        mBall.setVX(vx);
        mBall.setVY(vy);
        mBall.setState(Ball.STATE_MOVING);
    }

    private void createBrick(float x, float y, float w, float h) {
        Brick brick = new FixedBrick(mGameState);
        brick.setX(x);
        brick.setY(y);
        brick.setWidth(w);
        brick.setHeight(h);
        mGameState.addBrick(brick);
        Graphics g = getGraphics();
        g.setColor(Color.RED);
        g.drawRect((int)x, (int)y, (int)w, (int)h);
    }

    public static class FixedBrick extends Brick {

        public FixedBrick(GameState gameState) {
            super(gameState);
        }

        @Override
        public boolean onHit() {
            return false;
        }

    }

}
