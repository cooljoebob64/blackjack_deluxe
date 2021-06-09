package com.jlu.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    public Hand(Rules currentRules){
        this.currentRules = currentRules;
    }
    public int getBetAmount() {
        return betAmount;
    }
    public List<Card> getCurrentCards() {
        return currentCards;
    }
    public void addBet(int addAmount){}
    public void clearBet(){}
    public List<Integer> possibleValues(){
        return new ArrayList<>();
    }
    public void setCurrentCards(List<Card> currentCards) {
        this.currentCards = currentCards;
    }
    public void setMainHand(boolean mainHand) {
        this.mainHand = mainHand;
    }
    public Rules getCurrentRules() {
        return currentRules;
    }
    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }
    public void addCard(Card newCard){}
    public int highestNonBust(){
        return 0;
    }
    public void isMainHand(){}
    public void clearHand(){}
    public List<Action> getActions(){
        return new ArrayList<>();
    }
    public void addAction(Action newAction){}
    public Participant getOwner() {
        return owner;
    }
    public void setOwner(Participant owner) {
        this.owner = owner;
    }

    private int betAmount;
    private Rules currentRules;
    private List<Card> currentCards;
    private boolean mainHand;
    private Participant owner;
}
