package com.jlu.blackjack;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class Hand {



    public Hand(Rules currentRules){
        this.currentRules = currentRules;
    }

    public List<Card> getCurrentCards() {
        return currentCards;
    }

    public void setCurrentCards(List<Card> currentCards) {
        this.currentCards = currentCards;
    }




    public Rules getCurrentRules() {
        return currentRules;
    }

    public void setCurrentRules(Rules currentRules) {
        this.currentRules = currentRules;
    }

    private Rules currentRules;
    private List<Card> currentCards;
    private boolean mainHand;

    void addCard(Card newCard){}
    List<Integer> possibleValues(){
        return new ArrayList<>();
    }
    int highestNonBust(){
        return 0;
    }
    void isMainHand(){}
    public void setMainHand(boolean mainHand) {
        this.mainHand = mainHand;
    }
    void clearHand(){}
    List<Action> getActions(){
        return new ArrayList<>();
    }
    void addAction(Action newAction){

    }

    private Participant owner;
    public Participant getOwner() {
        return owner;
    }
    public void setOwner(Participant owner) {
        this.owner = owner;
    }

}
