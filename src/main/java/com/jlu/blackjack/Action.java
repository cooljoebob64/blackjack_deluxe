package com.jlu.blackjack;

public enum Action {
    HIT("H"),
    STAND("S"),
    SPLIT("T"),
    DOUBLE("D"),
    SURRENDER("R"),
    INSURANCE("I"),
    EVEN_MONEY("E"),
    GET_DECK_STATS("G"),
    QUIT("Q");

    private final String commandLetter;
    Action(String commandLetter){
        this.commandLetter=commandLetter;
    }
    String commandLetter(){
        return this.commandLetter;
    }
}
