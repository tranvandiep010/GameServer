package com.model;

public class Player {
    private String name;
    private boolean enable = false;
    private int score;
    private int plane;
    private Position position;
    private int health;
    private int gun;
    private String shield;

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
        this.health = Math.min(health, 100);
    }

    public int getGun() {
        return gun;
    }

    public void setGun(int gun) {
        this.gun = gun;
    }

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public Player(String name, boolean enable, int score, int plane, Position position, int health, int gun) {
        this.name = name;
        this.enable = enable;
        this.score = score;
        this.plane = plane;
        this.position = position;
        this.health = health;
        this.gun = gun;
        this.shield = "";
    }
}
