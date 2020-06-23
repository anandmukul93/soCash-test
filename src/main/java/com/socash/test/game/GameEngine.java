package com.socash.test.game;

public interface GameEngine<T extends GameContext> {
    void simulate(T gameContext) ;
}
