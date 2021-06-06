package com.jlu.blackjack;

import java.util.List;

public class Rules {

    public enum pushRuleOptions{
        NO_ACTION, PLAYER_WIN, PLAYER_LOSS
    }

    // Rule settings fields
    private boolean insurance; // Is insurance offered?
    private double insurancePayout; // Insurance payout rate?
    private boolean evenMoney; // Is Even Money offered?

    public double getPenetration() {
        return penetration;
    }

    public void setPenetration(double penetration) {
        this.penetration = penetration;
    }

    private double penetration; // How far into the shoe before reloading?
    private int decksToLoad; // How many decks to load into the shoe when needed?
    private boolean clearShoeOnLoad; // Should the shoe be emptied first when reloaded?
    private int pushRule; // No action, player win, player loss?
    private boolean dealerHitsSoft17; // Does the dealer hit on a soft 17?
    private double blackjackPayout; // Blackjack payout - 3:2, 2:1, custom?
    private double minBet; // Minimum starting bet?

    /**
     * Empty constructor, employs most common default rules
     */
    public Rules(){
        setInsurance(true);
        setInsurancePayout(2.0);
        setEvenMoney(true);
        setPenetration(0.75);
        setDecksToLoad(2);
        setClearShoeOnLoad(true);
        setPushRule(0);
        setDealerHitsSoft17(true);
        setBlackjackPayout(1.5);
        setMinBet(5.00);
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public double getInsurancePayout() {
        return insurancePayout;
    }

    public void setInsurancePayout(double insurancePayout) {
        this.insurancePayout = insurancePayout;
    }

    public boolean isEvenMoney() {
        return evenMoney;
    }

    public void setEvenMoney(boolean evenMoney) {
        this.evenMoney = evenMoney;
    }

    public int getDecksToLoad() {
        return decksToLoad;
    }

    public void setDecksToLoad(int decksToLoad) {
        this.decksToLoad = decksToLoad;
    }

    public boolean isClearShoeOnLoad() {
        return clearShoeOnLoad;
    }

    public void setClearShoeOnLoad(boolean clearShoeOnLoad) {
        this.clearShoeOnLoad = clearShoeOnLoad;
    }

    public int getPushRule() {
        return pushRule;
    }

    public void setPushRule(int pushRule) {
        this.pushRule = pushRule;
    }

    public boolean isDealerHitsSoft17() {
        return dealerHitsSoft17;
    }

    public void setDealerHitsSoft17(boolean dealerHitsSoft17) {
        this.dealerHitsSoft17 = dealerHitsSoft17;
    }

    public double getBlackjackPayout() {
        return blackjackPayout;
    }

    public void setBlackjackPayout(double blackjackPayout) {
        this.blackjackPayout = blackjackPayout;
    }

    public double getMinBet() {
        return minBet;
    }

    public void setMinBet(double minBet) {
        this.minBet = minBet;
    }



}
