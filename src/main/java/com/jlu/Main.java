package com.jlu;

import com.jlu.blackjack.RulesBuilder;
import com.jlu.blackjack.Game;
import com.jlu.clihelper.CLIHelper;

import java.util.Scanner;


public class Main {

    static CLIHelper cli;

    static String bannerText = """
                \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+
                \s\s|B|l|a|c|k|j|a|c|k| |D|e|l|u|x|e| |-| |M|a|i|n| |M|e|n|u|
                \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+
                """;

    /**
     * Main class which starts the game and interacts with the user.
     * @param args Arguments provided at run time
     */
    public static void main(String[] args){

        myScanner = new Scanner(System.in);
        setupCLI();

        String response = "";
        boolean inMenu = true;

        cli.nicePrint("Welcome to Blackjack Deluxe!");
        cli.enterToContinue();
        do {
            cli.nicePrint(new String[]{"Enter which option you'd like:",
                    "1: New Game with Default Rules",
                    "2: New Game with Custom Rules",
                    "Q: Quit"});
            response = myScanner.nextLine();
            switch (response){
                case "1" -> {defaultGame();}
                case "2" -> {customGame();}
                case "Q", "q" -> {
                    cli.nicePrint("Quitting the game!");
                    inMenu = false;
                }
                default -> {
                    cli.nicePrint("Unrecognized input, try again!");
                    cli.enterToContinue();
                }
            }
        } while(inMenu);
        cli.nicePrint("End of program.");
    }

    private static void setupCLI() {
        cli = CLIHelper.getInstance();
        cli.setLine(bannerText);
        cli.setConsoleSizeTo(15);
        cli.setScanner(myScanner);
    }

    private static void defaultGame() {
        // Launch the game with default rules
        System.out.println("Starting a new game with default rules!");
        new Game();
    }
    private static void customGame() {
        // Provide the user with some options about how to customize rules for the game
//        System.out.println("Custom rules are not yet implemented, sorry!");
        boolean awaitingInput = true;
        String response = "";

        do {
            cli.nicePrint(new String[]{"Which rule set would you like to use?",
                    "(Enter a number and press enter)",
                    "1. Default Rules",
                    "2. Easy Rules",
                    "3. Hard Rules",
                    "0. Custom Rules"});

            int selection;
            response = myScanner.nextLine();
            try {
                selection = Integer.parseInt(response);
                switch (selection){
                    case 1 -> {
                        cli.nicePrint("Default rules selected!");
                        cli.enterToContinue();
                        RulesBuilder.previewRules(RulesBuilder.buildDefaultRules());
                        awaitingInput = false;
                    }
                    case 2 -> {
                        cli.nicePrint("Easy rules selected!");
                        cli.enterToContinue();
                        RulesBuilder.previewRules(RulesBuilder.buildEZRules());
                        awaitingInput = false;
                    }
                    case 3 -> {
                        cli.nicePrint("Hard rules selected!");
                        cli.enterToContinue();
                        RulesBuilder.previewRules(RulesBuilder.buildHardRules());
                        awaitingInput = false;
                    }
                    case 0 -> {
                        cli.nicePrint("Custom rules selected!");
                        cli.enterToContinue();
                        RulesBuilder.previewRules(RulesBuilder.buildCustomRules());
                        awaitingInput = false;
                    }
                    default -> {
                        cli.nicePrint("Invalid option! Try again.");
                        cli.enterToContinue();
                    }
                }
            } catch (NumberFormatException e){
                cli.nicePrint("Invalid option! Try again!");
                enterToContinue();
            }
        } while (awaitingInput);
    }

    private static void clearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
    private static void drawLine(){
        // Provides a re-usable way to draw a graphical line in the console
        System.out.println("""
                \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+
                \s\s|B|l|a|c|k|j|a|c|k| |D|e|l|u|x|e| |-| |M|a|i|n| |M|e|n|u|
                \s\s+-+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+ +-+-+-+-+ +-+-+-+-+
                """);
    }
    private static void enterToContinue(){
        // Prompts the user to press enter before continuing
        System.out.println("Press Enter to continue...");
        myScanner.nextLine();
    }

    private static Scanner myScanner;
}
