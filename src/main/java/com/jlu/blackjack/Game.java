package com.jlu.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Game {

    private enum State{
        PRE_DEAL,
        DEALER_UP,
        PLAYER_ACTIONS,
        DEALER_REVEAL,
        PAYOUT,
        CLEANUP
    }

    private final Scanner myScanner;
//    private List<Participant> allParticipants;
    private List<Player> players;
    private boolean keepPlaying;
    private Player firstPlayer;
    private Dealer dealer;
    private final int STARTING_BANK = 50;
    private Deck deck;
    private Hand activeHand;
    private Hand dealerHand;

    private final Rules currentRules;
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
        myScanner = new Scanner(System.in);
        players = new ArrayList<>();

        this.currentRules = gameRules;
        this.currentState = State.PRE_DEAL;
        this.keepPlaying = true;

        enterToContinue();
        drawLine();
        System.out.println("Game Rules:");
        System.out.println(currentRules.getFormattedRules());
        enterToContinue();

        newGameSetup();
        mainLoop();
    }

    private void newGameSetup() {
        // Playing a single player game with one dealer
        firstPlayer = new Player(currentRules);
        firstPlayer.setName("Player 1");
        players.add(firstPlayer);
        firstPlayer.setBank(STARTING_BANK);
        dealer = new Dealer(currentRules);
        deck = new Deck(currentRules);


        dealerHand = dealer.getCurrentHand();


    }

    private void drawLine(){
        // Provides a re-usable way to draw a graphical line in the console
        System.out.println("================================================");
    }

    private void clearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    private void enterToContinue(){
        // Prompts the user to press enter before continuing
        System.out.println("Press Enter to continue...");
        myScanner.nextLine();
    }

    private void mainLoop(){
//        System.out.println("Main game loop.");
//        enterToContinue();

        do{
            switch (currentState) {
                case PRE_DEAL -> {
                    preDeal();
                }
                case PLAYER_ACTIONS -> {
                    playerActions();
                }
                case DEALER_UP -> {
                    dealerUp();
                }
                case DEALER_REVEAL -> {
                    dealerReveal();
                }
                case PAYOUT -> {
                    payout();
                }
                case CLEANUP -> {
                    cleanup();
                }
                default -> System.out.println("Error! State not loaded.");
            }
        } while(keepPlaying);
    }

    private void preDeal(){
        clearScreen();
        drawLine();
        System.out.println("Starting new round!");
        for(Player player:players){
            player.setActive(true);
            player.clearHands();
            player.newHand(new Hand(currentRules));
            player.getCurrentHand().addCard((deck.dealCard()));
            player.getCurrentHand().addCard((deck.dealCard()));

            int betAmount = 0;
            int minBet = (int)currentRules.getMinBet();
            boolean stillBetting = true;
            do {
                if(player.getBank()<minBet){
                    System.out.println("Your current bank: " + player.getBank());
                    System.out.println("Hey, you are all outta money!");
                    System.out.println("That's alright, we're really nice here.");
                    System.out.println("We'll put you back in the game.");
                    player.setBank((int)currentRules.getMinBet());
                }
                System.out.println("Your current bank: " + player.getBank());
                System.out.println("How much would you like to bet on this hand?");
                System.out.println("(Minimum bet: " + minBet + ")");
                System.out.print("Bet Amount: ");
                betAmount = myScanner.nextInt();
                if(betAmount<minBet){
                    System.out.println("Bet too low or invalid! Try again.");
                } else if (betAmount>player.getBank()){
                    System.out.println("Hey, you don't have that much! Try again.");
                }else {
                    player.bet(player.getCurrentHand(), betAmount);
                    stillBetting = false;
                }
            } while(stillBetting);
        }

        dealerHand.clearHand();
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());

        firstPlayer.getCurrentHand().calculateActions();
        activeHand = firstPlayer.getCurrentHand();
        printBoard();
        this.currentState = State.PLAYER_ACTIONS;
    }
    private void playerActions(){
        boolean validChoice = false;
        do {
            printBoard();
            System.out.println("What would you like to do?");
            String response = myScanner.nextLine().toUpperCase(Locale.ROOT);
            switch (response) {
                case "H" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.HIT)) {
                        playerActionHit();
                        validChoice = true;
                    }
                }
                case "S" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.STAND)) {
                        validChoice = true;

                        playerActionStand();
                    }
                }
                case "T" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.SPLIT)) {
                        playerActionSplit();
                        validChoice = true;
                    }
                }
                case "D" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.DOUBLE)) {
                        playerActionDouble();
                        validChoice = true;
                    }
                }
                case "R" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.SURRENDER)) {
                        playerActionSurrender();
                        validChoice = true;
                    }
                }
                case "Q" -> {
                    playerActionQuit();
                    validChoice = true;
                }
                default -> {
                    System.out.println("Invalid command!");
                    validChoice = false;
                }
            }
//            enterToContinue();
        } while(!validChoice);
    }

    private void playerActionHit() {
        System.out.println("You hit!");
        Card newPlayerCard = deck.dealCard();
        activeHand.addCard(newPlayerCard);
        System.out.println("You drew: " + newPlayerCard.cardName());
        if(activeHand.highestNonBust()>21){
            System.out.println("Busted!");
            firstPlayer.setActive(false);
        } else if (activeHand.highestNonBust()==21){
            System.out.println("21, nice!");
        }
    }
    private void playerActionStand() {
        System.out.println("You stand!");
        System.out.print("Your current hand: " + activeHand.cardsToString());
        System.out.println("Hand value: " + activeHand.highestNonBust());
        if(activeHand.isMainHand()&&firstPlayer.getSplitHand()!=null){
            activeHand = firstPlayer.getSplitHand();
        } else
            currentState = State.DEALER_UP;
    }
    private void playerActionSplit() {
        System.out.println("You split!");
        firstPlayer.newHand(new Hand(currentRules));
        firstPlayer.getSplitHand().addCard(activeHand.getCurrentCards().get(1));
        firstPlayer.bet(firstPlayer.getSplitHand(), activeHand.getBetAmount());
        activeHand.getCurrentCards().remove(1);
    }
    private void playerActionDouble() {
        System.out.println("You double down!");
        if(firstPlayer.getBank()<activeHand.getBetAmount()){
            System.out.println("You're going into the negative here... But we'll allow it.");
        }
        firstPlayer.bet(activeHand, activeHand.getBetAmount());
        playerActionHit();
        playerActionStand();
        firstPlayer.setActive(false);
    }
    private void playerActionSurrender() {
        System.out.println("You surrender! Get back half your bet.");
        int surrenderReward = (int)Math.floor(activeHand.getBetAmount()*.5);
        System.out.println("You get: $" + surrenderReward + " back.");
        firstPlayer.pay(surrenderReward);
        firstPlayer.setActive(false);
    }
    private void playerActionQuit() {
        System.out.println("Quitting!");
        keepPlaying = false;
        currentState = State.CLEANUP;
    }

    private void dealerUp(){
        Hand dealerHand = dealer.getCurrentHand();
        boolean keepDealing = true;
        
        do {
            int dealerValue = dealerHand.highestNonBust();
            if (dealerValue > 21) {
                dealerBust();
                keepDealing = false;
            } else if(dealerValue>17){
                dealerStand();
                keepDealing = false;
            } else {
                if((dealerValue==17)
                        &&
                        (dealerHand.possibleValuesNonBust().size()>1)){
                    if(currentRules.isDealerHitsSoft17()){
                        dealerHit();
                    } else {
                        dealerStand();
                        keepDealing = false;
                    }
                } else {
                    dealerHit();
                }
            }
        } while(keepDealing);
        this.currentState = State.PAYOUT;
    }

    private void dealerStand() {
        System.out.println("Dealer stands!");
    }

    private void dealerHit(){
        System.out.println("Dealer hits!");
        Card newDealerCard = deck.dealCard();
        System.out.println("Dealer draws: " + newDealerCard.cardName());
        dealer.getCurrentHand().addCard(newDealerCard);
    }
    private void dealerBust() {
        System.out.println("Dealer is busted!");
    }

    private void dealerReveal(){
//        printBoard();
//        enterToContinue();
        this.currentState = State.DEALER_UP;
    }
    private void payout(){
        int dealerValue = dealer.getCurrentHand().highestNonBust();
        System.out.println("Dealer hand value: " + dealerValue);
        for(Player player:players){
//            if(player.isStillActive()){

                System.out.println("Calculating hands for " + player.getName());
                for(Hand hand : player.getAllHands()){
                    System.out.println("This hand: " + hand.cardsToString());
                    if(!hand.isMainHand()){
                        System.out.println("(this is your split hand)");
                    }
                    System.out.println("Highest value: " + hand.highestNonBust());
                    int highValue = hand.highestNonBust();
                    int payoutAmount;

                    if(highValue==21&&hand.getCurrentCards().size()==2){ // Blackjack!
                        payoutAmount = (int)Math.ceil(hand.getBetAmount() * currentRules.getBlackjackPayout())+hand.getBetAmount();
                        System.out.println("Blackjack!");
                        System.out.println("Bet: " + hand.getBetAmount());
                        System.out.println("Blackjack payout: " + currentRules.getBlackjackPayout());
                        System.out.println("You win: " + payoutAmount);
                    } else if(highValue>21){ // Busted hand
                        System.out.println("This hand busted!");
                        System.out.println("No payout!");
                        payoutAmount = 0;
                    } else if(highValue>dealerValue||dealerValue>21){
                        System.out.println("Your " + highValue + " beats the dealer!");
                        payoutAmount = 2* hand.getBetAmount();
                    } else if(highValue==dealerValue){
                        System.out.println("You tied the dealer. Push!");
                        if(currentRules.getPushRule()==0){ // No action
                            System.out.println("No action. Bet refunded!");
                            payoutAmount = hand.getBetAmount();
                        } else if(currentRules.getPushRule()==1){ // Player win
                            System.out.println("Player wins the push!");
                            payoutAmount = hand.getBetAmount()*2;
                        } else { // Player loss
                            System.out.println("Player loses the push!");
                            payoutAmount = 0;
                        }
                    } else {
                        System.out.println("Your " + highValue + " didn't beat the dealer. Sorry!");
                        payoutAmount = 0;
                    }
                    System.out.println("You receive: " + payoutAmount);
                    player.pay(payoutAmount);
                    System.out.println("Your new bank: " + player.getBank());
                }
//            }
        }
        enterToContinue();
        this.currentState = State.CLEANUP;
    }
    private void cleanup(){
        dealerHand.clearHand();
        for(Player player:players){
            player.clearHands();
            player.newHand(new Hand(currentRules));
            player.setActive(true);
        }
        this.currentState = State.PRE_DEAL;
    }

    private void printBoard(){

        clearScreen();
        drawLine();

        // Print dealer hand
        System.out.println(enumerateDealerCards());
        // Print player bank
        System.out.println("Current bank: " + firstPlayer.getBank());
        // Print player hand information
        System.out.print("Your main hand: " + firstPlayer.getCurrentHand().cardsToString());
        System.out.println("Main hand bet: " + firstPlayer.getAllHands().get(0).getBetAmount());
        System.out.println("Main hand possible Values: " + firstPlayer.getCurrentHand().possibleValues());
        String playerSplitHand = "";
        if(firstPlayer.getSplitHand()!= null){
            playerSplitHand = "Your split hand: " + firstPlayer.getSplitHand().cardsToString();
            System.out.println("Split hand bet: " + firstPlayer.getAllHands().get(1).getBetAmount());
            System.out.println("Split hand possible Values: " + firstPlayer.getSplitHand().possibleValues());
        } else
            playerSplitHand = "You do not have a split hand.\n";
        System.out.println(playerSplitHand);

        if(currentState==State.PLAYER_ACTIONS){
            firstPlayer.getCurrentHand().calculateActions();
            if(dealerHand.getCurrentCards().get(0).handValue()==11&&firstPlayer.getCurrentHand().getCurrentCards().size()==2){
                firstPlayer.getCurrentHand().addAction(Action.SURRENDER);
            }
            printPossibleActions();
        }
    }

    private void printPossibleActions() {
        System.out.println("Pick an action:");
        int skipLines = 0;
        if(firstPlayer.getCurrentHand().getActions().contains(Action.STAND)){
            System.out.println(Action.STAND.commandLetter() +": Stand");
        } else skipLines++;
        if(firstPlayer.getCurrentHand().getActions().contains(Action.HIT)){
            System.out.println(Action.HIT.commandLetter() +": Hit");
        } else skipLines++;
        if(firstPlayer.getCurrentHand().getActions().contains(Action.DOUBLE)){
            System.out.println(Action.DOUBLE.commandLetter() +": Double Down");
        } else skipLines++;
        if(firstPlayer.getCurrentHand().getActions().contains(Action.SPLIT)){
            System.out.println(Action.SPLIT.commandLetter() +": Split");
        } else skipLines++;
        if(firstPlayer.getCurrentHand().getActions().contains(Action.SURRENDER)){
            System.out.println(Action.SURRENDER.commandLetter() +": Surrender");
        } else skipLines++;
        System.out.println(Action.QUIT.commandLetter() +": Quit");
        for(int i=0; i<skipLines;i++){
            System.out.println();
        }
    }

    private String enumerateDealerCards() {
        List<Card> dealerCards = dealer.getCurrentHand().getCurrentCards();
        String dealerCardOne = dealerCards.get(0).cardName();
        String dealerCardTwo = "";
        StringBuilder dealerExtraCards = new StringBuilder();
        for(int i=2;i<dealerCards.size();i++){
            dealerExtraCards.append(" and ").append(dealerCards.get(i).cardName());
        }
        if(currentState == State.PRE_DEAL||currentState == State.PLAYER_ACTIONS){
            dealerCardTwo = " and one face down\nDealer showing value: " + dealerCards.get(0).handValue();

        } else {
            dealerCardTwo = " and " + dealerCards.get(1).cardName();
            dealerExtraCards.append("\nDealer showing value: ");
            dealerExtraCards.append(dealer.getCurrentHand().highestNonBust());
        }

        return "Dealer's Cards: " + dealerCardOne + dealerCardTwo + dealerExtraCards + ".";
    }

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
