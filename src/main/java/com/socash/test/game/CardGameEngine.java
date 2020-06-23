package com.socash.test.game;

import com.socash.test.exception.CardNotFoundException;

import java.util.logging.Logger;

public class CardGameEngine implements GameEngine<CardGameContext> {
    Logger LOGGER = Logger.getLogger(CardGameEngine.class.getName());

    @Override
    public void simulate(CardGameContext gameContext) {
        CardGame game = new CardGame(gameContext);
        //TODO iterate card draw till find winner
        game.preGame(null);
        //first 3 rounds draw cards
        LOGGER.info("Drawing 3 cards in the beginning for all..");
        try {
            game.drawCard(3);
        } catch (CardNotFoundException e) {
            game.postGame(() -> "Cannot decide winner !! First round cards exhausted..");
            return;
        }

        //continue drawing until winner or cards exhausted
        while (!game.hasWinner()) {
            LOGGER.info("Drawing more cards for deciding winner in case of face-off");
            try {
                game.drawCard(1);
            } catch (CardNotFoundException e) {
                game.postGame(() -> "Cannot decide winner !! Cards exhausted..");
                return;
            }
        }

        if (game.hasWinner()) {
            Player winner = game.getWinner();
            game.postGame(() -> {
                String message = "Winner is : \n \n " + "Id : " + winner.getId() + " Hand : " + gameContext.getPlayerHand(winner.getId()).toString();
                return message;
            });
        }
    }
}
