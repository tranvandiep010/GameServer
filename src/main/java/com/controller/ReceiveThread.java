package com.controller;

import com.model.Player;
import com.model.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ReceiveThread extends Thread {

    BlockingQueue<String> IQueue = null;
    List<Player> players = null;
    List<Socket> sockets = null;
    int ready = 0;
    Boolean isStart = false;

    public ReceiveThread(BlockingQueue<String> IQueue, List<Socket> sockets, List<Player> players) {
        this.IQueue = IQueue;
        this.sockets = sockets;
        this.players = players;
    }

    @Override
    public void run() {
        for (; ; ) {
            synchronized (sockets) {
                for (Socket socket : sockets) {
                    try {
                        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        if (reader.ready()) {
                            IQueue.put(reader.readLine());
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        IQueue.removeAll(null);
                    }
                }
            }
        }
    }

//    public synchronized void addPlayer(Socket socket, String name) {
//        Player player = new Player(name, true);
//        players.add(player);
//        System.out.println(name);
//        ready++;
//        if (ready >= 3) isStart = true;
//    }

    public int getNumPlayers() {
        return players.size();
    }
}
