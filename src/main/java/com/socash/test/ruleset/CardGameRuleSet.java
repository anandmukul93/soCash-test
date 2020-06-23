package com.socash.test.ruleset;

import com.socash.test.game.CardGameHand;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

public class CardGameRuleSet {

    //returns the solo winner or winner candidates - winner is the one with biggest value hand (by type or faceoff criteria)
    public Map<String, CardGameHand> filterWinnerCandidates(Map<String,CardGameHand> playerHands) {
        Map<String, CardGameHand> filteredCandidates = new HashMap<>();
        if (playerHands.isEmpty()) {
            return filteredCandidates;
        }
        List<SimpleEntry<String, CardGameHand>> candidateHandTuples =  playerHands.entrySet().stream().map((entry) -> new SimpleEntry<>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        candidateHandTuples.sort(Comparator.comparing(SimpleEntry::getValue));
        Collections.reverse(candidateHandTuples);
        //filtering candidates who are possible winners
        filteredCandidates.put(candidateHandTuples.get(0).getKey(), candidateHandTuples.get(0).getValue());
        CardGameHand prevHand = candidateHandTuples.get(0).getValue();
        for (int i = 1; i < candidateHandTuples.size(); i++) {
            if (candidateHandTuples.get(i).getValue().compareTo(prevHand) != 0) {
                break;
            }
            filteredCandidates.put(candidateHandTuples.get(i).getKey(), candidateHandTuples.get(i).getValue());
        }
        return filteredCandidates;
    }
}
