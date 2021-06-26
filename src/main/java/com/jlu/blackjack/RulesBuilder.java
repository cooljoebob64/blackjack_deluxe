package com.jlu.blackjack;

import com.jlu.clihelper.CLIHelper;

import java.util.Locale;
import java.util.Scanner;

public class RulesBuilder {


    static String bannerText = """
              \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+-+-+-+
              \s\s|B|l|a|c|k|j|a|c|k| |D|e|l|u|x|e| |-| |R|u|l|e| |B|u|i|l|d|e|r|
              \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+-+-+-+
                """;

    private final static Scanner myScanner = new Scanner(System.in);

    public static Rules buildDefaultRules(){
        return new Rules();
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
    public static Rules buildCustomRules(CLIHelper cli) {
        cli.setLine(bannerText);

        Rules customRules = new Rules();

        cli.nicePrint("Let's go through each of the rules, and you can select each option.");
        cli.enterToContinue();

        boolean responding;
        String response;

        // Insurance
        responding = true;
        do {
            cli.nicePrint(new String[]{"Insurance",
                    "Explanation: Insurance is a side bet that allows a player to win back some money, "+
                    "available only if the dealer shows an Ace.",
                    "Should Insurance be allowed?",
                    "Default: " + customRules.isInsurance(),
                    "(Y/N, enter for default)"});
            response = myScanner.nextLine();
            if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.isInsurance());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setInsurance(true);
                cli.nicePrint("Insurance allowed set to: " + customRules.isInsurance());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setInsurance(false);
                cli.nicePrint("Insurance allowed set to: " + customRules.isInsurance());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Insurance Payout
        responding = true;
        do {
            cli.nicePrint(new String[]{"Insurance Payout",
                    "Explanation: How much insurance pays out if the dealer makes Blackjack.",
                    "How much should insurance pay out?",
                    "Default: " + customRules.getInsurancePayout(),
                    "(Enter a number with decimals, enter for default)"});
            response = myScanner.nextLine();
            double amount;

            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setInsurancePayout(amount);
                    cli.nicePrint("Insurance payout set to: " + customRules.getInsurancePayout());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getInsurancePayout());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Even Money Allowed
        responding = true;
        do {
            cli.nicePrint(new String[]{"Even Money",
                    "Explanation: Even Money gives the player the option to get their money back on a " +
                    "Blackjack if the dealer also has a Blackjack. If the dealer does not have a Blackjack, the " +
                    "player gets only the normal payout instead of the Blackjack payout.",
                    "Should Even Money be offered?",
                    "Default: " + customRules.isEvenMoney(),
                    "(Y/N, enter for default)"});
            response = myScanner.nextLine();
            if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.isEvenMoney());
                cli.enterToContinue();
                responding = false;
            }
            else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setEvenMoney(true);
                cli.nicePrint("Even money set to: " + customRules.isEvenMoney());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setEvenMoney(false);
                cli.nicePrint("Even money set to: " + customRules.isEvenMoney());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Shoe Penetration
        responding = true;
        do {
            cli.nicePrint(new String[]{"Penetration",
                    """
                    Explanation: Controls how far into the shoe cards are dealt before the shoe is reloaded.
                    The number is a ratio (expressed as a decimal number):
                    Cards Dealt / Total Shoe Size
                    """,
                    "Default: " + customRules.getPenetration(),
                    "(Enter a number with decimals, enter for default)"});
            response = myScanner.nextLine();
            double amount;
            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setInsurancePayout(amount);
                    cli.nicePrint("Penetration set to: " + customRules.getPenetration());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getPenetration());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Decks on Shoe Reload
        responding = true;
        do {
            cli.nicePrint(new String[]{"Decks to Load",
                    "Explanation: How many decks should be loaded into the shoe at a time.",
                    "Default: " + customRules.getDecksToLoad(),
                    "(Enter a whole integer number, enter for default)"});
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setDecksToLoad(amount);
                    cli.nicePrint("Decks to Load set to: " + customRules.getDecksToLoad());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getDecksToLoad());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Clear Shoe on Reload
        responding = true;
        do {
            cli.nicePrint(new String[]{"Clear Shoe on Load",
                    "Explanation: Controls if the shoe should be cleared when it is loaded.",
                    "Should the shoe be cleared on load?",
                    "Default: " + customRules.isClearShoeOnLoad(),
                    "(Y/N, enter for default)"});
            response = myScanner.nextLine();
            if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.isClearShoeOnLoad());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setClearShoeOnLoad(true);
                cli.nicePrint("Clear Shoe on Load set to: " + customRules.isClearShoeOnLoad());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setClearShoeOnLoad(false);
                cli.nicePrint("Clear Shoe on Load set to: " + customRules.isClearShoeOnLoad());
                cli.enterToContinue();
                responding = false;
            }  else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Push Rule
        responding = true;
        do {
            cli.nicePrint(new String[]{"Push Rule",
                    """
                    Explanation: What happens when the player and dealer tie (push)?
                    1. Tie (no action)
                    2. Player Wins
                    3. Player Loses
                    """,
                    "Default: " + Rules.PushRuleOptions.values()[customRules.getPushRule()],
                    "(Enter a whole integer number, or enter for default)"});
            response = myScanner.nextLine();
            int optionSelected;

            if (!response.isBlank()) {
                try {
                    optionSelected = Integer.parseInt(response);
                    if(optionSelected>=0&&optionSelected<=2) {
                        customRules.setPushRule(optionSelected);
                        cli.nicePrint("Push Rule set to: " +
                                Rules.PushRuleOptions.values()[customRules.getPushRule()]);
                        cli.enterToContinue();
                        responding = false;
                    } else throw new NumberFormatException("Number selected outside of range, try again.");
                } catch (NumberFormatException e) {
                    cli.nicePrint("Error: " + e.getMessage()+ "\nInvalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default Push Rule: " +
                        Rules.PushRuleOptions.values()[customRules.getPushRule()]);
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Dealer Hits Soft 17
        responding = true;
        do {
            cli.nicePrint(new String[]{"Dealer Hits Soft 17",
                    """
                    Explanation: Controls whether the dealer will hit on a 'soft 17' (a 17 with a 1-value Ace).
                    """,
                    "Should the dealer hit on soft 17?",
                    "Default: " + customRules.isDealerHitsSoft17(),
                    "(Y/N, enter for default)"});
            response = myScanner.nextLine();
            if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.isDealerHitsSoft17());
                cli.enterToContinue();
                responding = false;
            }
            else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setDealerHitsSoft17(true);
                cli.nicePrint("Dealer Hits Soft 17 set to: " + customRules.isDealerHitsSoft17());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setDealerHitsSoft17(false);
                cli.nicePrint("Dealer Hits Soft 17 set to: " + customRules.isDealerHitsSoft17());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Blackjack Payout
        responding = true;
        do {
            cli.nicePrint(new String[]{"Blackjack Payout",
                    "Explanation: How much the house pays on a player Blackjack," +
                    " expressed as a number with decimals.",
                    "How much should the house pay out on a Blackjack?",
                    "Default: " + customRules.getBlackjackPayout(),
                    "(Enter a number with decimals, enter for default)"});
            response = myScanner.nextLine();
            double amount;

            if (!response.isBlank()) {
                try {
                    amount = Double.parseDouble(response);
                    customRules.setBlackjackPayout(amount);
                    cli.nicePrint("Blackjack payout set to: " + customRules.getBlackjackPayout());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getBlackjackPayout());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Minimum Bet
        responding = true;
        do {
            cli.nicePrint(new String[]{"Minimum Bet",
                    "Explanation: What the smallest bet allowed at the table is.",
                    "Default: " + customRules.getMinBet(),
                    "(Enter a whole integer number, enter for default)"});
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setMinBet(amount);
                    cli.nicePrint("Minimum bet set to: " + customRules.getMinBet());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getMinBet());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        // Starting Bank
        responding = true;
        do {
            cli.nicePrint(new String[]{"Starting Bank",
                    "Explanation: How much should each player start the game with?",
                    "Default: " + customRules.getStartingBank(),
                    "(Enter a whole integer number, enter for default)"});
            response = myScanner.nextLine();
            int amount;

            if (!response.isBlank()) {
                try {
                    amount = Integer.parseInt(response);
                    customRules.setStartingBank(amount);
                    cli.nicePrint("Decks to Load set to: " + customRules.getStartingBank());
                    cli.enterToContinue();
                    responding = false;
                } catch (NumberFormatException e) {
                    cli.nicePrint("Invalid number, try again.");
                    cli.enterToContinue();
                }
            } else if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.getStartingBank());
                cli.enterToContinue();
                responding = false;
            } else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);
        // Cheat Mode
        responding = true;
        do {
            cli.nicePrint(new String[]{"Cheat Mode",
                    """
                    Explanation: Enables cheat mode! This will allow you to do things like look at the deck at any time,
                    potentially among other things...
                    """,
                    "Should cheat mode by enabled?",
                    "Default: " + customRules.isCheatMode(),
                    "(Y/N, enter for default)"});
            response = myScanner.nextLine();
            if (response.isBlank()) {
                cli.nicePrint("Accepting default: " + customRules.isCheatMode());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("Y")) {
                customRules.setCheatMode(true);
                cli.nicePrint("Cheat Mode set to: " + customRules.isCheatMode());
                cli.enterToContinue();
                responding = false;
            } else if (response.substring(0, 1).equalsIgnoreCase("N")) {
                customRules.setCheatMode(false);
                cli.nicePrint("Cheat Mode set to: " + customRules.isCheatMode());
                cli.enterToContinue();
                responding = false;
            }  else {
                cli.nicePrint("Response not recognized. Try again.");
                cli.enterToContinue();
            }
        } while (responding);

        cli.nicePrint("Custom rules complete!");
        cli.enterToContinue();
        return customRules;
    }

    public static void previewRules(Rules potentialRules, CLIHelper cli){
        cli.setLine(bannerText);
        boolean awaitingInput = true;
        String response = "";
        do {
            cli.nicePrint(new String[]{"The chosen rules are:",
                    potentialRules.getFormattedRules(),
                    "Would you like to start a game with these rules?",
                    "(Y/N):"});
            response = myScanner.nextLine();
            if(response.isBlank()){
                cli.nicePrint("Invalid input! Try again.");
                cli.enterToContinue();
            } else response = response.substring(0,1).toUpperCase(Locale.ROOT);
            if(response.equalsIgnoreCase("Y")){
                cli.nicePrint("Great! Starting game with your selected rules.");
                cli.enterToContinue();
                awaitingInput = false;
                new Game(potentialRules);
            } else if(response.equalsIgnoreCase("N")){
                cli.nicePrint("Ok, returning you to rules selection...");
                awaitingInput= false;
                cli.enterToContinue();
            } else {
                cli.nicePrint("Invalid input! Try again.");
                cli.enterToContinue();
            }
        } while (awaitingInput);
    }
}