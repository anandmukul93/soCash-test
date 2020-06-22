abstract class GameConfig {

    GameConfigType configType;

    public GameConfig(GameConfigType configType) {
        this.configType = configType;
    }

    enum GameConfigType{
        CARD_GAME_CONFIG;
    }
}


