package com.sss.ball;

public class CollisionUtil {

    public static final int COL_NONE            = 0;
    public static final int COL_TOP             = 1;
    public static final int COL_BOTTOM          = 2;
    public static final int COL_LEFT            = 3;
    public static final int COL_RIGHT           = 4;
    public static final int COL_TOP_LEFT        = 5;
    public static final int COL_TOP_RIGHT       = 6;
    public static final int COL_BOTTOM_LEFT     = 7;
    public static final int COL_BOTTOM_RIGHT    = 8;

    public static final int EDGES_TOP               = 1;
    public static final int EDGES_BOTTOM            = 2;
    public static final int EDGES_LEFT              = 4;
    public static final int EDGES_RIGHT             = 8;
    public static final int EDGES_TOP_LEFT_RIGHT    = EDGES_TOP | EDGES_LEFT | EDGES_RIGHT;
    public static final int EDGES_ALL = EDGES_TOP | EDGES_LEFT | EDGES_RIGHT | EDGES_BOTTOM;

    // Temporary fields to store collision detection information
    private static float mTmpX;
    private static float mTmpY;
    private static float mTmpVX;
    private static float mTmpVY;
    private static float mTmpR;

    public static int checkCollisionCircleBox(
            float cx, float cy, float cr,
            float x, float y, float w, float h)
    {
        // First do a quick box-vs-box check to see if there is even a chance they overlap
        if (cx - cr > x + w) return COL_NONE;
        if (cx + cr < x) return COL_NONE;
        if (cy - cr > y + h) return COL_NONE;
        if (cy + cr < y) return COL_NONE;

        // Now do the special checks
        if (cx >= x && cx < x + w) {
            // check top
            if (cy < y && cy + cr >= y) {
                return COL_TOP;
            }
            // check bottom
            if (cy > y + h && cy - cr <= y + h) {
                return COL_BOTTOM;
            }
        }
        if (cy >= y && cy < y + h) {
            // check left
            if (cx < x && cx + cr >= x) {
                return COL_LEFT;
            }
            // check right
            if (cx > x + w && cx - cr <= x + w) {
                return COL_RIGHT;
            }
        }

        // Check the corners
        if (checkDist(cx, cy, x, y, cr)) {
            return COL_TOP_LEFT;
        }
        // Check the corners
        if (checkDist(cx, cy, x + w - 1, y, cr)) {
            return COL_TOP_RIGHT;
        }
        // Check the corners
        if (checkDist(cx, cy, x, y + h - 1, cr)) {
            return COL_BOTTOM_LEFT;
        }
        // Check the corners
        if (checkDist(cx, cy, x + w - 1, y + h - 1, cr)) {
            return COL_BOTTOM_RIGHT;
        }

        return COL_NONE;
    }

    private static boolean checkDist(float cx, float cy, float x, float y, float cr) {
        float dx = cx - x;
        float dy = cy - y;
        return (dx*dx + dy*dy < cr*cr);
    }

    public static void setupBall(float x, float y, float r, float vx, float vy) {
        mTmpX = x;
        mTmpY = y;
        mTmpR = r;
        mTmpVX = vx;
        mTmpVY = vy;
    }

    public static int doCollisionInBox(float x, float y, float w, float h, int edges) {
        int ret = 0;

        // check collision with left
        if ((edges & EDGES_LEFT) != 0) {
            if (mTmpX < x + mTmpR) {
                mTmpX = x + mTmpR;
                ret |= EDGES_LEFT;
                if (mTmpVX < 0) {
                    mTmpVX = -mTmpVX;
                }
            }
        }

        // check collision with right
        if ((edges & EDGES_RIGHT) != 0) {
            if (mTmpX > x + w - mTmpR) {
                mTmpX = x + w - mTmpR;
                ret |= EDGES_RIGHT;
                if (mTmpVX > 0) {
                    mTmpVX = -mTmpVX;
                }
            }
        }

        // check collision with top
        if ((edges & EDGES_TOP) != 0) {
            if (mTmpY < y + mTmpR) {
                mTmpY = y + mTmpR;
                ret |= EDGES_TOP;
                if (mTmpVY < 0) {
                    mTmpVY = -mTmpVY;
                }
            }
        }

        // check collision with bottom
        if ((edges & EDGES_BOTTOM) != 0) {
            if (mTmpY > y + h - mTmpR) {
                mTmpY = y + h - mTmpR;
                ret |= EDGES_BOTTOM;
                if (mTmpVY > 0) {
                    mTmpVY = -mTmpVY;
                }
            }
        }

        return ret;
    }

    public static int doCollisionWithBox(float x, float y, float w, float h) {
        int collision = CollisionUtil.checkCollisionCircleBox(mTmpX, mTmpY, mTmpR, x, y, w, h);
        double alpha = 0;
        float cosa = 0, sina = 0, nvx, nvy, dx, dy;
        if (collision != CollisionUtil.COL_NONE) {
            // There was some sort of collision
            // Bounce
            switch (collision) {
            case CollisionUtil.COL_TOP:
                // Adjust Y and VY
                mTmpY = y - mTmpR;
                if (mTmpVY > 0) {
                    mTmpVY = -mTmpVY;
                }
                break;
            case CollisionUtil.COL_BOTTOM:
                // Adjust Y and VY
                mTmpY = y + h + mTmpR;
                if (mTmpVY < 0) {
                    mTmpVY = -mTmpVY;
                }
                break;
            case CollisionUtil.COL_LEFT:
                // Adjust X and VX
                mTmpX = x - mTmpR;
                if (mTmpVX > 0) {
                    mTmpVX = -mTmpVX;
                }
                break;
            case CollisionUtil.COL_RIGHT:
                // Adjust X and VX
                mTmpX = x + w + mTmpR;
                if (mTmpVX < 0) {
                    mTmpVX = -mTmpVX;
                }
                break;
            // TODO: corners
            case CollisionUtil.COL_TOP_LEFT:
                dx = x - mTmpX;
                dy = y - mTmpY;
                alpha = Math.atan2(dx, dy);
                cosa = (float) Math.cos(alpha);
                sina = (float) Math.sin(alpha);
                // rotate with alpha
                nvx = mTmpVX * cosa - mTmpVY * sina;
                nvy = mTmpVY * cosa + mTmpVX * sina;
                // reflect
                nvy = -nvy;
                // rotate back
                mTmpVX = nvx * cosa + nvy * sina;
                mTmpVY = nvy * cosa - nvx * sina;
                break;
            default:
                break;
            }
        }
        return collision;
    }

    public static float getNewX() {
        return mTmpX;
    }

    public static float getNewY() {
        return mTmpY;
    }

    public static float getNewVX() {
        return mTmpVX;
    }

    public static float getNewVY() {
        return mTmpVY;
    }

}
