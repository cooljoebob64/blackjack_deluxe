package com.jlu.blackjack;

import java.util.List;

public class Rules {

    private List<Object> rules;
    private boolean insurance; // Is insurance offered?
    private double insurancePayout; // Insurance payout rate?
    private boolean evenMoney; // Is Even Money offered?
    private int decksToLoad; // How many decks to load into the shoe when needed?
    private boolean clearShoeOnLoad; // Should the shoe be emptied first when reloaded?
    private int pushRule; // No action, player win, player loss?
    private boolean dealerHitsSoft17; // Does the dealer hit on a soft 17?
    private int blackjackPayout; // Blackjack payout - 3:2, 2:1, custom?
    private double minBet; // Minimum starting bet?

    /**
     * Empty constructor, employs most common default rules
     */
    public Rules(){
    }

    /**
     * Provides a list of current rules in String format
     * @return A String representing the list of rules that are currently in force
     */
    public String listRules(){
        return "";
    }

}
