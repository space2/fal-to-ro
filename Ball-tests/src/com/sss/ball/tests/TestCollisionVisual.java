package com.sss.ball.tests;

import java.awt.Color;
import java.awt.Graphics;

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
        assertSim("ball", "Ball movement is incorrect");
    }

    public void testBallBrickCollisionTop() {
        // Create a ball
        createBall(50, 50, 500, 300);

        // create the racket
        createBrick(90, 100, 100, 50);

        // Do the simulation
        assertSim("ball_brick_top", "Collision between ball and top of the brick is incorrect");
    }

    public void testBallBrickCollisionBottom() {
        // Create a ball
        createBall(50, 250, 250, -300);

        // create the racket
        createBrick(90, 100, 100, 50);

        // Do the simulation
        assertSim("ball_brick_bottom", "Collision between ball and bottom of the brick is incorrect");
    }

    public void testBallBrickCollisionLeft() {
        // Create a ball
        createBall(50, 50, 500, 300);

        // create the racket
        createBrick(150, 100, 100, 50);

        // Do the simulation
        assertSim("ball_brick_left", "Collision between ball and left of the brick is incorrect");
    }

    public void testBallBrickCollisionRight() {
        // Create a ball
        createBall(300, 50, -500, 300);

        // create the racket
        createBrick(100, 100, 100, 50);

        // Do the simulation
        assertSim("ball_brick_right", "Collision between ball and right of the brick is incorrect");
    }

    private void assertSim(String string, String string2) {
        Graphics g = getGraphics();

        for (int i = 0; i < 40; i++) {
            mBall.tick(25);
            g.setColor(Color.BLUE);
            g.drawArc((int)mBall.getX(), (int)mBall.getY(), (int)mBall.getWidth(), (int)mBall.getHeight(), 0, 360);
        }

        assertScreen("ball_brick_top", "Collision between mBall and top of the brick is incorrect");
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
        Brick brick = new Brick(mGameState);
        brick.setX(x);
        brick.setY(y);
        brick.setWidth(w);
        brick.setHeight(h);
        mGameState.addBrick(brick);
        Graphics g = getGraphics();
        g.setColor(Color.RED);
        g.drawRect((int)x, (int)y, (int)w, (int)h);
    }

}
