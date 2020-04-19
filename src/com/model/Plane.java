package com.model;

public class Plane {
    private int type;
    private Position position;
    boolean isEnable;
    private int numOfGun;
    private int shield;

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

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getNumOfGun() {
        return numOfGun;
    }

    public void setNumOfGun(int numOfGun) {
        this.numOfGun = numOfGun;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }
}
