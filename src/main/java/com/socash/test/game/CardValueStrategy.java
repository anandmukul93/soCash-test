package com.socash.test.game;


import com.socash.test.game.Deck.Card;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface CardValueStrategy<T extends Comparable<T>>{
    class Value<T> {
        private T value;

        public Value(T value) {
            this.value = value;
        }
        T value(){
            return value;
        }
    }

    Value<T> getValue(Card card);

    //Returns the first one found
    default SimpleEntry<Card, Value<T>> getMaxValueCardAmong(List<Card> cards){
        List<Card> duplicateList = new ArrayList<>(cards);
        Collections.sort(duplicateList, (Card c1, Card c2) -> {
            T v1 = getValue(c1).value();
            T v2 = getValue(c2).value();
            return v1.compareTo(v2) == -1 ? 1 : (v1.compareTo(v2) == 0 ? 0 : -1);
        });

        return new SimpleEntry<>(duplicateList.get(0), new Value(duplicateList.get(0).getRank().value()));
    }
}


class DefaultValueStrategy implements CardValueStrategy<Integer> {
    @Override
    public Value<Integer> getValue(Card card) {
        return new Value(card.getRank().value());
    }
}

class FaceOffValueStrategy implements CardValueStrategy<Integer> {
    @Override
    public Value<Integer> getValue(Card card) {
        if (card.getRank() == Card.Rank.ACE) {
            return new Value(Card.Rank.KING.value() + 1);
        }
        return new Value(card.getRank().value());
    }

}