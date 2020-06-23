package com.socash.test.simulator;

import com.socash.test.game.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GameRunner {

    public abstract void runGame(List<Player> playerList);

    public static void main(String[] args){
        Player player1 = new Player("P1", "xyz");
        Player player2 = new Player("P2", "abc");
        Player player3 = new Player("P3", "pqr");
        Player player4 = new Player("P4", "fgh");

        GameRunner gameRunner = new CardGameRunner();
        gameRunner.runGame(new ArrayList<>(Arrays.asList(player1, player2, player3, player4)));
    }
}
