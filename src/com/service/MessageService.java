package com.service;

import com.controller.RoomThread;
import com.model.Player;
import com.model.Room;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class MessageService {
    String LOGIN_CODE = "";
    String JOIN_ROOM_CODE = "";
    String MOVE_CODE = "";
    String START_CODE = "";
    String END_GAME_CODE = "";
    BlockingQueue<String> queue = null;
    Player player = null;
    RoomThread roomThread = null;
    Socket socket = null;

    public MessageService(Player player, Socket socket) {
        this.socket = socket;
        this.player = player;
        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
            LOGIN_CODE = p.getProperty("LOGIN_CODE");
            JOIN_ROOM_CODE = p.getProperty("JOIN_ROOM_CODE");
            MOVE_CODE = p.getProperty("MOVE_CODE");
            END_GAME_CODE = p.getProperty("END_GAME_CODE");
        } catch (IOException e) {
            System.out.println("Homecontroller: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Homecontroller: " + e.getMessage());
                }
            }
        }
    }

    public void handle(String message) {
        String CODE = message.substring(0, message.indexOf('|'));
        String DATA = message.substring(message.indexOf('|')+1);
        try {
            if (CODE.equals(LOGIN_CODE)) {
                Login login = Login.getInstance();
                login.execute(socket, DATA, player);
            } else if (CODE.equals(JOIN_ROOM_CODE)) {
                JoinRoom joinRoom = new JoinRoom();
                joinRoom.execute(socket, DATA, queue, player, roomThread);
            } else if (CODE.equals(MOVE_CODE)) {
                String[] position = DATA.split("|");
                queue.put(MOVE_CODE + "|" + position + "|" + player.getName());
            } else if (CODE.equals(START_CODE)) {
                queue.put(START_CODE + player.getName());
            } else if (CODE.equals(END_GAME_CODE)) {
                queue.put(END_GAME_CODE + player.getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
