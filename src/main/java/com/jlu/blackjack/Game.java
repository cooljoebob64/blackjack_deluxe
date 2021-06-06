package com.jlu.blackjack;

import java.util.List;

public class Game {

    private enum State{
        PRE_DEAL,
        DEALER_UP,
        PLAYER_ACTIONS,
        DEALER_REVEAL,
        PAYOUT,
        CLEANUP
    }

    private List<Participant> allParticipants;
    private Dealer dealer;
    private List<Player> players;
    private boolean keepPlaying;

    private Rules currentRules;

    private State currentState;

    /**
     * Empty constructor, starts game with default rules
     */
    public Game(){
        this(new Rules());
    }

    /**
     * Constructor to start the game with specified rules set
     * @param gameRules A rules object that specifies how the game will be played
     */
    public Game(Rules gameRules){
        this.currentRules = gameRules;
        this.currentState = State.PRE_DEAL;
        this.keepPlaying = true;

        mainLoop();
    }

    private void mainLoop(){
        do{
            System.out.println("Main game loop.");
            keepPlaying=false;
        } while(keepPlaying);
    }

    private void preDeal(){}
    private void dealerUp(){}
    private void playerActions(){}
    private void dealerReveal(){}
    private void payout(){}
    private void cleanup(){}


//    private void mainLoop() {
//        switch (currentState){
//            case PRE_DEAL -> {}
//            case DEALER_UP -> {}
//            case PLAYER_ACTIONS -> {}
//            case DEALER_REVEAL -> {}
//            case PAYOUT -> {}
//            case CLEANUP -> {}
//            default -> {}
//        }
//    }
//

}
