package com.jlu.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Rules testRules;
    Hand testHand;
    Player testPlayer;

    @BeforeEach
    void setUp(){
        testRules = new Rules();
        testHand = new Hand(testRules);
        testPlayer = new Player(testRules);
        testHand.setOwner(testPlayer);
    }

//    @Test
//    void isStillActiveBusted() {
//        boolean expectedInactive = false;
//        boolean actualInactive;
//
//        testHand.clearHand();
//        testHand.addCard(Card.DK);
//        testHand.addCard(Card.DK);
//        testHand.addCard(Card.DK);
//        actualInactive = testPlayer.isStillActive();
//        // Need to ensure only hand
//
//        assertEquals(expectedInactive, actualInactive);
//    }

    @Test
    void isStillActiveNotBusted(){
        boolean expectedActive = true;
        boolean actualActive;

        testHand.clearHand();
        testHand.addCard(Card.D2);
        actualActive = testPlayer.isStillActive();

        assertEquals(expectedActive, actualActive);
    }
}