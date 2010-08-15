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
        if (cy >= y && cy < y + y) {
            // check left
            if (cx < x && cx + cr >= x) {
                return COL_LEFT;
            }
            // check right
            if (cx > x + w && cx - cr <= x + w) {
                return COL_RIGHT;
            }
        }

        // TODO: check corners

        return COL_NONE;
    }

}
