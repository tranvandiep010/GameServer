package com.model;

public class Enermy {
    private long id;
    private int type;
    private Plane plane;
    private boolean enable;
    private int TTL;

    public Enermy(int type, Plane plane, boolean enable) {
        this.type = type;
        this.plane = plane;
        this.enable = enable;
        this.TTL=120;
    }

    public Enermy() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }
}
