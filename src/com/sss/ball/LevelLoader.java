package com.sss.ball;

import com.sss.ball.xml.XMLNode;

public class LevelLoader {

    private GameState mGameState;

    public LevelLoader(GameState gameState) {
        mGameState = gameState;
    }

    public boolean load(String name) {
        XMLNode root = XMLNode.parse(name);
        if (root == null) {
            return false;
        }

        if (!"level".equals(root.getName())) {
            // Invalid xml file
            System.out.println("! Invalid xml file: " + name);
            return false;
        }

        // Fetch and print some info
        XMLNode title = root.findChild("title");
        XMLNode author = root.findChild("author");
        System.out.println("Title: " + (title == null ? "(unknown)" : title.getText()));
        System.out.println("Author: " + (author == null ? "(unknown)" : author.getText()));

        // Fetch the background
        XMLNode bgNode = root.findChild("background");
        Background bg = null;
        if (bgNode != null) {
            bg = Background.create(bgNode, mGameState);
        }
        mGameState.setBackground(bg);

        return true;
    }

}
