package com.controller;

import com.model.Player;

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
    List<BufferedReader> readers = new ArrayList<>();
    int ready = 0;
    Boolean isStart = false;

    public ReceiveThread(BlockingQueue<String> IQueue) {
        this.IQueue = IQueue;
    }

    @Override
    public void run() {
        for (; ; ) {
            synchronized (readers) {
                for (BufferedReader reader : readers) {
                    try {
                        if (reader.ready()) {
                            IQueue.put(reader.readLine());
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void addPlayer(Socket socket, String name) {
        Player player = new Player(name, true);
        players.add(player);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (readers) {
            readers.add(new BufferedReader(inputStreamReader));
        }
        ready++;
        if (ready >= 3) isStart = true;
    }

    public void removePlayer(int index) {
        players.remove(index);
        synchronized (readers) {
            readers.remove(index);
        }
    }
}
