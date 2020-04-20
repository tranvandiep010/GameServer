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
    List<BufferedReader> readers=new ArrayList<>();

    public IOThread(BlockingQueue<String> IOQueue) {
        this.IOQueue = IOQueue;
    }

    @Override
    public void run() {
        for (; ; ) {
            for (BufferedReader reader:readers){
                try {
                    if (reader.ready()){
                        IOQueue.put(reader.readLine());
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    IOQueue.removeAll(null);
                }
            }
        }
    }

    public void addPlayer(Socket socket, String name) {
        Player player = new Player(name, socket, true);
        players.add(player);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        readers.add(new BufferedReader(inputStreamReader));
    }

    public int getNumPlayers(){
        return players.size();
    }
}
