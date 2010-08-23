package com.sss.ball;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class TextureUtil {

    private static TextureUtil dummy = new TextureUtil();
    private static ClassLoader cl = dummy.getClass().getClassLoader();

    private TextureUtil() {}

    public static int loadTexture(String fileName) {
        try {
            // Open stream to resource
            InputStream is = cl.getResourceAsStream(fileName);
            if (is == null) {
                is = new FileInputStream("res" + fileName);
                if (is == null) {
                    System.out.println("! Cannot find resource: " + fileName);
                    return -1;
                }
            }

            // Decode image
            BufferedImage image = ImageIO.read(is);
            int w = image.getWidth();
            int h = image.getHeight();
            ByteBuffer argb = ByteBuffer.allocateDirect(w*h*4);
            argb.order(ByteOrder.nativeOrder());
            IntBuffer buff = argb.asIntBuffer();
            int arr[] = new int[w*h];
            image.getRGB(0, 0, w, h, arr, 0, w);
            for (int i = w*h-1; i >= 0; i--) {
                int c = arr[i];
                int a = (c >> 24) & 0xff;
                int r = (c >> 16) & 0xff;
                int g = (c >> 8) & 0xff;
                int b = (c >> 0) & 0xff;
                r = r * a / 255;
                g = g * a / 255;
                b = b * a / 255;
                arr[i] = (a << 24) | (b << 16) | (g << 8) | (r << 0);
            }
            buff.put(arr);
            buff.flip();

            int texId = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, argb);
            return texId;
        } catch (IOException e) {
            System.out.println("! Error loading texture resource: " + fileName);
            e.printStackTrace();
            return -1;
        }
    }

}
