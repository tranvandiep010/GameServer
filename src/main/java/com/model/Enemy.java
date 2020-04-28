package com.model;

public class Enemy {
    private long id;
    private int plane;
    private Position position;

    public Enemy(long id, int plane, Position position) {
        this.id = id;
        this.plane = plane;
        this.position = position;
    }

    public Enemy(long id) {
        this.id = id;
    }

    public Enemy() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
