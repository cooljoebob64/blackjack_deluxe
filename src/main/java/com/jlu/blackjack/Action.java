package com.jlu.blackjack;

public enum Action {
    HIT("H"),
    STAND("S"),
    SPLIT("T"),
    DOUBLE("D"),
    SURRENDER("R"),
    QUIT("Q"),
    GET_DECK_STATS("G");

    private final String commandLetter;
    Action(String commandLetter){
        this.commandLetter=commandLetter;
    }
    String commandLetter(){
        return this.commandLetter;
    }
}
