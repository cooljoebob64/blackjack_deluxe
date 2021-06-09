package com.jlu.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    Rules testRules;
    Deck testDeck;

    @BeforeEach
    void setUp(){
        testRules = new Rules();
        testDeck = new Deck(testRules);
    }

    @Test
    void reloadShoe() {
        int testDecksCount = 3;
        boolean testClearShoeOnLoad = true;
        int expected = testDecksCount * 52;
        int actual = 0;

        testRules.setDecksToLoad(testDecksCount);
        testRules.setClearShoeOnLoad(testClearShoeOnLoad);
        testDeck.newRules(testRules);
        testDeck.reloadShoe();
        actual = testDeck.getCardsInShoe();

        assertEquals(expected, actual);
    }

    @Test
    void dealCard() {
        Card expectedCard = testDeck.getPeekCard();
        Card actualCard = testDeck.dealCard();

        assertEquals(expectedCard, actualCard);
    }

//    @Test
//    void shuffle() {
//        Card expectedCard = testDeck.getPeekCard();
//        testDeck.shuffle();
//        Card actualCard = testDeck.getPeekCard();
//
//        // Could fail on rare chance of same card after shuffle
//        assertNotEquals(expectedCard, actualCard);
//    }

//    @Test
//    void burn() {
//        Card expectedCard = testDeck.getPeekCard();
//        testDeck.burn();
//        Card actualCard = testDeck.getPeekCard();
//
//        // Could fail on rare chance of same card after burn
//        assertNotEquals(expectedCard, actualCard);
//    }
}