package com.controller;

import com.model.Player;

import java.net.Socket;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TaskThread extends Thread {

    BlockingQueue<String> IOQueue = null;
    List<Player> players = new ArrayList<>();

    public TaskThread(BlockingQueue<String> IOQueue) {
        this.IOQueue = IOQueue;
    }

    @Override
    public void run() {
        Clock clock = Clock.systemDefaultZone();
        long cycle = clock.millis();
        for (; ; ) {
            try {
                String data = IOQueue.poll(5, TimeUnit.MILLISECONDS);
                long curr = clock.millis();
                if (curr - cycle >= 50) {
                    sendData();
                    //empty queue
                    cycle = curr;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlayer(Socket socket, String name) {
        Player player = new Player(name, socket, true);
        players.add(player);
    }

    public void handle(String message) {
        String[] data = message.split("\\|");
        if (data.length>0){
            if (data[0].equals("MOVE")){

            }else if (data[0].equals("START")){

            }
        }
    }

    public void sendData() {

    }
}
