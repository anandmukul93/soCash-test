package com.socash.test.game;

import com.socash.test.exception.DuplicatePlayerException;
import com.socash.test.ruleset.CardGameRuleSet;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

//holds overall game context like players, ruleset, other permanent post game states as well.
public class CardGameContext extends GameContext {
    private CardGameRuleSet cardGameRuleSet;
    private List<Player> playersList;
    private Map<String, SimpleEntry<Player, CardGameHand>> playerHand;
    private CardGameDealer dealer;

    public CardGameContext(GameConfigType configType, CardGameRuleSet cardGameRuleSet, CardGameDealer cardSupplier) {
        super(configType);
        this.cardGameRuleSet = cardGameRuleSet;
        this.dealer = new CardGameDealer(Deck.newDeck());
        this.playersList = new ArrayList<>();
        this.playerHand = new HashMap<>();
    }

    CardGameRuleSet getRuleSet() {
        return cardGameRuleSet ;
    }

    public void addPlayer(Player player) throws DuplicatePlayerException {
        //Can check if player already added
        Boolean playerExisting = playersList.stream().filter((playerIter) -> playerIter.getId().equalsIgnoreCase(player.getId())).findFirst().isPresent();
        if (playerExisting)
            throw new DuplicatePlayerException("Cannot add duplicate Player");
        this.playersList.add(player);
    }

    public List<Player> getPlayersList() {
        return this.playersList;
    }

    public void storePlayerHand(String id, CardGameHand hand) {
        if (!playerHand.containsKey(id)) {
            playerHand.put(id,
                    new SimpleEntry<>(playersList.stream()
                            .filter((player) -> player.getId().equalsIgnoreCase(id))
                            .findFirst()
                            .get(), hand));
        }
    }

    public CardGameHand getPlayerHand(String id) {
        if(playerHand.containsKey(id)){
            return playerHand.get(id).getValue();
        }
        return new CardGameHand();
    }
    public Player getFromId(String id) {
        return playersList.stream().filter((player) -> player.getId().equalsIgnoreCase(id)).findFirst().get();
    }

    public CardGameDealer getDealer() {
        return this.dealer;
    }
}
