package com.controller;

import com.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class IOThread extends Thread {

    BlockingQueue<String> IOQueue = null;
    List<Player> players = new ArrayList<>();
    List<BufferedReader> readers = new ArrayList<>();
    int ready = 0;

    public IOThread(BlockingQueue<String> IOQueue) {
        this.IOQueue = IOQueue;
    }

    @Override
    public void run() {
        while (ready < 3) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (; ; ) {
            for (BufferedReader reader : readers) {
                try {
                    if (reader.ready()) {
                        IOQueue.put(reader.readLine());
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    IOQueue.removeAll(null);
                }
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlayer(Socket socket, String name) {
        ready++;
        Player player = new Player(name, true);
        players.add(player);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        readers.add(new BufferedReader(inputStreamReader));
    }

    public int getNumPlayers() {
        return players.size();
    }
}
