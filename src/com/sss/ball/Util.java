package com.sss.ball;

public class Util {

    public static int parseColor(String color) {
        if (color.startsWith("#")) {
            color = color.substring(1);
        } else if (color.startsWith("0x") || color.startsWith("0X")) {
            color = color.substring(2);
        } else {
            // TODO: suspicious color value
        }
        // We use Long so we can parse values >= 0x80000000
        int ret = (int) Long.parseLong(color, 16);
        if (color.length() <= 6) {
            // alpha value not specified, assume opaque
            ret |= 0xff000000;
        }
        return ret;
    }

}
