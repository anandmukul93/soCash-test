import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards;
    private static List<Card> defaultPack;
    private Integer start, end;
    static{
        for (Card.Rank rank : Card.Rank.values()){
            for (Card.Suite suite: Card.Suite.values()){
                Card card = new Card(rank, suite);
                defaultPack.add(card);
            }
        }
    }

    private Deck(List<Card> cards){
        this.cards = cards;
        this.start = 0;
        this.end = cards.size() - 1;
    }

    public static Deck newDeck() {
        int size = defaultPack.size();
        List<Card> cards = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            cards.add(i, defaultPack.get(i));
        }
        return new Deck(cards);
    }

    public void shuffle(){
        Random random = new Random();
        Card temp = null;
        for (int i = start; i < end; i++) {
            int swapIndex = i + random.nextInt(end - i);
            temp = cards.get(i);
            cards.add(i, cards.get(swapIndex));
            cards.add(swapIndex, temp);
        }
    }

    //takes out the top card
    public Card getTopCard() throws Exception{
        if (!isEmpty()){
            Card card = cards.get(0);
            start ++;
            return card;
        }
        throw new Exception("Deck Empty!! Cannot get Card from Deck");
    }

    public Boolean isEmpty(){
        return start >= end;
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
            ONE,
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
            KING
        }

        public enum Suite {
            HEARTS,
            SPADES,
            CLUBS,
            DIAMONDS
        }
    }
}

