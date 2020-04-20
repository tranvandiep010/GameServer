package com.model;

public class Item {
    private int id;
    private int type;
    private int TTL;

    public Item(int id, int type) {
        this.id = id;
        this.type = type;
        this.TTL = 120;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }
}