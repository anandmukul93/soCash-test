package com.socash.test.game;

import com.socash.test.game.CardValueStrategy.Value;
import com.socash.test.game.Deck.Card;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardGameHand extends Hand implements Comparable<CardGameHand>{

    @Override
    public int compareTo(CardGameHand o2) {
        if (this.getCardCount() < o2.getCardCount()) {
            return -1;
        } else if (this.getCardCount() > o2.getCardCount()) {
            return 1;
        }

        SimpleEntry<Integer, Integer> handPriority1 = HandType.getHandPriority(this);
        SimpleEntry<Integer, Integer> handPriority2 = HandType.getHandPriority(o2);
        Integer handTypePriority1 = handPriority1.getKey();
        Integer handTypePriority2 = handPriority2.getKey();
        if (handTypePriority1 < handTypePriority2 || handTypePriority1 > handTypePriority2){
            return handTypePriority1.compareTo(handTypePriority2);
        }

        Integer maxFaceOffValue1 = handPriority1.getValue();
        Integer maxFaceOffValue2 = handPriority2.getValue();
        return maxFaceOffValue1.compareTo(maxFaceOffValue2);
    }

    public SimpleEntry<Card, Value<Integer>> getMaxValueCardWithStrategy(CardValueStrategy<Integer> strategy){
        return strategy.getMaxValueCardAmong(showHand());
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    private interface PriorityValueGenerator{
        SimpleEntry<Integer, Integer> getPriority(CardGameHand hand);
    }

    //SimpleEntry used instead of Pair or Tuple
    private enum HandType implements PriorityValueGenerator{
        TRAIL(5){
            @Override
            Boolean typeOf(CardGameHand hand) {
                Boolean areThreeCards = hand.getCardCount() == BASIC_HAND_CARD_COUNT;
                if (!areThreeCards) {
                    return false;
                }

                List<Card> cardsInHand = hand.showHand();
                Boolean allSame = cardsInHand.get(0).getRank() == cardsInHand.get(1).getRank();
                return allSame && (cardsInHand.get(1).getRank() == cardsInHand.get(2).getRank());
            }
        },
        SEQUENCE(4){
            @Override
            Boolean typeOf(CardGameHand hand) {
                Boolean areThreeCards = hand.getCardCount() == BASIC_HAND_CARD_COUNT;
                if (!areThreeCards) {
                    return false;
                }

                List<Card> cardsInHand = new ArrayList<>(hand.showHand());

                Collections.sort(cardsInHand, (Card c1, Card c2) -> {
                    int valueCard1 = c1.valueWithStrategy(defaultValueStrategy).value();
                    int valueCard2 = c2.valueWithStrategy(defaultValueStrategy).value();
                    return valueCard1 < valueCard2 ? -1 : (valueCard1 == valueCard2 ? 0 : 1);
                });
                int valueCard1 = cardsInHand.get(0).valueWithStrategy(defaultValueStrategy).value();
                int valueCard2 = cardsInHand.get(1).valueWithStrategy(defaultValueStrategy).value();
                int valueCard3 = cardsInHand.get(2).valueWithStrategy(defaultValueStrategy).value();

                Boolean isSequence = (valueCard1 == (valueCard2 - 1)) && (valueCard1 == (valueCard3 - 2));

                return isSequence;
            }
        },
        PAIR(3){
            @Override
            Boolean typeOf(CardGameHand hand) {
                Boolean areThreeCards = hand.getCardCount() == BASIC_HAND_CARD_COUNT;
                if (!areThreeCards) {
                    return false;
                }

                List<Card> cardsInHand = hand.showHand();
                Boolean onlyTwoSame = (cardsInHand.get(0).getRank() == cardsInHand.get(1).getRank() && cardsInHand.get(0).getRank() != cardsInHand.get(2).getRank()) ||
                        (cardsInHand.get(1).getRank() == cardsInHand.get(2).getRank() && cardsInHand.get(1).getRank() != cardsInHand.get(0).getRank()) ||
                        (cardsInHand.get(3).getRank() == cardsInHand.get(0).getRank() && cardsInHand.get(3).getRank() != cardsInHand.get(1).getRank());

                return areThreeCards && onlyTwoSame;
            }
        },
        TOP_CARD(2){
            @Override
            Boolean typeOf(CardGameHand hand) {
                Boolean areThreeCards = hand.getCardCount() == BASIC_HAND_CARD_COUNT;
                if (!areThreeCards) {
                    return false;
                }

                Boolean isOtherCardType = false;
                for (HandType handType : HandType.values()) {
                    if (handType == this) {
                        continue;
                    }
                    isOtherCardType = isOtherCardType || handType.typeOf(hand);
                }
                return areThreeCards && !isOtherCardType;
            }
        },
        FACE_OFF(1){
            @Override
            public SimpleEntry<Integer, Integer> getPriority(CardGameHand hand) {
                List<Card> cards = hand.showHand();
                List<Card> validFaceOffHand = new ArrayList<>(Arrays.asList(cards.get(cards.size() - 1)));
                CardGameHand cardGameHand = new CardGameHand();
                cardGameHand.addCards(validFaceOffHand);
                return new SimpleEntry<>(getPriority(), cardGameHand.getMaxValueCardWithStrategy(faceOffValueStrategy).getValue().value());
            }

            @Override
            Boolean typeOf(CardGameHand hand) {
                Boolean areThreeCard = hand.getCardCount() == BASIC_HAND_CARD_COUNT;
                return !areThreeCard;
            }
        };
        private static Integer BASIC_HAND_CARD_COUNT = 3;
        private static DefaultValueStrategy defaultValueStrategy = new DefaultValueStrategy();
        private static FaceOffValueStrategy faceOffValueStrategy = new FaceOffValueStrategy();
        private Integer priority;

        HandType(int priority) {
            this.priority = priority;
        }

        protected Integer getPriority() {
            return this.priority;
        }

        abstract Boolean typeOf(CardGameHand hand);

        public SimpleEntry<Integer, Integer> getPriority(CardGameHand hand){
            return new SimpleEntry<>(getPriority(), hand.getMaxValueCardWithStrategy(defaultValueStrategy).getValue().value());
        }

        private static SimpleEntry<Integer, Integer> getHandPriority(CardGameHand hand) {
            for (HandType handType : HandType.values()) {
                if (handType.typeOf(hand)) {
                    return handType.getPriority(hand);
                }
            }
            return hand.getCardCount() == BASIC_HAND_CARD_COUNT ? HandType.TOP_CARD.getPriority(hand) : HandType.FACE_OFF.getPriority(hand);
        }


        public static HandType of(CardGameHand hand) {
            for (HandType handType : HandType.values()) {
                if (handType.typeOf(hand)) {
                    return handType;
                }
            }
            return null;
        }
    }

}
