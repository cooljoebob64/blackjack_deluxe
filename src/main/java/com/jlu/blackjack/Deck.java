package com.jlu.blackjack;

import java.util.Stack;

public class Deck {

    private Stack<Card> currentShoe;
    private Rules gameRules;

    public Deck(Rules gameRules){
        this.gameRules = gameRules;
        currentShoe = new Stack<>();
        reloadShoe();
    }

    public int getCardsInShoe(){
        return currentShoe.size();
    }
    public Card getPeekCard(){
        return currentShoe.peek();
    }
    public void newRules(Rules newRules){
        this.gameRules = newRules;
    }

    public void reloadShoe(){}
    public Card dealCard(){
        return Card.C2;
    }
    public void shuffle(){}
    public void burn(){}

}
