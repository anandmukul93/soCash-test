import java.util.List;

public class CardGame implements Game{
    CardGameConfig cardGameConfig;
    @Override
    public GameConfig getGameConfig() {
        return cardGameConfig;
    }

    @Override
    public void setGameConfig(GameConfig gameConfig) {

    }

    @Override
    public void preGame() {

    }

    @Override
    public void postGame() {

    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public void addPlayer(Player player) {

    }
}
