package com.jlu.blackjack;

public class Dealer implements Participant{

    Game currentGame;
    public Dealer(Game currentGame){
        this.currentGame = currentGame;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isStillActive() {
        return false;
    }

    @Override
    public Hand getCurrentHand() {
        return null;
    }

    @Override
    public Hand getSplitHand() {
        return null;
    }
}
