package com.sss.ball.logic;

import java.util.Random;
import java.util.Vector;

import com.sss.ball.Bonus;
import com.sss.ball.Brick;
import com.sss.ball.GameState;
import com.sss.ball.Logic;
import com.sss.ball.xml.XMLNode;

public class BonusLogic extends Logic {

    static final Random rnd = new Random();

    private Vector<Rule> mRules = new Vector<Rule>();

    @Override
    protected void init(XMLNode xml, GameState gameState) {
        for (int i = 0; i < xml.getChildCount(); i++) {
            XMLNode child = xml.getChild(i);
            if ("rule".equals(child.getName())) {
                Rule rule = new Rule(child);
                mRules.add(rule);
            }
        }

    }

    @Override
    public void onBrickHit(Brick brick, boolean removed) {
        if (!removed) return;

        // Select the bonus type
        int btype = -1;
        float luck = rnd.nextFloat() * 100;
        for (Rule rule : mRules) {
            if (luck < rule.mChance) {
                // we are using this rule
                btype = rule.mType[rnd.nextInt(rule.mType.length)];
                break;
            }

        }

        // add a bonus as well
        if (btype >= 0) {
            Bonus b = new Bonus(getGameState());
            b.setBonusType(btype);
            b.centerOn(brick);
            getGameState().addBonus(b);
        }
    }

    static class Rule {
        public float mChance;
        public int mType[];

        public Rule(XMLNode child) {
            mChance = Float.parseFloat(child.getAttr("chance"));
            String types[] = child.getAttr("types").split(",");
            mType = new int[types.length];
            for (int i = 0; i < mType.length; i++) {
                mType[i] = Bonus.lookupType(types[i]);
            }
        }
    }

}
