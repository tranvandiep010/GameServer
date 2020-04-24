package com.model;

public class Enermy {
    private long id;
    private int type;
    private Plane plane;
    private int enable;

    public Enermy(long id, int type, int enable) {
        this.id = id;
        this.type = type;
        this.enable = enable;
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

    public int isEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
