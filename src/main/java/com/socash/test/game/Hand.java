package com.socash.test.game;


import com.socash.test.game.Deck.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private List<Card> cards = new ArrayList<>();
    private Integer totalCards = 0;

    public List<Card> showHand() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        totalCards++;
    }

    public void addCards(List<Card> cards) {
        for (Card card : cards) {
            cards.add(card);
            totalCards++;
        }
    }
    public Integer getCardCount() {
        return totalCards;
    }

    @Override
    public String toString() {
        List<String> strings = new ArrayList<>(cards.size());
        for (Card card : cards) {
            strings.add(card.getSuite().toString() + card.getRank().toString());
        }
        return String.join(", ", strings);
    }
}