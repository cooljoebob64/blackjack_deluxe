package com.jlu.blackjack.test;

import com.jlu.blackjack.Card;
import com.jlu.blackjack.Deck;
import com.jlu.blackjack.Rules;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.De;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckShoe  {

    Deck testDeck;
    Rules testRules;

    @Given("^there is a new deck that has just been created$")
    public void thereIsANewDeckThatHasJustBeenCreated() {
        testDeck = new Deck(new Rules());
    }

    @When("^we have rules that require at least one deck to be loaded$")
    public void weHaveRulesThatRequireAtLeastOneDeckToBeLoaded() {
        testRules = new Rules();
        testRules.setDecksToLoad(2);
        testDeck = new Deck(testRules);
    }

    @Then("^you can request a card to be dealt and will receive a Card object$")
    public void youCanRequestACardToBeDealtAndWillReceiveACardObject() {
        assertAll("Verify all conditions for a new Deck dealing a card",
                ()-> assertEquals(testDeck.dealCard().getClass(), Card.C2.getClass())
                );
    }
}
