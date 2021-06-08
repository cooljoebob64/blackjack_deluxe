package com.jlu.blackjack;

public class Player implements Participant {

    private String playerName;
    private int bank;

    Game currentGame;
    public Player(Game currentGame){
        this.currentGame = currentGame;
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
}
