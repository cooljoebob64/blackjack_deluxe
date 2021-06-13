package com.jlu.blackjack;

import java.util.Random;
import java.util.Stack;

public class Deck {


    private final Stack<Card> currentShoe;
    private Rules gameRules;

    /**
     * Constructor takes in a Rules object, and loads a new shoe
     * @param gameRules The rules object for the deck to use
     */
    public Deck(Rules gameRules){
        this.gameRules = gameRules;
        currentShoe = new Stack<>();
        reloadShoe();
    }

    /**
     * Adds decks into the Shoe, optionally clearing beforehand according to game rules
     * Loads the number of decks specified by the game rules
     */
    public void reloadShoe(){
        // If the rules require it, clear the shoe before reloading
        if(gameRules.isClearShoeOnLoad()){
            currentShoe.clear();
            System.out.println("Clearing shoe...");
        }

        // Determine the number of decks that must be loaded at once
        int decksToLoad = gameRules.getDecksToLoad();
        System.out.println("Reloading shoe with " + decksToLoad + " decks.");

        // For each deck that must be loaded, make a shuffled deck and add each card to the shoe
        for(int i=0; i<decksToLoad; i++){
            Card[] deckToLoad = getShuffledDeck();
            for(Card card : deckToLoad){
                currentShoe.push(card);
            }
        }
    }

    /**
     * Gets the number of cards that are currently in the shoe
     * @return An integer representing the number of cards that are in the shoe
     */
    public int getCardsInShoe(){
        return currentShoe.size();
    }

    /**
     * Shows the top card of the deck, but does not remove it
     * @return A Card object from the top of the deck
     */
    public Card getPeekCard(){
        return currentShoe.peek();
    }

    /**
     * Changes the rules object that the deck will use
     * @param newRules The new rules object for the deck to use
     */
    public void newRules(Rules newRules){
        this.gameRules = newRules;
    }
    /**
     * Deals the next card off the top of the deck.
     * @return The next Card on top of the shoe
     */
    public Card dealCard(){
        Card dealCard = currentShoe.pop();
        int initialSize = gameRules.getDecksToLoad()*52;
        if(currentShoe.size()< initialSize-(gameRules.getPenetration()*(initialSize))){
            System.out.println("Reloading shoe!");
            reloadShoe();
        }
        return dealCard;
    }

//    /**
//     * Removes the top card from the shoe
//     * @deprecated Functionality is identical to dealCard unless burn rules are altered
//     */
//    public Card burn(){
//        return currentShoe.pop();
//    }

    private Card[] shuffleDeck(Card[] deckToShuffle){
        Random rand = new Random();
        Card[] tmpDeck = deckToShuffle.clone();
        int deckSize = tmpDeck.length;
        for (int i=0;i<deckSize;i++) {
            int index = rand.nextInt(deckSize - i);
            Card tmp = tmpDeck[tmpDeck.length-1-i];
            tmpDeck[deckSize - 1 -i] = tmpDeck[index];
            tmpDeck[index] = tmp;
        }
        return tmpDeck;
    }

    private Card[] getShuffledDeck(){
        Card[] newDeck = Card.values();
        newDeck = shuffleDeck(newDeck);
        return newDeck;
    }


    public String seeAllCardsInShoe() {
        StringBuilder allCards = new StringBuilder();
        for(Card card : currentShoe){
            allCards.append(card.cardName());
            if(currentShoe.indexOf(card)!=currentShoe.size()-1){
                allCards.append(", ");
            }
        }
        return allCards.toString();
    }

    public Stack<Card> getCurrentShoe() {
        return currentShoe;
    }
}
