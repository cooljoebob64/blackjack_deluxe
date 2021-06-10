package com.jlu.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player implements Participant {

    /**
     * Constructor takes a Rules object
     * @param currentRules The Rules object which describes which game rules to use
     */
    public Player(Rules currentRules){
        this.currentRules = currentRules;
        this.bank = 0;
        this.setActive(true);
        this.hands = new ArrayList<>();
    }

    // Overridden methods from Participant
    @Override
    public String getName() {
        return playerName;
    }
    @Override
    public boolean isStillActive() {
        return this.active;
    }
    public void setActive(boolean isNowActive){
        this.active = isNowActive;
    }
    @Override
    public Hand getCurrentHand() {
        if(hands.isEmpty()){
            newHand(new Hand(currentRules));
        }
        return hands.get(0);
    }

    // Vanilla getters and setters
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
    public void setName(String newName){
        this.playerName = newName;
    }
    public Hand getSplitHand() {
        if(hands.size()>1){
            return hands.get(1);
        } else
            return null;
    }
    public void newHand(Hand newHand){
        hands.add(newHand);
    }
    public void clearHands(){
        hands.clear();
    }
    public void bet(Hand bettingHand, int betAmount){
        this.bank -= betAmount;
        bettingHand.addBet(betAmount);
    }
    public void pay(int payAmount){
        this.bank += payAmount;
    }

    // Private fields
    private String playerName;
    private Rules currentRules;
    private int bank;
    private List<Hand> hands;
    private boolean active;
}
