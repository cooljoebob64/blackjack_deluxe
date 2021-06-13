package com.jlu.blackjack;

import java.util.List;

public interface Participant {

    /**
     * Gets the participant's name in String format
     * @return String representing the participant's name
     */
    String getName();

    /**
     * Check whether the participant is still active in the current round
     * @return boolean value that describes the active state
     */
    boolean isStillActive();

    /**
     * Get the participant's current (main) hand
     * @return a Hand object that is the main hand
     */
    Hand getCurrentHand();

    boolean isOnFirstAction();
    void setOnFirstAction(boolean onFirstAction);


}
