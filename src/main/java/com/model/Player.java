package com.model;

import java.net.Socket;

public class Player {
    private String name;
    private boolean enable=false;
    private int score;
    private Plane plane;
    private Item item;

    public Player(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean enable() {
        return enable;
    }

    public void setReady(boolean ready) {
        enable = ready;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
