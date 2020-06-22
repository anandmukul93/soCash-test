public interface GameSimulator<T extends GameConfig> {
    void simulate(T gameConfig) ;
}
