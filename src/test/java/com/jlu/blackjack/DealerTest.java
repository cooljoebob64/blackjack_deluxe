package com.jlu.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    Rules testRules;
    Hand testHand;
    Dealer testDealer;

    @BeforeEach
    void setUp(){
        testRules = new Rules();
        testHand = new Hand(testRules);
        testDealer = new Dealer(testRules);

        testHand.setOwner(testDealer);
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
//        actualInactive = testDealer.isStillActive();
//
//        assertEquals(expectedInactive, actualInactive);
//    }

    @Test
    void isStillActiveNotBusted(){
        boolean expectedActive = true;
        boolean actualActive;

        testHand.clearHand();
        testHand.addCard(Card.D2);
        actualActive = testDealer.isStillActive();

        assertEquals(expectedActive, actualActive);
    }

//    @Test
//    void getCurrentHand() {
//        Hand expectedHand = testHand;
//        Hand actualHand;
//
//
//        actualHand = testDealer.getCurrentHand();
//
//        assertEquals(expectedHand, actualHand);
//    }


}