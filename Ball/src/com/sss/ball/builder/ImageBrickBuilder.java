package com.sss.ball.builder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.sss.ball.Brick;
import com.sss.ball.Builder;
import com.sss.ball.GameState;
import com.sss.ball.Util;
import com.sss.ball.bricks.MultiHitBrick;
import com.sss.ball.bricks.PixelBrick;
import com.sss.ball.bricks.SimpleBrick;
import com.sss.ball.xml.XMLNode;

public class ImageBrickBuilder extends Builder {

    static class BrickType {
        int color;
        int type;
        String className;
    }
    private Vector<BrickType> mMap = new Vector<BrickType>();

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        // Fetch parameters
        float brickWidth = Float.parseFloat(xml.getAttr("brickWidth"));
        float brickHeight = Float.parseFloat(xml.getAttr("brickHeight"));
        float x = 0;
        float y = 0;

        // Fetch mapping
        for (int i = 0; i < xml.getChildCount(); i++) {
            XMLNode child = xml.getChild(i);
            if (child.getName().equals("mapping")) {
                BrickType bt = new BrickType();
                bt.color = Util.parseColor(child.getAttr("color"));
                bt.type = Integer.parseInt(child.getAttr("type"));
                bt.className = child.getAttr("class");
                mMap.add(bt);
            }
        }

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
                    // first check the mapping
                    Brick brick = findBrick(gameState, color);
                    // if not found, simply colorize a brick
                    if (brick == null) {
                        brick = new PixelBrick(gameState, color, x, y, brickWidth, brickHeight);
                    } else {
                        // still need to store position and size
                        brick.setX(x);
                        brick.setY(y);
                        brick.setWidth(brickWidth);
                        brick.setHeight(brickHeight);
                    }

                    gameState.addBrick(brick);
                }
                x += brickWidth;
            }
            y += brickHeight;
        }
    }

    private Brick findBrick(GameState gameState, int color) {
        Brick brick = null;
        color &= 0xf0f0f0;
        for (BrickType bt : mMap) {
            if (color == (bt.color & 0xf0f0f0)) {
                // found a match, now create the appropiate brick
                // TODO: refactor so class names won't be hardcoded
                if (bt.className == null) {
                    brick = new SimpleBrick(gameState, bt.type);
                } else if (bt.className.equals("MultiHitBrick")) {
                    brick = new MultiHitBrick(gameState, bt.type);
                }
            }
        }
        return brick;
    }

}
