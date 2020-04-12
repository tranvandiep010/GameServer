package com.service;

import com.controller.RoomThread;
import com.model.Player;

import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageService {
    String LOGIN_CODE = "";
    String JOIN_ROOM_CODE = "";
    String MOVE_CODE = "";
    String START_CODE = "";
    String END_GAME_CODE = "";
    String QUIT_CODE = "";
    BlockingQueue<String> queue = new LinkedBlockingDeque<>(20);
    Player player = null;
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
            QUIT_CODE = p.getProperty("QUIT_CODE");
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
        String CODE = "";
        String DATA = "";
        if (message.indexOf('|') != -1) {
            CODE = message.substring(0, message.indexOf('|'));
            DATA = message.substring(message.indexOf('|') + 1);
        } else CODE = message;
        try {
            if (CODE.equals(LOGIN_CODE)) {
                Login login = Login.getInstance();
                login.execute(socket, DATA, player);
            } else if (CODE.equals(QUIT_CODE)) {
                JoinRoom.getInstance().removePlayer(queue,player);
                Login login = Login.getInstance();
                login.quit(player);
            } else if (CODE.equals(JOIN_ROOM_CODE)) {
                queue = JoinRoom.getInstance().execute(socket, DATA, player);
            } else if (CODE.equals(MOVE_CODE)) {
                queue.put(MOVE_CODE + "|" + DATA + "|" + player.getName());
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
