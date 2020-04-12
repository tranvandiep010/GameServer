package com.controller;

import com.model.Player;
import com.model.Room;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RoomThread extends Thread {

    private String START_CODE = "";
    private String END_GAME_CODE = "";
    private String MOVE_CODE = "";
    private Room room;
    public static int numOfPlayer = 0;
    List<Socket> sockets = new ArrayList<>();
    private int flag = 1;
    private boolean AC = false;
    private BlockingQueue<String> queue = null;

    private int ready = 0;

    public RoomThread(int id,BlockingQueue<String> queue) {
        this.queue=queue;
        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
            START_CODE = p.getProperty("START_CODE");
            END_GAME_CODE = p.getProperty("END_GAME_CODE");
            MOVE_CODE = p.getProperty("MOVE_CODE");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        room = new Room();
        room.setId(id);
    }

    @Override
    public void run() {
        while (flag == 1) {
            try {
                String data = queue.take();
                String CODE = data.substring(0, data.indexOf('|'));
                String DATA = data.substring(data.indexOf('|') + 1);
                if (CODE.equals(START_CODE)) {
                    Player player = room.findPlayer(data.substring(data.indexOf('|') + 1));
                    if (!player.isReady()) {
                        broadcast(START_CODE + "|" + DATA + "\n");
                        player.setReady(true);
                        ready++;
                    }
                } else if (CODE.equals(END_GAME_CODE)) {
                    Player player = room.findPlayer(DATA);
                    player.setReady(false);
                    ready--;
                } else if (CODE.equals(MOVE_CODE)) {
                    broadcast(data + "\n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //if (AC == true && ready == 0) endGame();
            if (ready == 3) startGame();
        }
    }

    public void addPlayer(Socket socket, Player player) {
        numOfPlayer++;
        room.addPlayer(player);
        sockets.add(socket);
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    private void startGame() {
        AC = true;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String data){
        try {
            System.out.println("Test"+data);
            for (Socket socket : sockets) {
                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                outToClient.writeBytes(data);
            }
        } catch (IOException e) {
            System.out.println("RoomThread" + e.getMessage());
        }
    }

    private void endGame() {
        this.flag = 0;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }
}