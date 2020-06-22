import java.util.List;

public interface Game<T extends GameConfig> {
    GameConfig getGameConfig();

    void setGameConfig(GameConfig gameConfig);

    void preGame();

    void postGame();

    List<Player> getPlayers();

    void addPlayer(Player player);

}
