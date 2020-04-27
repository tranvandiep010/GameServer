package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.log.Log;
import com.model.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TaskThread extends Thread {

    BlockingQueue<String> IQueue = null;
    BlockingQueue<String> OQueue = new LinkedBlockingDeque<>(50);
    List<Player> players = new ArrayList<>();
    List<Socket> sockets = new ArrayList<>();
    List<Enermy> enermies = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    List<Position> positionDefault = new ArrayList<>();
    Integer ready = 0;
    Integer numPlayer = 0;
    Integer guards = 0;
    ObjectMapper mapper = new ObjectMapper();
    Logger logger = Log.getLogger();

    public TaskThread(BlockingQueue<String> IQueue) {
        this.IQueue = IQueue;
        positionDefault.add(new Position(0, 0, 2.98f));
        positionDefault.add(new Position(-30, 0, 2.98f));
        positionDefault.add(new Position(30, 0, 2.98f));
        new SendThread(sockets, OQueue).start();
    }

    @Override
    public void run() {
        Clock clock = Clock.systemDefaultZone();
        long cycle = clock.millis();
        for (; ; ) {
            try {
                String data = IQueue.poll(2, TimeUnit.MILLISECONDS);
                if (data != null) handle(data);
                long curr = clock.millis();
                if (curr - cycle >= 17) {
                    try {
                        sendData();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    //empty queue
                    if (numPlayer == 0 && ready != 0) break;
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
//        logger.info("Size" + IQueue.size());
        System.out.println("Size" + IQueue.size());
        //if (ready >= 3) IQueue.clear();// chỉ xóa những dữ liệu không nhạy cảm
    }

    public void addPlayer(Socket socket, String name, String plane) {
        Player player = new Player(name, true);
        player.setPosition(positionDefault.get(numPlayer));
        player.setPlane(Integer.parseInt(plane));
        players.add(player);
        sockets.add(socket);
        numPlayer++;
    }

    //xử lí dữ liệu đầu vào
    public void handle(String message) {
        String[] data = message.split("\\|");
        if (data.length > 0) {
            if (data[0].equals("MOVE")) {
                Float x = Float.parseFloat(data[1]);
                Float y = Float.parseFloat(data[2]);
                Float z = Float.parseFloat(data[3]);
                String name = data[4];
                for (Player player : players)
                    if (player.getName().equals(name)) {
                        player.setPosition(new Position(x, y, z));
                        break;
                    }
            } else if (data[0].equals("READY")) {
                guards++;
            } else if (data[0].equals("ENERMY")) {
                enermies.add(new Enermy(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[4])));
                //TODO
                //add score player
            } else if (data[0].equals("ITEM")) {
                items.add(new Item(Integer.parseInt(data[1]), Integer.parseInt(data[2]), 0));
                //TODO
                //add score player
            } else if (data[0].equals("SHOT")) {
                try {
                    OQueue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (data[0].equals("ENDGAME")) {
                numPlayer--;
                guards--;
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
    public void createEnermy() {

    }

    //gửi dữ liệu đi
    public void sendData() throws JsonProcessingException, InterruptedException {
        if (ready == 0 && guards == 3) {
            OQueue.put("START");
            ready = 1;
        }
        String jsonString = "STATE|";
        //execute
        for (Player player : players) jsonString += mapper.writeValueAsString(player)+"|";
        for (Enermy enermy : enermies) jsonString += mapper.writeValueAsString(enermy)+"|";
        enermies.clear();
        OQueue.put(jsonString);
    }
}
