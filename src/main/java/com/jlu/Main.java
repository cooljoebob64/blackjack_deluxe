package com.jlu;

import com.jlu.blackjack.RulesBuilder;
import com.jlu.blackjack.Game;

import java.util.Scanner;

import com.jlu.clihelper.CLIHelper;

//import CLIHelper;

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

        setupCLI();

        myScanner = new Scanner(System.in);
        String response = "";
        boolean inMenu = true;

        clearScreen();
        drawLine();
        System.out.println("Welcome to Blackjack Deluxe!");
        enterToContinue();
        do {
            clearScreen();
            drawLine();
            System.out.println("Enter which option you'd like:");
            System.out.println("""
                    1: New Game with Default Rules
                    2: New Game with Custom Rules
                    Q: Quit""");
            response = myScanner.nextLine();
            switch (response){
                case "1" -> {defaultGame();}
                case "2" -> {customGame();}
                case "Q", "q" -> {
                    clearScreen();
                    drawLine();
                    System.out.println("Quitting the game!");
                    inMenu = false;
                }
                default -> {
                    System.out.println("Unrecognized input, try again!");
                    enterToContinue();
                }
            }
        } while(inMenu);
        System.out.println("End of program.");
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
            clearScreen();
            drawLine();
            System.out.println("Which rule set would you like to use?");
            System.out.println("(Enter a number and press enter)");
            System.out.println("1. Default Rules");
            System.out.println("2. Easy Rules");
            System.out.println("3. Hard Rules");
            System.out.println("0. Custom Rules");

            int selection;
            response = myScanner.nextLine();
            try {
                selection = Integer.parseInt(response);
                switch (selection){
                    case 1 -> {
                        System.out.println("Default rules selected!");
                        enterToContinue();
                        RulesBuilder.previewRules(RulesBuilder.buildDefaultRules());
                        awaitingInput = false;
                    }
                    case 2 -> {
                        System.out.println("Easy rules selected!");
                        RulesBuilder.previewRules(RulesBuilder.buildEZRules());
                        awaitingInput = false;
                    }
                    case 3 -> {
                        System.out.println("Hard rules selected!");
                        RulesBuilder.previewRules(RulesBuilder.buildHardRules());
                        awaitingInput = false;
                    }
                    case 0 -> {
                        System.out.println("Custom rules selected!");
                        RulesBuilder.previewRules(RulesBuilder.buildCustomRules());
                        awaitingInput = false;
                    }
                    default -> {
                        System.out.println("Invalid option! Try again.");
                        enterToContinue();
                    }
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid option! Try again!");
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
