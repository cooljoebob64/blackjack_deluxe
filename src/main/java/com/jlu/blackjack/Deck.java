package com.jlu.blackjack;

import java.util.Stack;

public class Deck {

    Game currentGame;
    public Deck(Game currentGame){
        this.currentGame = currentGame;
    }
    private Stack<Card> currentShoe;

    public void reloadShoe(){}
    public void dealCard(){}
    public void shuffle(){}
    public void burn(){}

    public int getCardsInShoe(){
        return currentShoe.size();
    }
    public Card getPeekCard(){
        return currentShoe.peek();
    }
}
