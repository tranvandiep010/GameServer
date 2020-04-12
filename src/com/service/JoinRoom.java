package com.service;

import com.controller.Main;
import com.controller.RoomThread;
import com.model.Player;
import com.model.Room;

import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class JoinRoom {

    static String NUM_OF_ROOM = "";
    static String NUM_OF_LEVEL = "";
    private static Map<Integer, RoomThread> roomMap = new HashMap<>();
    private static Map<Integer, BlockingQueue<String>> listQueue=new HashMap<>();

    private static JoinRoom joinRoom=null;
    private JoinRoom() {
        setup();
    }

    public static JoinRoom getInstance(){
        if (joinRoom==null) joinRoom=new JoinRoom();
        return joinRoom;
    }

    public BlockingQueue<String> execute(Socket socket, String data, Player player) {
        RoomThread room=null;
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            room = roomMap.get(Integer.parseInt(data));
            System.out.println("JOIN ROOM");
            if (room.numOfPlayer< Room.MAX_PEOPLE) {
                if (!room.isAlive()) {
                    room.start();
                }
                room.addPlayer(socket, player);
                outputStream.writeBytes("JOIN_ROOM|1\n");
            } else {
                outputStream.writeBytes("JOIN_ROOM|0\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return listQueue.get(Integer.parseInt(data));
        }
    }
    private static void setup() {
        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
            NUM_OF_ROOM = p.getProperty("NUM_OF_ROOM");
            NUM_OF_LEVEL = p.getProperty("NUM_OF_LEVEL");
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
        //create room
        for (int level = 1; level <= Integer.parseInt(NUM_OF_LEVEL); ++level) {
            for (int room = 1; room <= Integer.parseInt(NUM_OF_ROOM); ++room) {
                BlockingQueue<String> queue=new LinkedBlockingDeque<>(20);
                roomMap.put(level * Integer.parseInt(NUM_OF_ROOM) + room, new RoomThread(level * Integer.parseInt(NUM_OF_ROOM) + room,queue));
                listQueue.put(level * Integer.parseInt(NUM_OF_ROOM) + room, queue);
            }
        }
    }
}
