package com.sss.ball;

import org.lwjgl.opengl.GL11;

public class G {

    public static void drawRect(float x, float y, float w, float h, int argb) {
        float a = ((argb >> 24) & 0xff) / 255.0f;
        float r = ((argb >> 16) & 0xff) / 255.0f;
        float g = ((argb >> 8) & 0xff) / 255.0f;
        float b = ((argb >> 8) & 0xff) / 255.0f;
        float x2 = x + w;
        float y2 = y + h;
        GL11.glDisable(GL11.GL_TEXTURE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor4f(r, g, b, a);
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x2, y);
            GL11.glVertex2f(x2, y2);
            GL11.glVertex2f(x, y2);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawImage(int tex, float x, float y, float w, float h, float tx, float ty, float tw, float th) {
        float x2 = x + w;
        float y2 = y + h;
        float tx2 = tx + tw;
        float ty2 = ty + th;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(tx, ty);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(tx2, ty);
            GL11.glVertex2f(x2, y);
            GL11.glTexCoord2f(tx2, ty2);
            GL11.glVertex2f(x2, y2);
            GL11.glTexCoord2f(tx, ty2);
            GL11.glVertex2f(x, y2);
        }
        GL11.glEnd();
    }

    public static void drawImage(int tex, float x, float y, float w, float h, float tx, float ty, float tw, float th, float alpha) {
        float x2 = x + w;
        float y2 = y + h;
        float tx2 = tx + tw;
        float ty2 = ty + th;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor4f(alpha, alpha, alpha, alpha);
            GL11.glTexCoord2f(tx, ty);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(tx2, ty);
            GL11.glVertex2f(x2, y);
            GL11.glTexCoord2f(tx2, ty2);
            GL11.glVertex2f(x2, y2);
            GL11.glTexCoord2f(tx, ty2);
            GL11.glVertex2f(x, y2);
        }
        GL11.glEnd();
    }

}
