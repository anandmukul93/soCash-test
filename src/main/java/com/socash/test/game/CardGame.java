package com.socash.test.game;

import com.socash.test.exception.CardNotFoundException;
import com.socash.test.exception.DuplicatePlayerException;
import com.socash.test.game.Deck.Card;
import com.socash.test.ruleset.CardGameRuleSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.logging.Logger;

//holds the execution time context
public class CardGame implements Game<CardGameContext, Player, String>{
    private CardGameContext cardGameContext;
    private Map<String, CardGameHand> activePlayerHands;
    private static final Logger LOGGER = Logger.getLogger(CardGame.class.getName());

    public CardGame(CardGameContext cardGameContext){
        this.cardGameContext = cardGameContext;
        this.activePlayerHands = new HashMap<>();
        for (Player player : cardGameContext.getPlayersList()) {
            activePlayerHands.put(player.getId(), new CardGameHand());
        }
    }

    @Override
    public GameContext getGameContext() {
        return cardGameContext;
    }

    @Override
    public void preGame(Supplier<String> messageSupplier) {
        //could have been save to DB com.socash.test.game details
        cardGameContext.getDealer().initialize();
        LOGGER.info("Players: \n");
        int count = 1;
        for (Player player: cardGameContext.getPlayersList()) {
            LOGGER.info(String.format("ID : %s, Name : %s", player.getId(), player.getName()));
            count++;
        }
    }

    public void inGame(Supplier<String> messageSupplier){
        if (messageSupplier != null)
            LOGGER.info(messageSupplier.get());
    }

    @Override
    public void postGame(Supplier<String> messageSupplier) {
        //could have been update results in DB
        if (messageSupplier != null) {
            LOGGER.info(messageSupplier.get());
        }
    }

    public void drawCard(int n) throws CardNotFoundException {
        int activePlayersCount = activePlayerHands.size();
        CardGameDealer cardGameDealer  = cardGameContext.getDealer();
        while(n > 0) {
                for (Entry<String, CardGameHand> playerHand : activePlayerHands.entrySet()) {
                    playerHand.getValue().addCard(cardGameDealer.getCard());
                }
            inGame(()-> {
                StringBuilder builder = new StringBuilder();
                for(Entry<String, CardGameHand> playerHand : activePlayerHands.entrySet()) {
                    builder.append(String.format("Player : %s and Cards : %s \n", playerHand.getKey(), playerHand.getValue().toString()));
                }
                return builder.toString();
            });
            n --;
        }
    }
    @Override
    public List<Player> getParticipants() {
        return cardGameContext.getPlayersList();
    }

    public Boolean hasWinner() {
        if(activePlayerHands.size() == 1) {
            storeWinnerData();
            return true;
        }
        filterWinnerCandidates();
        if (activePlayerHands.size() == 1) {
            storeWinnerData();
            return true;
        }
        return false;
    }

    public Player getWinner() {
        Entry<String, CardGameHand> winnerId = activePlayerHands.entrySet().stream().findFirst().get();
        return cardGameContext.getFromId(winnerId.getKey());
    }

    private void filterWinnerCandidates() {
        CardGameRuleSet cardGameRuleSet = cardGameContext.getRuleSet();
        Map<String, CardGameHand> modifiedActivePlayersHands = cardGameRuleSet.filterWinnerCandidates(activePlayerHands);
        if(modifiedActivePlayersHands.size() != activePlayerHands.size()){
            storeLoserCandidatesData(modifiedActivePlayersHands, activePlayerHands);
            activePlayerHands = modifiedActivePlayersHands;
        }
    }

    private void storeWinnerData(){
        Player player = getWinner();
        cardGameContext.storePlayerHand(player.getId(), activePlayerHands.get(player.getId()));
    }

    private void storeLoserCandidatesData(Map<String, CardGameHand> oldActive, Map<String, CardGameHand> newActive) {
        for (Entry<String, CardGameHand> playerNHand: oldActive.entrySet()) {
            if (!newActive.containsKey(playerNHand.getKey())) {
                cardGameContext.storePlayerHand(playerNHand.getKey(), playerNHand.getValue());
            }
        }
    }


    @Override
    public void addParticipant(Player player) throws DuplicatePlayerException {
        cardGameContext.addPlayer(player);
    }
}
