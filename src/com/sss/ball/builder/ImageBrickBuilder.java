package com.sss.ball.builder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

import com.sss.ball.Builder;
import com.sss.ball.GameState;
import com.sss.ball.Util;
import com.sss.ball.bricks.PixelBrick;
import com.sss.ball.bricks.SimpleBrick;
import com.sss.ball.xml.XMLNode;

public class ImageBrickBuilder extends Builder {

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        // Fetch parameters
        float brickWidth = Float.parseFloat(xml.getAttr("brickWidth"));
        float brickHeight = Float.parseFloat(xml.getAttr("brickHeight"));
        float x = 0;
        float y = 0;

        // decode image
        String imageName = xml.getAttr("image");
        BufferedImage image;
        try {
            image = ImageIO.read(Util.open(imageName));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int iw = image.getWidth();
        int ih = image.getHeight();
        int rgb[] = new int[iw*ih];
        image.getRGB(0, 0, iw, ih, rgb, 0, iw);
        for (int i = 0; i < ih; i++) {
            x = 0;
            for (int j = 0; j < iw; j++) {
                int color = rgb[i*iw + j];
                if (((color >> 24) & 0xff) >= 0xf0) {
                    // Opaque pixel, so add a brick
                    PixelBrick brick = new PixelBrick(gameState, color, x, y, brickWidth, brickHeight);
                    gameState.addBrick(brick);
                }
                x += brickWidth;
            }
            y += brickHeight;
        }
    }

}
