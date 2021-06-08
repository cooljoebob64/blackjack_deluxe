package com.jlu.blackjack;

public class Dealer implements Participant{


    Rules currentRules;
    public Dealer(Rules currentRules){
        this.currentRules = currentRules;
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


    public Rules getCurrentRules() {
        return currentRules;
    }

    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }
}
