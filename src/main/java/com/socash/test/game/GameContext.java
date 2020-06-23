package com.socash.test.game;

public abstract class GameContext {

    GameConfigType configType;

    public GameContext(GameConfigType configType) {
        this.configType = configType;
    }

    public enum GameConfigType{
        CARD_GAME_CONFIG;
    }
}


