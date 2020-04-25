package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class TaskThread extends Thread {

    BlockingQueue<String> IOQueue = null;
    List<Player> players = new ArrayList<>();
    List<Socket> sockets = new ArrayList<>();
    List<Enermy> enermies = new ArrayList<>();
    List<Bullet> bullets = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<Position> positionDefault = new ArrayList<>();
    int ready = 0;
    int numPlayer = 0;
    boolean isStart = false;
    ObjectMapper mapper = new ObjectMapper();

    public TaskThread(BlockingQueue<String> IOQueue) {
        this.IOQueue = IOQueue;
        positionDefault.add(new Position(0, 0, 2.98f));
        positionDefault.add(new Position(-30, 0, 2.98f));
        positionDefault.add(new Position(30, 0, 2.98f));
    }

    @Override
    public void run() {
        Clock clock = Clock.systemDefaultZone();
        long cycle = clock.millis();
        for (; ; ) {
            try {
                String data = IOQueue.poll(3, TimeUnit.MILLISECONDS);
                if (data != null) handle(data);
                long curr = clock.millis();
                if (curr - cycle >= 50) {
                    try {
                        sendData();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    //empty queue
                    if (isStart && ready == 0) break;
                    update();
                    cycle = curr;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //cập nhật sau khi gửi xong
    private void update() {
        //System.out.println("Size" + IOQueue.size());
        if (ready >= 3) IOQueue.clear();// chỉ xóa những dữ liệu không nhạy cảm
    }

    public synchronized void addPlayer(Socket socket, String name, String plane) {
        Player player = new Player(name, true);
        player.setPosition(positionDefault.get(numPlayer));
        player.setPlane(Integer.parseInt(plane));
        players.add(player);
        sockets.add(socket);
        System.out.println(name);
        numPlayer++;
    }


    //xử lí dữ liệu đầu vào
    public void handle(String message) {
        String[] data = message.split("\\|");
        if (data.length > 0) {
            if (data[0].equals("MOVE")) {
                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                int z = Integer.parseInt(data[3]);
                String name = data[4];
                for (Player player : players)
                    if (player.getName().equals(name)) {
                        player.setPosition(new Position(x, y, z));
                        break;
                    }
            } else if (data[0].equals("START")) {
                ready++;
            } else if (data[0].equals("ENERMY")) {
                enermies.add(new Enermy(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[4])));
                //TODO
                //add score player
            } else if (data[0].equals("ITEM")) {
                items.add(new Item(Integer.parseInt(data[1]), Integer.parseInt(data[2]), 0));
                //TODO
                //add score player
            } else if (data[0].equals("BULLET")) {
                bullets.add(new Bullet(Integer.parseInt(data[1]), new Position(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])), data[5], Integer.parseInt(data[6])));
            } else if (data[0].equals("ENDGAME")) {
                ready--;
                //remove player
                int index = 0;
                for (Player player : players) {
                    if (player.equals(data[1])) break;
                    index++;
                }
                players.remove(index);
                //remove socket
                try {
                    sockets.get(index).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sockets.remove(index);
            }
        }
    }

    //tạo mục tiêu
    public void createEnermy(){

    }

    //gửi dữ liệu đi
    public void sendData() throws JsonProcessingException {
        List<String> jsonString = new ArrayList<>();
        String[] a = new String[Constant.NUM_OF_PLAYER];
        String[] b = new String[enermies.size()];
        //execute
        int i = 0;
        for (Player player : players) a[i++] = mapper.writeValueAsString(player);
        jsonString.add(String.join("|",a));//player
        i = 0;
        for (Enermy enermy : enermies) b[i++] = mapper.writeValueAsString(enermy);
        enermies.clear();
        jsonString.add(String.join("|",b));//enermy
        for (Socket socket : sockets) {
            try {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeBytes(String.join("|", jsonString)+"\n");
            } catch (IOException e) {
                System.out.println("Send data error");
                e.printStackTrace();
            }
        }
    }
}
