package com.jlu.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    Rules testRules;
    Hand testHand;

    @BeforeEach
    void setUp(){
        testRules = new Rules();
        testHand = new Hand(testRules);
    }

    @Test
    void addCard() {
        int expectedSize = 3;
        int actual;

        int beforeSize = testHand.getCurrentCards().size();
        testHand.addCard(Card.D2);
        testHand.addCard(Card.D2);
        testHand.addCard(Card.D2);
        int afterSize = testHand.getCurrentCards().size();
        actual = afterSize - beforeSize;

        assertEquals(expectedSize, actual);
    }

    @Test
    void possibleValuesEmpty() {
        List<Integer> actualEmptyValue;

        testHand.clearHand();
        actualEmptyValue = testHand.possibleValues();

        assertEquals(0, actualEmptyValue.size());

    }

    @Test
    void possibleValuesTwoCards() {
        int expectedSize = 1;
        int expectedValue = 5;
        List<Integer> actualTwoCardValue;

        testHand.clearHand();
        testHand.addCard(Card.D2);
        testHand.addCard(Card.D3);
        actualTwoCardValue = testHand.possibleValues();

        assertEquals(expectedSize, actualTwoCardValue.size());
        assertEquals(expectedValue, actualTwoCardValue.get(0));
    }

    @Test
    void possibleValuesAce() {
        int expectedSize = 2;
        int expectedFirstValue = 3;
        int expectedSecondValue = 13;
        List<Integer> actualAceValue;

        testHand.clearHand();
        testHand.addCard(Card.D2);
        testHand.addCard(Card.DA);
        actualAceValue = testHand.possibleValues();

        assertEquals(expectedSize, actualAceValue.size());
        assertEquals(expectedFirstValue, actualAceValue.get(0));
        assertEquals(expectedSecondValue, actualAceValue.get(1));
    }

    @Test
    void highestNonBust() {
        int expectedHighestValue = 19;
        int actualHighestValue;

        testHand.clearHand();
        testHand.addCard(Card.D3);
        testHand.addCard(Card.D5);
        testHand.addCard(Card.DK);
        testHand.addCard(Card.DA);
        actualHighestValue = testHand.highestNonBust();

        assertEquals(expectedHighestValue, actualHighestValue);
    }

    @Test
    void clearHand() {
        int expectedSize = 0;
        int actualSize;

        testHand.clearHand();
        testHand.addCard(Card.D2);
        testHand.clearHand();
        actualSize = testHand.getCurrentCards().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void getActions() {
        List<Action> expectedActions = new ArrayList<>();
        expectedActions.add(Action.HIT);
        expectedActions.add(Action.STAND);
        expectedActions.add(Action.DOUBLE);

        List<Action> actualActions = testHand.getActions();

        assertEquals(expectedActions, actualActions);
    }

    @Test
    void addBet(){
        int expectedBetAmount = 20;
        int actualBetAmount;

        testHand.clearBet();
        testHand.addBet(20);
        actualBetAmount = testHand.getBetAmount();

        assertEquals(expectedBetAmount, actualBetAmount);
    }
}