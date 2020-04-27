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
    List<Player> players = new ArrayList<>();
    BufferedReader[] readers = new BufferedReader[3];
    int ready = 0;

    public ReceiveThread(BlockingQueue<String> IQueue) {
        this.IQueue = IQueue;
    }

    @Override
    public void run() {
        for (; ; ) {
            for (int i = 0; i < ready; ++i) {
                try {
                    if (readers[i].ready()) {
                        IQueue.put(readers[i].readLine());
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    IQueue.removeAll(null);
                }
            }
//            try {
//                Thread.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public synchronized void addPlayer(Socket socket, String name) {
        Player player = new Player(name, true);
        players.add(player);
        System.out.println(name);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        readers[ready] = new BufferedReader(inputStreamReader);
        ready++;
    }

    public int getNumPlayers() {
        return players.size();
    }
}
