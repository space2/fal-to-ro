package com.sss.ball;

public class Main {

    public static void main(String args[]) {
        System.out.println("Startup.");
        GameController g = new GameController();
        g.parseArgs(args);
        g.run();
    }
}
