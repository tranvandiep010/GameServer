package com.model;

public class Player {
    private int id;
    private String name;
    private String ipAddress;
    private String port;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(int id, String name, String ipAddress, String port) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


}
