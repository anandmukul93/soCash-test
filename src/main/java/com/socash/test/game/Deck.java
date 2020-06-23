package com.socash.test.game;

import com.socash.test.exception.CardNotFoundException;
import com.socash.test.game.CardValueStrategy.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Deck {

    private List<Card> cards;
    private static List<Card> defaultPack = new ArrayList<>();
    private Integer start, end;
    static{
        for (Card.Rank rank : Card.Rank.values()){
            for (Card.Suite suite: Card.Suite.values()){
                Card card = new Card(rank, suite);
                defaultPack.add(card);
            }
        }
        System.out.println(defaultPack.toString());
    }

    private Deck(List<Card> cards){
        this.cards = new ArrayList<>(cards);
        this.start = 0;
        this.end = cards.size() - 1;
    }

    public static Deck newDeck() {
        return new Deck(defaultPack);
    }

    public void shuffle(){
        Random random = new Random(System.currentTimeMillis());
        Card temp = null;
        for (int i = start; i < end; i++) {
            int swapIndex = i + random.nextInt(end - i);
            temp = cards.get(i);
            cards.set(i,cards.get(swapIndex));
            cards.set(swapIndex, temp);
        }
    }

    //takes out the top card
    public Card drawCard() throws CardNotFoundException{
        if (!isEmpty()){
            Card card = cards.get(start);
            start ++;
            return card;
        }
        throw new CardNotFoundException("Deck Empty!! Cannot get Card from Deck");
    }

    public Card drawCard(Supplier<Card> supplier) {
        return supplier.get();
    }

    public Boolean isEmpty(){
        return start > end;
    }

    public static class Card{
        private Rank rank;
        private Suite suite;
        private Card(Rank rank, Suite suite) {
            this.rank = rank;
            this.suite = suite;
        }
        public Rank getRank(){
            return this.rank;
        }
        public Suite getSuite(){
            return this.suite;
        }

        public enum Rank {
            ACE,
            TWO,
            THREE,
            FOUR,
            FIVE,
            SIX,
            SEVEN,
            EIGHT,
            NINE,
            TEN,
            JACK,
            QUEEN,
            KING;

            public Integer value(){
                return this.ordinal() + 1;
            }

            public String toString(){
                if (ordinal() < 10) {
                    return String.valueOf(ordinal() + 1);
                } else {
                    return String.valueOf(this.name().charAt(0));
                }
            }
        }

        public enum Suite {
            HEARTS("H"),
            SPADES("S"),
            CLUBS("C"),
            DIAMONDS("D");

            private String notation;

            Suite(String notation) {
                this.notation = notation;
            }

            public String toString(){
                return this.notation;
            }
        }

        public <T extends Comparable<T>> Value<T> valueWithStrategy(CardValueStrategy<T> strategy){
            return strategy.getValue(this);
        }

        public static Card from (String r, String s){
            Rank rank = null;
            Suite suite = null;
            for (Rank testRank : Rank.values()) {
                if (testRank.toString().equalsIgnoreCase(r)){
                    rank = testRank;
                    break;
                }
            }
            for (Suite testSuite : Suite.values()) {
                if (testSuite.toString().equalsIgnoreCase(s)) {
                   suite = testSuite;
                   break;
                }
            }
            if (rank != null && suite != null) {
                return new Card(rank, suite);
            }
            return null;
        }
    }
}


