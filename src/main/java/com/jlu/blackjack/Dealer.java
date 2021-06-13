package com.jlu.blackjack;

public class Dealer implements Participant{

    private final Hand dealerHand;
    private String dealerName;
    private boolean onFirstAction;

    Rules currentRules;
    public Dealer(Rules currentRules){
        this.currentRules = currentRules;
        this.dealerHand = new Hand(currentRules);
        this.onFirstAction = true;
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
    @Override
    public boolean isOnFirstAction() {
        return onFirstAction;
    }
    @Override
    public void setOnFirstAction(boolean onFirstAction) {
        this.onFirstAction = onFirstAction;
    }


    public Rules getCurrentRules() {
        return currentRules;
    }
    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }
}
