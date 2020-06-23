package com.socash.test.game;

import com.socash.test.exception.DuplicatePlayerException;

import java.util.List;
import java.util.function.Supplier;

public interface Game<T extends GameContext, P extends BasicParticipant, M> {
    GameContext getGameContext();

    void preGame(Supplier<M> supplier);

    void postGame(Supplier<M> supplier);

    List<P> getParticipants();

    void addParticipant(P participant) throws DuplicatePlayerException;
}
