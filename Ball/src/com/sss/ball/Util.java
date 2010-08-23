package com.sss.ball;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Util {

    private static Util dummy = new Util();
    private Util() {}

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

    public static InputStream open(String resName) {
        InputStream is = dummy.getClass().getResourceAsStream(resName);
        if (is == null) {
            try {
                is = new FileInputStream("res" + resName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return is;
    }

}
