package com.model;

public class Bullet {
    private int type;
    private Position position;
    private String owner;
    private int TTL;

    public Bullet(int type, Position position, String owner) {
        this.type = type;
        this.position = position;
        this.owner = owner;
        this.TTL = 120;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }
}
