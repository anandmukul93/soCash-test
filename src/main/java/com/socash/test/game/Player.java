package com.socash.test.game;

public class Player implements BasicParticipant {
    private String name;
    private String id;
    public Player(String name, String id){
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
