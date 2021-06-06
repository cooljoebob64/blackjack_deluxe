package com.jlu;

import com.jlu.blackjack.Game;

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

    private static void defaultGame() {
        // Launch the game with default rules
        System.out.println("Starting a new game with default rules!");
        new Game();
    }
    private static void customGame() {
        // Provide the user with some options about how to customize rules for the game
        System.out.println("Custom rules are not yet implemented, sorry!");
        defaultGame();
    }
}
