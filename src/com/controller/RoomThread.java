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
    private int numOfPlayer;
    List<Socket> sockets = new ArrayList<>();
    private int flag = 1;
    private boolean active = false;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

    private int ready = 0;

    public RoomThread(int id) {
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
                if (data.substring(0, 2).equals(START_CODE)) {
                    Player player = room.findPlayer(data.substring(2));
                    if (!player.isReady()) {
                        try {
                            for (Socket socket : sockets) {
                                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                                outToClient.writeBytes(START_CODE + "|" + data.substring(2)+"\n");
                            }
                        } catch (IOException e) {
                            System.out.println("RoomThread" + e.getMessage());
                        }
                        player.setReady(true);
                        ready++;
                    }
                } else if (data.substring(0, 2).equals(END_GAME_CODE)) {
                    Player player = room.findPlayer(data.substring(2));
                    player.setReady(false);
                    ready--;
                } else if (data.substring(0, 2).equals(MOVE_CODE)) {
                    try {
                        for (Socket socket : sockets) {
                            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                            outToClient.writeBytes(data+"\n");
                        }
                    } catch (IOException e) {
                        System.out.println("RoomThread" + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (active == true && ready == 0) endGame();
            if (ready == 3) startGame();
        }
    }

    public void addPlayer(Socket socket, Player player) {
        room.addPlayer(player);
        sockets.add(socket);
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public void setNumOfPlayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }

    private void startGame() {
        active = true;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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