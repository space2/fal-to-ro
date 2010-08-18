package com.sss.ball.builder;

import com.sss.ball.Builder;
import com.sss.ball.GameState;
import com.sss.ball.bricks.SimpleBrick;
import com.sss.ball.xml.XMLNode;

public class SimpleBrickBuilder extends Builder {

    private int mMapping[] = new int[256];

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        // Fetch parameters
        float brickWidth = Float.parseFloat(xml.getAttr("brickWidth"));
        float brickHeight = Float.parseFloat(xml.getAttr("brickHeight"));
        float x = 0;
        float y = 0;
        for (XMLNode node : xml) {
            if (node.getName().equals("mapping")) {
                char key = node.getAttr("char").charAt(0);
                int value = Integer.parseInt(node.getAttr("type"));
                mMapping[key] = value;
            } else if (node.getName().equals("row")) {
                x = 0;
                String bricks = node.getAttr("bricks");
                for (int i = 0; i < bricks.length(); i++) {
                    int type = mMapping[bricks.charAt(i)];
                    if (type != 0) {
                        // not en empty space => brick
                        SimpleBrick brick = new SimpleBrick(gameState, type, x, y, brickWidth, brickHeight);
                        gameState.addBrick(brick);
                    }
                    x += brickWidth;
                }
                y += brickHeight;

            }

        }
    }

}
