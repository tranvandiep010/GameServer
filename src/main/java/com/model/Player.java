package com.model;

import java.net.Socket;

public class Player {
    private String name;
    private boolean enable=false;
    private int score;
    private int plane;
    private Position position;
    private int health;
    private int shield;
    private Item item;

    public Player(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public Player() {
    }


    public Player(String name, boolean enable, int score, int plane, Position position, int health, int shield, Item item) {
        this.name = name;
        this.enable = enable;
        this.score = score;
        this.plane = plane;
        this.position = position;
        this.health = health;
        this.shield = shield;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
