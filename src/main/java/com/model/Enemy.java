package com.model;

public class Enemy {
    private long id;
    private int plane;
    private int health;
    private int enable;

    public Enemy(long id, int plane, int enable) {
        this.id = id;
        this.plane = plane;
        this.enable = enable;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
