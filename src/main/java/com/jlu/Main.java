package com.jlu;

import com.jlu.blackjack.Game;
import com.jlu.blackjack.Rules;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static Scanner myScanner;

    /**
     * Main class which starts the game and interacts with the user.
     * @param args Arguments provided at run time
     */
    public static void main(String[] args){

        myScanner = new Scanner(System.in);
        String response = "";
        boolean inMenu = true;

        drawLine();
        System.out.println("Welcome to Blackjack Deluxe!");
        enterToContinue();
        do {
            drawLine();
            System.out.println("Enter which option you'd like:");
            System.out.println("""
                    1: New Game with Default Rules
                    2: New Game with Custom Rules
                    Q: Quit""");
            response = myScanner.nextLine();
            drawLine();

            switch (response){
                case "1" -> {defaultGame();}
                case "2" -> {customGame();}
                case "Q", "q" -> {
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

    private static void drawLine(){
        // Provides a re-usable way to draw a graphical line in the console
        System.out.println("================================================");
    }

    private static void enterToContinue(){
        // Prompts the user to press enter before continuing
        System.out.println("Press Enter to continue...");
        myScanner.nextLine();
    }

    private static void clearScreen(){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
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
            System.out.println("0. Custom Rules");

            int selection;
            response = myScanner.nextLine();
            try {
                selection = Integer.parseInt(response);
                switch (selection){
                    case 1 -> {
                        System.out.println("Default rules selected!");
                        showDefaultRules();
                        awaitingInput = false;
                    }
                    case 0 -> {
                        System.out.println("Custom rules selected!");
                        buildCustomRules();
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

    private static void showDefaultRules(){
        clearScreen();
        drawLine();
        Rules defaultRules = new Rules();
        previewRules(defaultRules);
    }

    private static void buildCustomRules(){
        clearScreen();
        drawLine();
        System.out.println("We will build some rules here soon!");
        enterToContinue();
        Rules customRules = new Rules();
        previewRules(customRules);
    }

    private static void previewRules(Rules potentialRules){

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
