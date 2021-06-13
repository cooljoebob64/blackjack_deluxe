package com.jlu.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Game {
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

//        enterToContinue();
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("Game Rules:");
        System.out.println(currentRules.getFormattedRules());
        utilityEnterToContinue();

        newGameSetup();
        mainLoop();
    }

    private enum State{
        PRE_DEAL,
        DEALER_UP,
        PLAYER_ACTIONS,
        DEALER_REVEAL,
        PAYOUT,
        CLEANUP
    }
    private final Scanner myScanner;
    private final List<Player> players;
    private boolean keepPlaying;
    private Player firstPlayer;
    private Dealer dealer;
    private Deck deck;
    private Hand activeHand;
    private Hand dealerHand;
    private final Rules currentRules;
    private State currentState;

    private void newGameSetup() {
        // Playing a single player game with one dealer
        firstPlayer = new Player(currentRules);
        firstPlayer.setName("Player 1");
        players.add(firstPlayer);
        firstPlayer.setBank(currentRules.getStartingBank());
        dealer = new Dealer(currentRules);
        deck = new Deck(currentRules);


        dealerHand = dealer.getCurrentHand();


    }
    private void mainLoop(){
//        System.out.println("Main game loop.");
//        enterToContinue();

        do{
            switch (currentState) {
                case PRE_DEAL -> {
                    statePreDeal();
                }
                case PLAYER_ACTIONS -> {
                    statePlayerActions();
                }
                case DEALER_UP -> {
                    stateDealerUp();
                }
                case DEALER_REVEAL -> {
                    stateDealerReveal();
                }
                case PAYOUT -> {
                    statePayout();
                }
                case CLEANUP -> {
                    stateCleanup();
                }
                default -> System.out.println("Error! State not loaded.");
            }
        } while(keepPlaying);
    }

    private void statePreDeal(){
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("Starting new round!");
        for(Player player:players){
            player.setActive(true);
            player.clearHands();
            player.newHand(new Hand(currentRules));
            player.getCurrentHand().addCard((deck.dealCard()));
            player.getCurrentHand().addCard((deck.dealCard()));
            player.setOnFirstAction(true);

            int betAmount = 0;
            int minBet = (int)currentRules.getMinBet();
            boolean stillBetting = true;
            do {
                if(player.getBank()<minBet){
                    utilityClearScreen();
                    utilityDrawLine();
                    System.out.println("Your current bank: " + player.getBank());
                    System.out.println("Hey, you are all outta money!");
                    System.out.println("That's alright, we're really nice here.");
                    System.out.println("We'll put you back in the game.");
                    player.setBank((int)currentRules.getMinBet());
                    utilityEnterToContinue();
                }
                boolean awaitingInput = true;
                do{
                    utilityClearScreen();
                    utilityDrawLine();
                    System.out.println("Your current bank: " + player.getBank());
                    System.out.println("How much would you like to bet on this hand?");
                    System.out.println("(Minimum bet: " + minBet + ")");
                    System.out.print("Bet Amount: ");
                    String response = myScanner.nextLine();
                    try{
                        betAmount = Integer.parseInt(response);
                        if(betAmount<minBet){
                            System.out.println("Bet too low or invalid! Try again.");
                        } else if (betAmount>player.getBank()){
                            System.out.println("Hey, you don't have that much! Try again.");
                        }else {
                            player.bet(player.getCurrentHand(), betAmount);
                            stillBetting = false;
                        }
                        awaitingInput = false;
                    } catch(NumberFormatException e){
                        System.out.println("That's not a valid number! Try that again...");
                        utilityEnterToContinue();
                    }
                } while(awaitingInput);
            } while(stillBetting);
        }

        dealerHand.clearHand();
        dealerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());


        activeHand = firstPlayer.getCurrentHand();
        utilityPrintBoard();
        this.currentState = State.PLAYER_ACTIONS;
    }
    private void statePlayerActions(){
        // Get the native hand actions
        activeHand.clearActions();
        activeHand.calculateActions();
        // If dealer shows an Ace and player has taken no actions, allow a Surrender
        if(dealerHand.getCurrentCards().get(0).handValue()==11
                && activeHand.getOwner().isOnFirstAction()){
            activeHand.addAction(Action.SURRENDER);
        }
        if(currentRules.isCheatMode()) {
            activeHand.addAction(Action.GET_DECK_STATS);
        }
        boolean validChoice = false;
        do {
            utilityPrintBoard();
            utilityPrintPossibleActions();
            String isSplitHand = "main";
            if(!activeHand.isMainHand()){
                isSplitHand = "split";
            }
            System.out.println("(this is your " + isSplitHand + " hand)");
            System.out.println("What would you like to do?");
            String response = myScanner.nextLine().substring(0,1).toUpperCase(Locale.ROOT);
            switch (response) {
                case "H" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.HIT)) {
                        playerActionHit();
                        activeHand.getOwner().setOnFirstAction(false);
                        validChoice = true;
                    }
                }
                case "S" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.STAND)) {
                        validChoice = true;

                        playerActionStand();
                        activeHand.getOwner().setOnFirstAction(false);
                    }
                }
                case "T" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.SPLIT)) {
                        playerActionSplit();
                        validChoice = true;
                        activeHand.getOwner().setOnFirstAction(false);
                    }
                }
                case "D" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.DOUBLE)) {
                        playerActionDouble();
                        validChoice = true;
                        activeHand.getOwner().setOnFirstAction(false);
                    }
                }
                case "R" -> {
                    if (firstPlayer.getCurrentHand().getActions().contains(Action.SURRENDER)) {
                        playerActionSurrender();
                        validChoice = true;
                        activeHand.getOwner().setOnFirstAction(false);
                    }
                }
                case "G" -> {
                    if(currentRules.isCheatMode()){
                        playerActionPrintDeckStats();
                        validChoice = true;
                    } else{
                        System.out.println("Cheat mode not enabled, nice try!");
                    }
                }
                case "Q" -> {
                    playerActionQuit();
                    validChoice = true;
                }
                default -> {
                    System.out.println("Invalid command! Try again.");
                    utilityEnterToContinue();
                    validChoice = false;
                }
            }
//            enterToContinue();
        } while(!validChoice);
    }
    private void statePayout(){
        utilityClearScreen();
        utilityDrawLine();
        int dealerValue = dealer.getCurrentHand().highestNonBust();
        System.out.println("Dealer hand value: " + dealerValue);
        for(Player player:players){
            System.out.println("Calculating hands for " + player.getName());
            for(Hand hand : player.getAllHands()){
                System.out.println("This hand: " + hand.cardsToString());
                String isMain = "main";
                if(!hand.isMainHand()){
                    isMain = "split";
                }
                System.out.println("(this is your " + isMain + " hand)");
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
                    System.out.println("House keeps your bet!");
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
        }
        utilityEnterToContinue();
        this.currentState = State.CLEANUP;
    }
    private void stateCleanup(){
        utilityClearScreen();
        utilityDrawLine();
//        System.out.println("Setting up for a new round");
        dealerHand.clearHand();
        for(Player player:players){
            player.clearHands();
            player.newHand(new Hand(currentRules));
            player.setActive(true);
        }
//        enterToContinue();
        this.currentState = State.PRE_DEAL;
    }
    private void stateDealerReveal(){
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("Dealer reveals their hand: ");
        System.out.println(dealerHand.cardsToString());
        System.out.println("Initial hand value: " + dealerHand.highestNonBust());
        System.out.println("Dealer will play now!");
        utilityEnterToContinue();
        this.currentState = State.DEALER_UP;
    }
    private void stateDealerUp(){
        utilityClearScreen();
        utilityDrawLine();
        Hand dealerHand = dealer.getCurrentHand();
        System.out.println("Dealer's starting hand: " + dealerHand.cardsToString());
        boolean keepDealing = true;
        do {
            int dealerValue = dealerHand.highestNonBust();
            if (dealerValue > 21) {
                dealerActionBust();
                keepDealing = false;
            } else if(dealerValue>17){
                dealerActionStand();
                keepDealing = false;
            } else {
                if(dealerValue==17) {
                    if((currentRules.isDealerHitsSoft17()&&dealerHand.possibleValuesNonBust().size()>1)){
                        dealerActionHit();
                    } else {
                        dealerActionStand();
                        keepDealing = false;
                    }
                } else {
                    dealerActionHit();
                }
            }
//            System.out.println("Dealer's hand: " + enumerateDealerCards() + ", highest value: " + dealerHand.highestNonBust());
            utilityEnterToContinue();
        } while(keepDealing);
        this.currentState = State.PAYOUT;
    }

    private void playerActionHit() {
        utilityClearScreen();
        utilityDrawLine();

        System.out.println("You hit!");
        Card newPlayerCard = deck.dealCard();
        activeHand.addCard(newPlayerCard);
        System.out.println("You drew: " + newPlayerCard.cardName());
        System.out.println("Your new hand: " + activeHand.cardsToString());
        if(activeHand.highestNonBust()>21){
            System.out.println(activeHand.highestNonBust() + ", your hand busted!");
            if(!activeHand.isMainHand()||firstPlayer.getSplitHand()==null){
                currentState = State.DEALER_REVEAL;
            }
        } else if (activeHand.highestNonBust()==21){
            System.out.println("21, nice!");
        } else {
            System.out.println("Your possible hand values: " + activeHand.possibleValues());
        }
        utilityEnterToContinue();
    }
    private void playerActionStand() {
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("You stand!");
        System.out.print("Your current hand: " + activeHand.cardsToString());
        String isSplit = "main";
        if(!activeHand.isMainHand()){
            isSplit = "split";
        }
        System.out.println("(this is your "+ isSplit +" hand)");
        System.out.println("Hand value: " + activeHand.highestNonBust());
        if(activeHand.isMainHand()&&firstPlayer.getSplitHand()!=null){
            activeHand = firstPlayer.getSplitHand();
        } else
            currentState = State.DEALER_REVEAL;

        utilityEnterToContinue();
    }
    private void playerActionSplit() {
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("You split!");
        firstPlayer.newHand(new Hand(currentRules));
        firstPlayer.getSplitHand().addCard(activeHand.getCurrentCards().get(1));
        firstPlayer.getSplitHand().setMainHand(false);
        firstPlayer.bet(firstPlayer.getSplitHand(), activeHand.getBetAmount());
        activeHand.getCurrentCards().remove(1);
        System.out.println("Your main hand: " + firstPlayer.getCurrentHand().cardsToString());
        System.out.println("Possible values: " + firstPlayer.getCurrentHand().possibleValues());
        System.out.println("Your split hand: " + firstPlayer.getSplitHand().cardsToString());
        System.out.println("Possible values: " + firstPlayer.getSplitHand().possibleValues());
        utilityEnterToContinue();
    }
    private void playerActionDouble() {
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("You double down!");
        if(firstPlayer.getBank()<activeHand.getBetAmount()){
            System.out.println("You're going into the negative here... But we'll allow it.");
        }
        firstPlayer.bet(activeHand, activeHand.getBetAmount());
        playerActionHit();
        playerActionStand();
        firstPlayer.setActive(false);
        utilityEnterToContinue();
    }
    private void playerActionSurrender() {
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("You surrender! Get back half your bet.");
        int surrenderReward = (int)Math.floor(activeHand.getBetAmount()*.5);
        System.out.println("You get: $" + surrenderReward + " back.");
        firstPlayer.pay(surrenderReward);
        firstPlayer.setActive(false);
        utilityEnterToContinue();
    }
    private void playerActionPrintDeckStats(){
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("Printing deck stats!");
        System.out.println("Cards in shoe: " + deck.getCardsInShoe());
        System.out.println("Next card to deal: " + deck.getPeekCard().cardName());
//        System.out.println("All cards in shoe: " + deck.seeAllCardsInShoe());
        System.out.println("Current shoe stack:" + deck.getCurrentShoe());
        utilityEnterToContinue();
    }
    private void playerActionQuit() {
        utilityClearScreen();
        utilityDrawLine();
        System.out.println("Quitting!");
        keepPlaying = false;
        currentState = State.CLEANUP;
        utilityEnterToContinue();
    }

    private void dealerActionStand() {
        System.out.println("Dealer stands!");
        System.out.println("Dealer's hand: " + dealerHand.cardsToString());
        System.out.println("Dealer hand value: " + dealerHand.highestNonBust());
    }
    private void dealerActionHit(){
        System.out.println("Dealer hits!");
        Card newDealerCard = deck.dealCard();
        System.out.println("Dealer draws: " + newDealerCard.cardName());
        dealer.getCurrentHand().addCard(newDealerCard);
    }
    private void dealerActionBust() {
        System.out.println("Dealer is busted!");
    }

    private void utilityClearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
    private void utilityDrawLine(){
        // Provides a re-usable way to draw a graphical line in the console
        System.out.println("""
            ░█▀▄░█░░░█▀█░█▀▀░█░█░▀▀█░█▀█░█▀▀░█░█░░░█▀▄░█▀▀░█░░░█░█░█░█░█▀▀
            ░█▀▄░█░░░█▀█░█░░░█▀▄░░░█░█▀█░█░░░█▀▄░░░█░█░█▀▀░█░░░█░█░▄▀▄░█▀▀
            ░▀▀░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀░░▀░▀░▀▀▀░▀░▀░░░▀▀░░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀
                """);
    }
    private void utilityEnterToContinue(){
        // Prompts the user to press enter before continuing
        System.out.println("Press Enter to continue...");
        myScanner.nextLine();
    }
    private void utilityPrintBoard(){

        utilityClearScreen();
        utilityDrawLine();

        // Print dealer hand
        System.out.println(utilityEnumerateDealerCards());
        // Print player bank
        System.out.println("Current bank: " + firstPlayer.getBank());
        // Print player hand information
        System.out.println("Your main hand: " + firstPlayer.getCurrentHand().cardsToString());
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
}
    private void utilityPrintPossibleActions() {
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
        if(currentRules.isCheatMode()){
            System.out.println(Action.GET_DECK_STATS.commandLetter() +": Get Deck Stats");
        } else skipLines++;
        System.out.println(Action.QUIT.commandLetter() +": Quit");
        for(int i=0; i<skipLines;i++){
            System.out.println();
        }
    }
    private String utilityEnumerateDealerCards() {
        List<Card> dealerCards = dealer.getCurrentHand().getCurrentCards();
        String dealerCardOne = dealerCards.get(0).cardName();
        String dealerCardTwo = "";
        StringBuilder dealerExtraCards = new StringBuilder();
        for(int i=2;i<dealerCards.size();i++){
            dealerExtraCards.append(" and ").append(dealerCards.get(i).cardName());
        }
        if(currentState == State.PRE_DEAL||currentState == State.PLAYER_ACTIONS){
            dealerCardTwo = " and one face down.\nDealer showing value: " + dealerCards.get(0).handValue();

        } else {
            dealerCardTwo = " and " + dealerCards.get(1).cardName();
            dealerExtraCards.append("\nDealer showing value: ");
            dealerExtraCards.append(dealer.getCurrentHand().highestNonBust());
        }

        return "Dealer's Cards: " + dealerCardOne + dealerCardTwo + dealerExtraCards + ".";
    }
}
