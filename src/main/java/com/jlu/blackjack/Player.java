package com.jlu.blackjack;

public class Player implements Participant {

    private String playerName;

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

    @Override
    public Hand getSplitHand() {
        return null;
    }
}
