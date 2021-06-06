package com.jlu.blackjack;

import java.util.List;

public interface Participant {

    String getName();
    boolean isStillActive();
    Hand getCurrentHand();
    Hand getSplitHand();
    void getAction();

}
