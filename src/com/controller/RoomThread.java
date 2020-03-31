package com.controller;

import com.model.Player;
import com.model.Room;
import com.service.Factory;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class RoomThread extends Thread {

    private Room room;
    private int numOfPlayer;
    ExecutorService service = Executors.newFixedThreadPool(Room.MAX_PEOPLE);
    UDPThread udpThread;
    private int flag = 1;
    BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

    private int ready=0;

    public RoomThread(int id) {
        room = new Room();
        room.setId(id);
        udpThread = new UDPThread(3308 + room.getId(),queue);
    }

    @Override
    public void run() {
        Factory factory = new Factory();
        while (flag == 1) {
            try {
                String data=queue.take();
                if (data.charAt(0)=='1'){
                    factory.handle(data.substring(1));
                }
                factory.handle(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlayer(Socket socket, String name) {
        room.addPlayer(new Player(room.getId() * 4 + room.getPlayers().size() + 1, name));
        TCPThread tcpThread = new TCPThread(socket,queue);
        service.execute(tcpThread);
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public void setNumOfPlayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }
}