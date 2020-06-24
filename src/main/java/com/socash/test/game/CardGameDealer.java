package com.socash.test.game;


import com.socash.test.exception.CardNotFoundException;
import com.socash.test.game.Deck.Card;


public class CardGameDealer {
    private Deck deck;

    public CardGameDealer(Deck deck) {
        this.deck = deck;
    }

    public Card getCard() throws CardNotFoundException {
        return deck.drawCard();
    }

    public void initialize(){
        //no implementation
        deck.shuffle();
    }
}
