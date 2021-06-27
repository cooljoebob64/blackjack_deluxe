package com.jlu.blackjack;

import com.jlu.clihelper.CLIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Game {

    CLIHelper cli;

    String bannerText = """
            ░█▀▄░█░░░█▀█░█▀▀░█░█░▀▀█░█▀█░█▀▀░█░█░░░█▀▄░█▀▀░█░░░█░█░█░█░█▀▀
            ░█▀▄░█░░░█▀█░█░░░█▀▄░░░█░█▀█░█░░░█▀▄░░░█░█░█▀▀░█░░░█░█░▄▀▄░█▀▀
            ░▀▀░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀░░▀░▀░▀▀▀░▀░▀░░░▀▀░░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀
                """;

    /**
     * Empty constructor, starts game with default rules
     */
    public Game(){
        this(new Rules());
    }
    public Game(CLIHelper cli){
        this(new Rules(), cli);
    }
    public Game(Rules gameRules){
        this(gameRules, CLIHelper.getInstance());
    }
    /**
     * Constructor to start the game with specified rules set
     * @param gameRules A rules object that specifies how the game will be played
     */
    public Game(Rules gameRules, CLIHelper cli){
        myScanner = new Scanner(System.in);
        this.cli = cli;
        String oldBanner = cli.getLine();
        cli.setLine(bannerText);
        cli.setScanner(myScanner);

        players = new ArrayList<>();

        this.currentRules = gameRules;
        this.currentState = State.PRE_DEAL;
        this.keepPlaying = true;

//        enterToContinue();
        cli.nicePrint(new String[]{"Game Rules:",
                currentRules.getFormattedRules()});
        cli.enterToContinue();

        newGameSetup();
        mainLoop();
        cli.setLine(oldBanner);
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
        dealerHand = dealer.getCurrentHand();

        deck = new Deck(currentRules);
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
                default -> cli.nicePrint("Error! State not loaded.");
            }
        } while(keepPlaying);
    }

    private void statePreDeal(){
        cli.nicePrint("Starting new round!");
        cli.enterToContinue();
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
                    cli.nicePrint(new String[]{"Your current bank: " + player.getBank(),
                            "Hey, you are all outta money!",
                            "That's alright, we're really nice here.",
                            "We'll put you back in the game."});
                    player.setBank((int)currentRules.getMinBet());
                    cli.enterToContinue();
                }
                boolean awaitingInput = true;
                do{
                    cli.nicePrint(new String[]{player.getName() + ": Your Bet.",
                            "Your current bank: " + player.getBank(),
                            "How much would you like to bet on this hand?",
                            "(Minimum bet: " + minBet + ")",
                            "Bet Amount: (enter for minimum)"});
                    String response = myScanner.nextLine();
                    if(response.isBlank()){
                        betAmount = minBet;
                        player.bet(player.getCurrentHand(), betAmount);
                        cli.nicePrint("You are betting the minimum: " + player.getCurrentHand().getBetAmount());
                        cli.enterToContinue();
                        stillBetting = false;
                        awaitingInput = false;
                    } else {
                        try {
                            betAmount = Integer.parseInt(response);
                            if (betAmount < minBet) {
                                cli.nicePrint("Bet too low! Try again.");
                                cli.enterToContinue();
                            } else if (betAmount > player.getBank()) {
                                cli.nicePrint("Hey, you don't have that much! Try again.");
                                cli.enterToContinue();
                            } else {
                                player.bet(player.getCurrentHand(), betAmount);
                                cli.nicePrint("You are betting: " + player.getCurrentHand().getBetAmount());
                                stillBetting = false;
                            }
                            awaitingInput = false;
                        } catch (NumberFormatException e) {
                            cli.nicePrint("That's not a valid number! Try that again...");
                            cli.enterToContinue();
                        }
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
                && activeHand.getOwner().isOnFirstAction()){ // Surrender possibility
            activeHand.addAction(Action.SURRENDER);
        }

        // If player has blackjack, and dealer shows Ace, allow Even Money possibility
        if(activeHand.getOwner().isOnFirstAction()&&activeHand.highestNonBust()==21&& currentRules.isEvenMoney()){ // Even Money possibility
            activeHand.addAction(Action.EVEN_MONEY);
        }

        // If the dealer is showing an Ace, allow the player to buy Insurance
        if(activeHand.getOwner().isOnFirstAction()  &&
                currentRules.isInsurance() &&
                dealerHand.getCurrentCards().get(0).handValue()==11){
            activeHand.addAction(Action.INSURANCE);
        }

        // Add options available in Cheat Mode
        if(currentRules.isCheatMode()) {
            activeHand.addAction(Action.GET_DECK_STATS);
        }

        boolean validChoice = false;
        do {
            utilityPrintBoard();
            String response = myScanner.nextLine();
            if(response.isEmpty()){
                cli.nicePrint("Invalid command! Try again.");
                cli.enterToContinue();
                validChoice = false;
            } else {
                response = response.substring(0,1).toUpperCase(Locale.ROOT);
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
                    case "I" -> {
                        if (firstPlayer.getCurrentHand().getActions().contains(Action.INSURANCE)) {
                            validChoice = true;
                            playerActionInsurance();
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
                    case "E" -> {
                        if(firstPlayer.getCurrentHand().getActions().contains(Action.EVEN_MONEY)){
                            playerActionEvenMoney();
                            validChoice = true;
                            activeHand.getOwner().setOnFirstAction(false);
                        }
                    }
                    case "G" -> {
                        if(currentRules.isCheatMode()){
                            playerActionPrintDeckStats();
                            validChoice = true;
                        } else{
                            cli.nicePrint("Cheat mode not enabled, nice try!");
                        }
                    }
                    case "Q" -> {
                        playerActionQuit();
                        validChoice = true;
                    }
                    default -> {
                        cli.nicePrint("Invalid command! Try again.");
                        cli.enterToContinue();
                        validChoice = false;
                    }
                }
            }
//            enterToContinue();
        } while(!validChoice);
    }
    private void stateDealerReveal(){
        List<String> dealerHandBuilder = new ArrayList<>();
        dealerHandBuilder.add("Dealer reveals their hand: ");
        dealerHandBuilder.add(dealerHand.cardsToString());
        dealerHandBuilder.add("Initial hand value: " + dealerHand.highestNonBust());
        // Check dealer blackjack for insurance payout
        if(dealerHand.highestNonBust()==21 && dealerHand.getCurrentCards().size()==2 && currentRules.isInsurance()){
            for(Player player: players){
                for (Hand hand:player.getAllHands()){
                    if(hand.getInsuranceAmount()>0){
                        String isMain = hand.isMainHand()?"main":"split";
                        dealerHandBuilder.add(player.getName() +", the dealer had Blackjack, and you had insurance on " +
                                "your "+ isMain +" hand!");
                        dealerHandBuilder.add("Insurance amount: " + hand.getInsuranceAmount());
                        dealerHandBuilder.add("Insurance payout rate: " + currentRules.getInsurancePayout());
                        int insuranceReward = (int)Math.floor(hand.getInsuranceAmount() * currentRules.getInsurancePayout());
                        dealerHandBuilder.add("You get: " + insuranceReward + " immediately!");
                        player.pay(insuranceReward);
                    }
                }
            }
        }
        dealerHandBuilder.add("Dealer will play now!");
        cli.nicePrint(dealerHandBuilder);
        cli.enterToContinue();
        this.currentState = State.DEALER_UP;
    }
    private void stateDealerUp(){
        Hand dealerHand = dealer.getCurrentHand();
        cli.nicePrint("Dealer's starting hand: " + dealerHand.cardsToString());
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
//            cli.enterToContinue();
        } while(keepDealing);
        this.currentState = State.PAYOUT;
    }
    private void statePayout(){
        List<String> payoutBuilder = new ArrayList<>();
        int dealerValue = dealer.getCurrentHand().highestNonBust();
        payoutBuilder.add("Dealer hand value: " + dealerValue);
        for(Player player:players){
            payoutBuilder.add("Calculating hands for " + player.getName());
            for(Hand hand : player.getAllHands()){
                payoutBuilder.add("This hand: " + hand.cardsToString());
                String isMain = "main";
                if(!hand.isMainHand()){
                    isMain = "split";
                }
                payoutBuilder.add("(this is your " + isMain + " hand)");
                payoutBuilder.add("Highest value: " + hand.highestNonBust());
                if(hand.isSurrendered()){
                    payoutBuilder.add("You surrendered, and already got your money back.");
                }
                if(hand.isTookEvenMoney()){
                    payoutBuilder.add("You took the Even Money option!");
                }
                int highValue = hand.highestNonBust();
                int payoutAmount;

                if(highValue==21&&hand.getCurrentCards().size()==2){ // Blackjack!

                    if(hand.isTookEvenMoney()){ // Even money taken
                        if(dealerValue==21&&dealerHand.getCurrentCards().size()==2){ // Both Blackjack
                            payoutBuilder.add("Dealer also has Blackjack! You get Even Money!");
                            payoutBuilder.add("Bet: " + hand.getBetAmount());
                            payoutAmount = hand.getBetAmount();
                            payoutBuilder.add("You win Even Money payout: " + payoutAmount);
                        }
                        else { // Player wins, took even money
                            payoutBuilder.add("You win with Blackjack, but took even money.");
                            payoutBuilder.add("Bet: " + hand.getBetAmount());
                            payoutAmount = hand.getBetAmount() * 2;
                            payoutBuilder.add("You win: " + payoutAmount);
                        }
                    }
                    else { // Even Money not taken
                        payoutAmount = (int) Math.ceil(hand.getBetAmount() * currentRules.getBlackjackPayout()) + hand.getBetAmount();
                        payoutBuilder.add("Blackjack!");
                        payoutBuilder.add("Bet: " + hand.getBetAmount());
                        payoutBuilder.add("Blackjack payout: " + currentRules.getBlackjackPayout());
                        payoutBuilder.add("You win: " + payoutAmount);
                    }
                } else if(highValue>21){ // Busted hand
                    payoutBuilder.add("This hand busted!");
                    payoutBuilder.add("House keeps your bet!");
                    payoutAmount = 0;
                } else if(highValue>dealerValue||dealerValue>21){ // Hand beats dealer
                    payoutBuilder.add("Your " + highValue + " beats the dealer!");
                    payoutAmount = 2* hand.getBetAmount();
                } else if(highValue==dealerValue){ // Push
                    payoutBuilder.add("You tied the dealer. Push!");
                    if(currentRules.getPushRule()==0){ // No action
                        payoutBuilder.add("No action. Bet refunded!");
                        payoutAmount = hand.getBetAmount();
                    } else if(currentRules.getPushRule()==1){ // Player win
                        payoutBuilder.add("Player wins the push!");
                        payoutAmount = hand.getBetAmount()*2;
                    } else { // Player loss
                        payoutBuilder.add("Player loses the push!");
                        payoutAmount = 0;
                    }
                } else { // Hand loses to dealer
                    payoutBuilder.add("Your " + highValue + " didn't beat the dealer. Sorry!");
                    payoutAmount = 0;
                }
                payoutBuilder.add("You receive: " + payoutAmount);
                player.pay(payoutAmount);
                payoutBuilder.add("Your new bank: " + player.getBank());
            }
        }
        cli.nicePrint(payoutBuilder);
        cli.enterToContinue();
        this.currentState = State.CLEANUP;
    }
    private void stateCleanup(){
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

    private void playerActionHit() {

        List<String> actionBuilder = new ArrayList<>();

        actionBuilder.add("You hit!");
        Card newPlayerCard = deck.dealCard();
        activeHand.addCard(newPlayerCard);
        actionBuilder.add("You drew: " + newPlayerCard.cardName());
        actionBuilder.add("Your new hand: " + activeHand.cardsToString());
        if(activeHand.highestNonBust()>21){
            actionBuilder.add(activeHand.highestNonBust() + ", your hand busted!");
            if(!activeHand.isMainHand()||firstPlayer.getSplitHand()==null){
                currentState = State.DEALER_REVEAL;
            }
        } else if (activeHand.highestNonBust()==21){
            actionBuilder.add("21, nice!");
        } else {
            actionBuilder.add("Your possible hand values: " + activeHand.possibleValues());
        }
        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
    }
    private void playerActionStand() {
        List<String> actionBuilder = new ArrayList<>();
        actionBuilder.add("You stand!");
        actionBuilder.add("Your current hand: " + activeHand.cardsToString());
        String isSplit = "main";
        if(!activeHand.isMainHand()){
            isSplit = "split";
        }
        actionBuilder.add("(this is your "+ isSplit +" hand)");
        actionBuilder.add("Hand value: " + activeHand.highestNonBust());
        if(activeHand.isMainHand()&&firstPlayer.getSplitHand()!=null){
            activeHand = firstPlayer.getSplitHand();
        } else
            currentState = State.DEALER_REVEAL;

        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
    }
    private void playerActionSplit() {
        List<String> actionBuilder = new ArrayList<>();
        actionBuilder.add("You split!");
        firstPlayer.newHand(new Hand(currentRules));
        firstPlayer.getSplitHand().addCard(activeHand.getCurrentCards().get(1));
        firstPlayer.getSplitHand().setMainHand(false);
        firstPlayer.bet(firstPlayer.getSplitHand(), activeHand.getBetAmount());
        activeHand.getCurrentCards().remove(1);
        actionBuilder.add("Your main hand: " + firstPlayer.getCurrentHand().cardsToString());
        actionBuilder.add("Possible values: " + firstPlayer.getCurrentHand().possibleValues());
        actionBuilder.add("Your split hand: " + firstPlayer.getSplitHand().cardsToString());
        actionBuilder.add("Possible values: " + firstPlayer.getSplitHand().possibleValues());
        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
    }
    private void playerActionDouble() {
        List<String> actionBuilder = new ArrayList<>();
        actionBuilder.add("You double down!");
        if(firstPlayer.getBank()<activeHand.getBetAmount()){
            actionBuilder.add("You're going into the negative here... But we'll allow it.");
        }
        firstPlayer.bet(activeHand, activeHand.getBetAmount());
        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
        playerActionHit();
        playerActionStand();
        firstPlayer.setActive(false);
    }
    private void playerActionSurrender() {
        List<String> actionBuilder = new ArrayList<>();
        actionBuilder.add("You surrender! Get back half your bet (rounded down).");
        int surrenderReward = (int)Math.floor(activeHand.getBetAmount()*.5);
        actionBuilder.add("You get: " + surrenderReward + " back.");
        firstPlayer.pay(surrenderReward);
        activeHand.clearBet();
//        firstPlayer.setActive(false);
        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
    }
    private void playerActionInsurance() {
        List<String> actionBuilder = new ArrayList<>();
        actionBuilder.add("You've opted for insurance! This costs half of your original bet.");
        int insuranceAmount = (int)Math.floor(activeHand.getBetAmount()*.5);
        actionBuilder.add("Insurance cost: " + insuranceAmount);
        firstPlayer.pay(-insuranceAmount);
        activeHand.setInsuranceAmount(insuranceAmount);
        actionBuilder.add("Your new bank: " + firstPlayer.getBank());
        cli.nicePrint(actionBuilder);
        cli.enterToContinue();
    }
    private void playerActionEvenMoney() {
        cli.nicePrint(new String[]{"You are taking the Even Money option!",
                "Your current hand: " + activeHand.getCurrentCards(),
                "Current hand value: " + activeHand.highestNonBust(),
                "If the dealer has a Blackjack, you will get your bet back.",
                "If you beat the dealer, you will win the regular (1:1) payout."});
        activeHand.setTookEvenMoney(true);
        cli.enterToContinue();
    }
    private void playerActionPrintDeckStats(){
        cli.nicePrint(new String[]{"Printing deck stats!",
                "Cards in shoe: " + deck.getCardsInShoe(),
                "Next card to deal: " + deck.getPeekCard().cardName(),
                "All cards in shoe: " + deck.seeAllCardsInShoe(),
                "Current shoe stack:" + deck.getCurrentShoe()});
        cli.enterToContinue();
    }
    private void playerActionQuit() {
        cli.nicePrint("Quitting!");
        keepPlaying = false;
        currentState = State.CLEANUP;
        cli.enterToContinue();
    }

    private void dealerActionStand() {
        cli.nicePrint(new String[]{"Dealer stands!",
                "Dealer's hand: " + dealerHand.cardsToString(),
                "Dealer hand value: " + dealerHand.highestNonBust()});
        cli.enterToContinue();
    }
    private void dealerActionHit(){
        Card newDealerCard = deck.dealCard();
        cli.nicePrint(new String[]{"Dealer hits!",
                "Dealer draws: " + newDealerCard.cardName()});
        dealer.getCurrentHand().addCard(newDealerCard);
        cli.enterToContinue();
    }
    private void dealerActionBust() {
        cli.nicePrint("Dealer is busted!");
        cli.enterToContinue();
    }

//    private void utilityClearScreen(){
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//    }
//    private void utilityDrawLine(){
//        // Provides a re-usable way to draw a graphical line in the console
//        System.out.println("""
//            ░█▀▄░█░░░█▀█░█▀▀░█░█░▀▀█░█▀█░█▀▀░█░█░░░█▀▄░█▀▀░█░░░█░█░█░█░█▀▀
//            ░█▀▄░█░░░█▀█░█░░░█▀▄░░░█░█▀█░█░░░█▀▄░░░█░█░█▀▀░█░░░█░█░▄▀▄░█▀▀
//            ░▀▀░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀░░▀░▀░▀▀▀░▀░▀░░░▀▀░░▀▀▀░▀▀▀░▀▀▀░▀░▀░▀▀▀
//                """);
//    }
//    private void utilityEnterToContinue(){
//        // Prompts the user to press enter before continuing
//        System.out.println("Press Enter to continue...");
//        myScanner.nextLine();
//    }
    private void utilityPrintBoard(){

        List<String> boardBuilder = new ArrayList<>();

        // Print dealer hand
        boardBuilder.add(utilityEnumerateDealerCards());
        // Print player bank
        boardBuilder.add("Current bank: " + firstPlayer.getBank());
        // Print player hand information
        boardBuilder.add("Your main hand: " + firstPlayer.getCurrentHand().cardsToString());
        boardBuilder.add("Main hand bet: " + firstPlayer.getAllHands().get(0).getBetAmount());
        boardBuilder.add("Main hand possible Values: " + firstPlayer.getCurrentHand().possibleValues());
        String playerSplitHand = "";
        if(firstPlayer.getSplitHand()!= null){
            boardBuilder.add("Your split hand: " + firstPlayer.getSplitHand().cardsToString());
            boardBuilder.add("Split hand bet: " + firstPlayer.getAllHands().get(1).getBetAmount());
            boardBuilder.add("Split hand possible Values: " + firstPlayer.getSplitHand().possibleValues());
        } else
            boardBuilder.add("You do not have a split hand.\n");

        utilityPrintPossibleActions(boardBuilder);
}
    private void utilityPrintPossibleActions(List<String> boardBuilder) {
        boardBuilder.add("Pick an action:");
//        int skipLines = 0;
        if(activeHand.getActions().contains(Action.STAND)){
            boardBuilder.add(Action.STAND.commandLetter() +": Stand");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.HIT)){
            boardBuilder.add(Action.HIT.commandLetter() +": Hit");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.DOUBLE)){
            boardBuilder.add(Action.DOUBLE.commandLetter() +": Double Down");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.SPLIT)){
            boardBuilder.add(Action.SPLIT.commandLetter() +": Split");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.SURRENDER)){
            boardBuilder.add(Action.SURRENDER.commandLetter() +": Surrender");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.INSURANCE)){
            boardBuilder.add(Action.INSURANCE.commandLetter() +": Buy Insurance");
        }
//        else skipLines++;
        if(activeHand.getActions().contains(Action.EVEN_MONEY)){
            boardBuilder.add(Action.EVEN_MONEY.commandLetter() +": Take Even-Money Option");
        }
//        else skipLines++;
        if(currentRules.isCheatMode()){
            boardBuilder.add(Action.GET_DECK_STATS.commandLetter() +": Get Deck Stats");
        }
//        else skipLines++;
        boardBuilder.add(Action.QUIT.commandLetter() +": Quit");
//        for(int i=0; i<skipLines;i++){
//            System.out.println();
//        }

        String isSplitHand = activeHand.isMainHand() ? "main" : "split";
        boardBuilder.add("(this is your " + isSplitHand + " hand)");
        boardBuilder.add("What would you like to do?");
        cli.nicePrint(boardBuilder);
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
