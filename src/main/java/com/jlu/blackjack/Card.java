package com.jlu.blackjack;

public enum Card {
    H2 (2, "Two of Hearts"),
    H3 (3, "Three of Hearts"),
    H4 (4, "Four of Hearts"),
    H5 (5, "Five of Hearts"),
    H6 (6, "Six of Hearts"),
    H7 (7, "Seven of Hearts"),
    H8 (8, "Eight of Hearts"),
    H9 (9, "Nine of Hearts"),
    H10 (10, "Ten of Hearts"),
    HJ (10, "Jack of Hearts"),
    HQ (10, "Queen of Hearts"),
    HK (10, "King of Hearts"),
    HA (11, "Ace of Hearts"),
    C2 (2, "Two of Clubs"),
    C3 (3, "Three of Clubs"),
    C4 (4, "Four of Clubs"),
    C5 (5, "Five of Clubs"),
    C6 (6, "Six of Clubs"),
    C7 (7, "Seven of Clubs"),
    C8 (8, "Eight of Clubs"),
    C9 (9, "Nine of Clubs"),
    C10 (10, "Ten of Clubs"),
    CJ (10, "Jack of Clubs"),
    CQ (10, "Queen of Clubs"),
    CK (10, "King of Clubs"),
    CA (11, "Ace of Clubs"),
    D2 (2, "Two of Diamonds"),
    D3 (3, "Three of Diamonds"),
    D4 (4, "Four of Diamonds"),
    D5 (5, "Five of Diamonds"),
    D6 (6, "Six of Diamonds"),
    D7 (7, "Seven of Diamonds"),
    D8 (8, "Eight of Diamonds"),
    D9 (9, "Nine of Diamonds"),
    D10 (10, "Ten of Diamonds"),
    DJ (10, "Jack of Diamonds"),
    DQ (10, "Queen of Diamonds"),
    DK (10, "King of Diamonds"),
    DA (11, "Ace of Diamonds"),
    S2 (2, "Two of Spades"),
    S3 (3, "Three of Spades"),
    S4 (4, "Four of Spades"),
    S5 (5, "Five of Spades"),
    S6 (6, "Six of Spades"),
    S7 (7, "Seven of Spades"),
    S8 (8, "Eight of Spades"),
    S9 (9, "Nine of Spades"),
    S10 (10, "Ten of Spades"),
    SJ (10, "Jack of Spades"),
    SQ (10, "Queen of Spades"),
    SK (10, "King of Spades"),
    SA (11, "Ace of Spades");

    private final int handValue;
    private final String cardName;
    Card(int handValue,  String cardName){
        this.handValue=handValue;
        this.cardName = cardName;
    }

    int handValue(){
        return handValue;
    }
    String cardName(){
        return cardName;
    }
}
