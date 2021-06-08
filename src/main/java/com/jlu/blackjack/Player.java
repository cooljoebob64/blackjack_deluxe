package com.jlu.blackjack;

public class Player implements Participant {

    private String playerName;
    private int bank;

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }


    public Rules getCurrentRules() {
        return currentRules;
    }

    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }

    Rules currentRules;
    public Player(Rules currentRules){
        this.currentRules = currentRules;
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Override
    public boolean isStillActive() {
        return false;
    }

    @Override
    public Hand getCurrentHand() {
        return null;
    }

    public Hand getSplitHand() {
        return null;
    }

    public void bet(Hand bettingHand){}
    public void pay(){}
}
