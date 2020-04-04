package com.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public final static int MAX_PEOPLE = 4;
    private int id;
    private List<Player> players = new ArrayList<>();
    private boolean isActive = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player findPlayer(String name){
        for (Player player:players) if (player.getName().equals(name)) return player;
        return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
