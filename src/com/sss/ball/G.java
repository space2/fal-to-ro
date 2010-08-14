package com.sss.ball;

import org.lwjgl.opengl.GL11;

public class G {

    public static void drawImage(int tex, int x, int y, int w, int h, float tx, float ty, float tw, float th) {
        int x2 = x + w;
        int y2 = y + h;
        float tx2 = tx + tw;
        float ty2 = ty + th;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(tx, ty);
            GL11.glVertex2i(x, y);
            GL11.glTexCoord2f(tx2, ty);
            GL11.glVertex2i(x2, y);
            GL11.glTexCoord2f(tx2, ty2);
            GL11.glVertex2i(x2, y2);
            GL11.glTexCoord2f(tx, ty2);
            GL11.glVertex2i(x, y2);
        }
        GL11.glEnd();
    }

    public static void drawImage(int tex, int x, int y, int w, int h, float tx, float ty, float tw, float th, float alpha) {
        int x2 = x + w;
        int y2 = y + h;
        float tx2 = tx + tw;
        float ty2 = ty + th;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor4f(alpha, alpha, alpha, alpha);
            GL11.glTexCoord2f(tx, ty);
            GL11.glVertex2i(x, y);
            GL11.glTexCoord2f(tx2, ty);
            GL11.glVertex2i(x2, y);
            GL11.glTexCoord2f(tx2, ty2);
            GL11.glVertex2i(x2, y2);
            GL11.glTexCoord2f(tx, ty2);
            GL11.glVertex2i(x, y2);
        }
        GL11.glEnd();
    }

}
