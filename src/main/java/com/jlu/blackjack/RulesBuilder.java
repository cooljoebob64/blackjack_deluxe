package com.jlu.blackjack;

import java.util.Locale;
import java.util.Scanner;

public class RulesBuilder {
    private final static Scanner myScanner = new Scanner(System.in);
    private static void drawLine(){
        // Provides a re-usable way to draw a graphical line in the console
        System.out.println("""
              \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+-+-+-+
              \s\s|B|l|a|c|k|j|a|c|k| |D|e|l|u|x|e| |-| |R|u|l|e| |B|u|i|l|d|e|r|
              \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+-+-+-+
                """);
    }
    private static void enterToContinue(){
        // Prompts the user to press enter before continuing
        System.out.println("Press Enter to continue...");
        myScanner.nextLine();
    }
    private static void clearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static Rules buildDefaultRules(){
        clearScreen();
        drawLine();
        return new Rules();
    }

    public static Rules buildCustomRules() {
        clearScreen();
        drawLine();
        Rules customRules = new Rules();

        System.out.println("Let's go through each of the rules, and you can select each option.");
        enterToContinue();

        boolean responding;
        String response;

        // Insurance
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Insurance");
            System.out.println("Explanation: Insurance is a side bet that allows a player to win back some money, " +
                    "\navailable only if the dealer shows an Ace.");
            System.out.println("Should Insurance be allowed?");
            System.out.println("Default: " + customRules.isInsurance());
            System.out.println("(Y/N, enter for default)");
            response = myScanner.nextLine();
            if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.isInsurance());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setInsurance(true);
                System.out.println("Insurance allowed set to: " + customRules.isInsurance());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setInsurance(false);
                System.out.println("Insurance allowed set to: " + customRules.isInsurance());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Insurance Payout
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Insurance Payout");
            System.out.println("Explanation: How much insurance pays out if the dealer makes Blackjack.");
            System.out.println("How much should insurance pay out?");
            System.out.println("Default: " + customRules.getInsurancePayout());
            System.out.println("(Enter a number with decimals, enter for default)");
            response = myScanner.nextLine();
            double amount;

            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setInsurancePayout(amount);
                    System.out.println("Insurance payout set to: " + customRules.getInsurancePayout());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getInsurancePayout());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Even Money Allowed
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Even Money");
            System.out.println("Explanation: Even Money gives the player the option to get their money back on a " +
                    "Blackjack if the dealer also has a Blackjack. If the dealer does not have a Blackjack, the " +
                    "player gets only the normal payout instead of the Blackjack payout.");
            System.out.println("Should Even Money be offered?");
            System.out.println("Default: " + customRules.isEvenMoney());
            System.out.println("(Y/N, enter for default)");
            response = myScanner.nextLine();
            if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.isEvenMoney());
                enterToContinue();
                responding = false;
            }
            else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setEvenMoney(true);
                System.out.println("Even money set to: " + customRules.isEvenMoney());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setEvenMoney(false);
                System.out.println("Even money set to: " + customRules.isEvenMoney());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Shoe Penetration
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Penetration");
            System.out.println("""
                    Explanation: Controls how far into the shoe cards are dealt before the shoe is reloaded.
                    The number is a ratio (expressed as a decimal number):
                    Cards Dealt / Total Shoe Size
                    """);
            System.out.println("Default: " + customRules.getPenetration());
            System.out.println("(Enter a number with decimals, enter for default)");
            response = myScanner.nextLine();
            double amount;
            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setInsurancePayout(amount);
                    System.out.println("Penetration set to: " + customRules.getPenetration());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getPenetration());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Decks on Shoe Reload
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Decks to Load");
            System.out.println("Explanation: How many decks should be loaded into the shoe at a time.");
            System.out.println("Default: " + customRules.getDecksToLoad());
            System.out.println("(Enter a whole integer number, enter for default)");
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setDecksToLoad(amount);
                    System.out.println("Decks to Load set to: " + customRules.getDecksToLoad());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getDecksToLoad());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Clear Shoe on Reload
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Clear Shoe on Load");
            System.out.println("Explanation: Controls if the shoe should be cleared when it is loaded.");
            System.out.println("Should the shoe be cleared on load?");
            System.out.println("Default: " + customRules.isClearShoeOnLoad());
            System.out.println("(Y/N, enter for default)");
            response = myScanner.nextLine();
            if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.isClearShoeOnLoad());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setClearShoeOnLoad(true);
                System.out.println("Clear Shoe on Load set to: " + customRules.isClearShoeOnLoad());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setClearShoeOnLoad(false);
                System.out.println("Clear Shoe on Load set to: " + customRules.isClearShoeOnLoad());
                enterToContinue();
                responding = false;
            }  else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Push Rule
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Push Rule");
            System.out.println("""
                    Explanation: What happens when the player and dealer tie (push)?
                    1. Tie (no action)
                    2. Player Wins
                    3. Player Loses
                    """);
            System.out.println("Default: " + Rules.PushRuleOptions.values()[customRules.getPushRule()]);
            System.out.println("(Enter a whole integer number, or enter for default)");
            response = myScanner.nextLine();
            int optionSelected;

            if (!response.isBlank()) {
                try {
                    optionSelected = Integer.parseInt(response);
                    if(optionSelected>=0&&optionSelected<=2) {
                        customRules.setPushRule(optionSelected);
                        System.out.println("Push Rule set to: " +
                                Rules.PushRuleOptions.values()[customRules.getPushRule()]);
                        enterToContinue();
                        responding = false;
                    } else throw new NumberFormatException("Number selected outside of range, try again.");
                } catch (NumberFormatException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default Push Rule: " +
                        Rules.PushRuleOptions.values()[customRules.getPushRule()]);
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Dealer Hits Soft 17
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Dealer Hits Soft 17");
            System.out.println("""
                    Explanation: Controls whether the dealer will hit on a 'soft 17' (a 17 with a 1-value Ace).
                    """);
            System.out.println("Should the dealer hit on soft 17?");
            System.out.println("Default: " + customRules.isDealerHitsSoft17());
            System.out.println("(Y/N, enter for default)");
            response = myScanner.nextLine();
            if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.isDealerHitsSoft17());
                enterToContinue();
                responding = false;
            }
            else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setDealerHitsSoft17(true);
                System.out.println("Dealer Hits Soft 17 set to: " + customRules.isDealerHitsSoft17());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setDealerHitsSoft17(false);
                System.out.println("Dealer Hits Soft 17 set to: " + customRules.isDealerHitsSoft17());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Blackjack Payout
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Blackjack Payout");
            System.out.println("Explanation: How much the house pays on a player Blackjack," +
                    " expressed as a number with decimals.");
            System.out.println("How much should the house pay out on a Blackjack?");
            System.out.println("Default: " + customRules.getBlackjackPayout());
            System.out.println("(Enter a number with decimals, enter for default)");
            response = myScanner.nextLine();
            double amount;

            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setBlackjackPayout(amount);
                    System.out.println("Blackjack payout set to: " + customRules.getBlackjackPayout());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getBlackjackPayout());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Minimum Bet
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Minimum Bet");
            System.out.println("Explanation: What the smallest bet allowed at the table is.");
            System.out.println("Default: " + customRules.getMinBet());
            System.out.println("(Enter a whole integer number, enter for default)");
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setMinBet(amount);
                    System.out.println("Minimum bet set to: " + customRules.getMinBet());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getMinBet());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        // Starting Bank
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Starting Bank");
            System.out.println("Explanation: How much should each player start the game with?");
            System.out.println("Default: " + customRules.getStartingBank());
            System.out.println("(Enter a whole integer number, enter for default)");
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setStartingBank(amount);
                    System.out.println("Decks to Load set to: " + customRules.getStartingBank());
                    enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                    enterToContinue();
                }
            } else if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.getStartingBank());
                enterToContinue();
                responding = false;
            } else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);
        // Cheat Mode
        responding = true;
        do {
            clearScreen();
            drawLine();
            System.out.println("Cheat Mode");
            System.out.println("""
                    Explanation: Enables cheat mode! This will allow you to do things like look at the deck at any time,
                    potentially among other things...
                    """);
            System.out.println("Should cheat mode by enabled?");
            System.out.println("Default: " + customRules.isCheatMode());
            System.out.println("(Y/N, enter for default)");
            response = myScanner.nextLine();
            if (response.isBlank()) {
                System.out.println("Accepting default: " + customRules.isCheatMode());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setCheatMode(true);
                System.out.println("Cheat Mode set to: " + customRules.isCheatMode());
                enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setCheatMode(false);
                System.out.println("Cheat Mode set to: " + customRules.isCheatMode());
                enterToContinue();
                responding = false;
            }  else {
                System.out.println("Response not recognized. Try again.");
                enterToContinue();
            }
        } while (responding);

        System.out.println("Custom rules complete!");
        enterToContinue();
        return customRules;
    }

    public static Rules buildEZRules(){
        Rules ezRules = new Rules();

        ezRules.setInsurance(true);
        ezRules.setInsurancePayout(2.5);
        ezRules.setEvenMoney(true);
        ezRules.setPenetration(.99);
        ezRules.setDecksToLoad(1);
        ezRules.setClearShoeOnLoad(false);
        ezRules.setPushRule(1);
        ezRules.setDealerHitsSoft17(false);
        ezRules.setBlackjackPayout(3.0);
        ezRules.setMinBet(1);
        ezRules.setStartingBank(100);
        ezRules.setCheatMode(true);

        return ezRules;
    }
    public static Rules buildHardRules(){
        Rules hardRules = new Rules();

        hardRules.setInsurance(false);
        hardRules.setInsurancePayout(2.0);
        hardRules.setEvenMoney(false);
        hardRules.setPenetration(.65);
        hardRules.setDecksToLoad(3);
        hardRules.setClearShoeOnLoad(true);
        hardRules.setPushRule(2);
        hardRules.setDealerHitsSoft17(true);
        hardRules.setBlackjackPayout(1.2);
        hardRules.setMinBet(10);
        hardRules.setStartingBank(50);
        hardRules.setCheatMode(false);

        return hardRules;
    }

    public static void previewRules(Rules potentialRules){

        boolean awaitingInput = true;
        String response = "";
        do {
            clearScreen();
            drawLine();
            System.out.println("The chosen rules are:");
            System.out.println(potentialRules.getFormattedRules());

            System.out.println("Would you like to use these rules?");
            System.out.println("(Y/N):");
            response = myScanner.nextLine().substring(0,1).toUpperCase(Locale.ROOT);
            if(response.isBlank()){
                System.out.println("Invalid input! Try again.");
                enterToContinue();
            }
            if(response.equalsIgnoreCase("Y")){
                System.out.println("Great! Starting game with your selected rules.");
                enterToContinue();
                awaitingInput = false;
                new Game(potentialRules);
            } else if(response.equalsIgnoreCase("N")){
                System.out.println("Ok, returning you to rules selection...");
                awaitingInput= false;
                enterToContinue();
            } else {
                System.out.println("Invalid input! Try again.");
                enterToContinue();
            }
        } while (awaitingInput);
    }

}