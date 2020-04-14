package com.model;

import java.net.Socket;

public class Player {
    private String name;
    private Socket socket;
    private boolean isReady=false;
    private int position;

    public Player(String name, Socket socket, boolean isReady) {
        this.name = name;
        this.socket = socket;
        this.isReady = isReady;
    }

    public Player() {
    }
}
