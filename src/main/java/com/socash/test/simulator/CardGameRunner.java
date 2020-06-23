package com.socash.test.simulator;

import com.socash.test.exception.DuplicatePlayerException;
import com.socash.test.game.CardGameEngine;
import com.socash.test.game.GameContext;
import com.socash.test.game.CardGameContext;
import com.socash.test.game.Player;
import com.socash.test.ruleset.RuleSetFactory;

import java.util.List;
import java.util.logging.Logger;

public class CardGameRunner extends GameRunner {

    Logger LOGGER = Logger.getLogger(CardGameRunner.class.getName());
    @Override
    public void runGame(List<Player> playerList) {
        CardGameContext context = new CardGameContext(GameContext.GameConfigType.CARD_GAME_CONFIG, RuleSetFactory.getCardGameRuleSet());
        CardGameEngine cardGameEngine = new CardGameEngine();
        for (Player player :playerList) {
            try {
                context.addPlayer(player);
            } catch (DuplicatePlayerException e) {
                LOGGER.warning("Already added !! \n Unable to add a duplicate player : " + player.getName() + "   with id :  " + player.getId() );
                LOGGER.warning("Ignoring player addition to game!!");
            }
        }
        cardGameEngine.simulate(context);
    }
}
