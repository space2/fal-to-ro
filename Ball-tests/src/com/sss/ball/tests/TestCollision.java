package com.sss.ball.tests;

import com.sss.ball.CollisionUtil;

import junit.framework.TestCase;

public class TestCollision extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Simple test to verify that collision is not detected
     * for obvious use cases
     */
    public void testCheckCollisionCircleBox00() {
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(10, 10, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(150, 10, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(300, 10, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(10, 125, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(300, 125, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(10, 200, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(150, 200, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_NONE,
                CollisionUtil.checkCollisionCircleBox(300, 200, 10, 100, 100, 100, 50));
    }

    /**
     * Check collision with edges
     */
    public void testCheckCollisionCircleBox01() {
        assertEquals(CollisionUtil.COL_TOP,
                CollisionUtil.checkCollisionCircleBox(150, 95, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_BOTTOM,
                CollisionUtil.checkCollisionCircleBox(150, 155, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_LEFT,
                CollisionUtil.checkCollisionCircleBox(95, 125, 10, 100, 100, 100, 50));
        assertEquals(CollisionUtil.COL_RIGHT,
                CollisionUtil.checkCollisionCircleBox(205, 125, 10, 100, 100, 100, 50));
    }


}
