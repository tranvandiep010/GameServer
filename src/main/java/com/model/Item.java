package com.model;

public class Item {
    private int id;
    private int type;
    private int status;

    public Item(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public Item(int id, int type, int status) {
        this.id = id;
        this.type = type;
        this.status = status;
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
}