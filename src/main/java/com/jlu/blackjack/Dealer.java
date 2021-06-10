package com.jlu.blackjack;

public class Dealer implements Participant{

    private final Hand dealerHand;
    private String dealerName;

    Rules currentRules;
    public Dealer(Rules currentRules){
        this.currentRules = currentRules;
        this.dealerHand = new Hand(currentRules);
    }

    @Override
    public String getName() {
        return this.dealerName;
    }
    public void setName(String newName){
        this.dealerName = newName;
    }

    @Override
    public boolean isStillActive() {
        return true;
    }

    @Override
    public Hand getCurrentHand() {
        return this.dealerHand;
    }


    public Rules getCurrentRules() {
        return currentRules;
    }
    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }
}
