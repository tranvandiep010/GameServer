package com.service;

import com.controller.Main;
import com.controller.RoomThread;
import com.model.Player;
import com.model.Room;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class JoinRoom {

    public RoomThread execute(Socket socket, String data, BlockingQueue<String> queue, Player player) {
        RoomThread roomThread=null;
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            Map<Integer, RoomThread> roomThreadMap = Main.getRoomMap();
            roomThread = roomThreadMap.get(Integer.parseInt(data));
            if (roomThread.getRoom().getPlayers().size() < Room.MAX_PEOPLE) {
                if (!roomThread.isAlive()) {
                    roomThread.setQueue(queue);
                    roomThread.start();
                }
                roomThread.addPlayer(socket, player);
                outputStream.writeBytes("JOIN_ROOM|1\n");
            } else {
                outputStream.writeBytes("JOIN_ROOM|0\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return roomThread;
        }
    }
}
