package com.model;

public class Enermy {
    private int type;
    private Position position;
    private int health;
    private boolean isEnable;

    public Enermy(int type, Position position, int health, boolean isEnable) {
        this.type = type;
        this.position = position;
        this.health = health;
        this.isEnable = isEnable;
    }

    public Enermy() {
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
