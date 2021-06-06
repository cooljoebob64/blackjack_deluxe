package com.jlu.blackjack;

import java.util.List;

public class Hand {

    List<Card> currentCards;
    boolean mainHand;

    Game currentGame;
    public Hand(Game currentGame){
        this.currentGame = currentGame;
    }

    void addCard(Card newCard){}
    void possibleValues(){}
    void highestNonBust(){}
    void isMainHand(){}
    void clearHand(){}
    void getActions(){}

}
