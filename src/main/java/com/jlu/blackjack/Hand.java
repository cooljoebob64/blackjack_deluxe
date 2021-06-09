package com.jlu.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    public Hand(Rules gameRules){
        this.gameRules = gameRules;
        this.currentCards = new ArrayList<>();
        this.currentPossibleActions = new ArrayList<>();
    }

    /**
     * Adds an amount the hand's current bet
     * @param addAmount the amount to be added
     */
    public void addBet(int addAmount){
        this.betAmount += addAmount;
    }

    /**
     * Sets the bet on a hand to zero
     */
    public void clearBet(){
        this.betAmount = 0;
    }

    /**
     * Gets all possible values for a hand, in descending order
     * @return a List of all possible values
     */
    public List<Integer> possibleValues(){
        List<Integer> values = new ArrayList<>();
        int aces = 0;
        // Find the value of all cards at max value, and count the aces
        int simpleAdd = 0;
        for(Card card:currentCards){
            if(card.cardName().contains("Ace")) aces++;
            simpleAdd += card.handValue();
        }
        values.add(simpleAdd);

        // Add the value of each soft Ace to the list of possibilities
        for (int i=1;i<=aces;i++){
            values.add(simpleAdd-(i*10));
        }
        return values;
    }

    public List<Card> getCurrentCards() {
        return currentCards;
    }
    public void setCurrentCards(List<Card> currentCards) {
        this.currentCards = currentCards;
    }
    public void addCard(Card newCard){
        currentCards.add(newCard);
    }
    public List<Integer> possibleValuesNonBust(){
        List<Integer> valuesNonBust = possibleValues();
        valuesNonBust.removeIf(val -> val > 21);
        return valuesNonBust;
    }
    public int highestNonBust(){
        return possibleValuesNonBust().get(0);
    }
    public boolean isMainHand(){
        return this.mainHand;
    }
    public void clearHand(){
        currentCards.clear();
    }

    private List<Action> currentPossibleActions;
    public List<Action> getActions(){
        return currentPossibleActions;
    }
    public void clearActions(){
        currentPossibleActions.clear();
    }
    /**
     * Calculates the actions that are possible given the current cards in hand
     */
    public void calculateActions(){
        List<Action> possibleActions = new ArrayList<>();
        // HIT
        if(highestNonBust()<21){
            possibleActions.add(Action.HIT);
        }

        // DOUBLE
        // SPLIT
        if(currentCards.size()==2
//                &&gameRules.isDoubleAllowed
        ){
            possibleActions.add(Action.DOUBLE);
            if(currentCards.get(0).handValue()==currentCards.get(1).handValue()){
                possibleActions.add(Action.SPLIT);}
        }
        // STAND
        possibleActions.add(Action.STAND);
        currentPossibleActions.addAll(possibleActions);
    }

    /**
     * Adds an arbitrary action to the list, such as Surrender
     * @param newAction Action to be added to the list
     */
    public void addAction(Action newAction){}
    public int getBetAmount() {
        return betAmount;
    }
    public void setMainHand(boolean mainHand) {
        this.mainHand = mainHand;
    }
    public Rules getGameRules() {
        return gameRules;
    }
    public void setGameRules(Rules gameRules) {
        this.gameRules = gameRules;
    }
    public Participant getOwner() {
        return owner;
    }
    public void setOwner(Participant owner) {
        this.owner = owner;
    }

    private int betAmount;
    private Rules gameRules;
    private List<Card> currentCards;
    private boolean mainHand;
    private Participant owner;
}
