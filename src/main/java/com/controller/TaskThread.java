package com.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    int ready = 0;
    boolean isStart = false;
    GsonBuilder builder = new GsonBuilder();

    Gson gson;

    public TaskThread(BlockingQueue<String> IOQueue) {
        this.IOQueue = IOQueue;
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    @Override
    public void run() {
        Clock clock = Clock.systemDefaultZone();
        long cycle = clock.millis();
        for (; ; ) {
            try {
                String data = IOQueue.poll(5, TimeUnit.MILLISECONDS);
                handle(data);
                long curr = clock.millis();
                if (curr - cycle >= 50) {
                    sendData();
                    //empty queue
                    if (isStart && ready == 0) break;
                    cycle = curr;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        for (Bullet bullet : bullets) {
            bullet.setTTL(bullet.getTTL() - 1);
            if (bullet.getTTL() == 0) bullets.remove(bullet);
        }
        for (Item item : items) {
            item.setTTL(item.getTTL() - 1);
            if (item.getTTL() == 0) items.remove(item);
        }
        for (Enermy enermy : enermies) {
            enermy.setTTL(enermy.getTTL() - 1);
            if (enermy.getTTL() == 0) enermies.remove(enermy);
        }
    }

    public void addPlayer(Socket socket, String name) {
        Player player = new Player(name, true);
        players.add(player);
        sockets.add(socket);
    }

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
                        player.getPlane().setPosition(new Position(x, y, z));
                        break;
                    }
            } else if (data[0].equals("START")) {
                ready++;
            } else if (data[0].equals("ENERMY")) {
                int index = 0, i = 0;
                for (Enermy enermy : enermies) {
                    if (enermy.getId() == Integer.parseInt(data[1])) {
                        index = i;
                        break;
                    }
                    i++;
                }
                enermies.remove(index);
            } else if (data[0].equals("ITEM")) {
                int index = 0, i = 0;
                for (Item item : items) {
                    if (item.getId() == Integer.parseInt(data[1])) {
                        //TODO
                        // add score for player
                        index = i;
                        break;
                    }
                    i++;
                }
                enermies.remove(index);
            } else if (data[0].equals("BULLET")) {
                bullets.add(new Bullet(Integer.parseInt(data[1]), new Position(Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4])), data[5]));
            } else if (data[0].equals("ENDGAME")) {
                ready--;
                int index = 0;
                for (Player player : players) {
                    if (player.equals(data[1])) break;
                    index++;
                }
                players.remove(index);
                try {
                    sockets.get(index).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sockets.remove(index);
            }
        }
    }

    public void sendData() {
        List<String> jsonString = new ArrayList<>();
        String[] a = new String[Constant.NUM_OF_PLAYER];
        String[] b = new String[enermies.size()];
        String[] c = new String[bullets.size()];
        String[] d = new String[items.size()];
        //execute
        if (!isStart && ready == Constant.NUM_OF_PLAYER) jsonString.add("START");
        int i = 0;
        for (Player player : players) a[i++] = gson.toJson(player);
        jsonString.add(Arrays.toString(a));//player
        i = 0;
        for (Enermy enermy : enermies) b[i++] = gson.toJson(enermy);
        jsonString.add(Arrays.toString(b));//enermy
        i = 0;
        for (Bullet bullet : bullets) c[i++] = gson.toJson(bullet);
        jsonString.add(Arrays.toString(c));//bullet
        i = 0;
        for (Item item : items) d[i++] = gson.toJson(item);
        jsonString.add(Arrays.toString(d));//item
        String data = "[" + String.join(",", jsonString) + "]\n";
        for (Socket socket : sockets) {
            try {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeBytes(data);
            } catch (IOException e) {
                System.out.println("Send data error");
                e.printStackTrace();
            }
        }
    }
}
