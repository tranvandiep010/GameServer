package com.service;

import com.controller.Main;
import com.controller.RoomThread;
import com.model.Player;

import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Login {

    String NUM_OF_ROOM = "";
    String NUM_OF_LEVEL = "";

    private static List<String> users = new ArrayList<>();
    private static Login login = null;
    private String room = "";


    private Login() {
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
        Map<Integer, RoomThread> roomMap = Main.getRoomMap();
        int k = 0;
        for (Integer entry : roomMap.keySet()) {
            if (entry % Integer.parseInt(NUM_OF_ROOM) == 0) {
                room += ((entry / Integer.parseInt(NUM_OF_ROOM)-1) + "|" + ++k + "|");
                k = 0;
            } else k++;
        }
        room = room.substring(0, room.length() - 1);
    }

    public static Login getInstance() {
        if (login == null) login = new Login();
        return login;
    }

    public void execute(Socket socket, String data, Player player) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            if (users.contains(data)) {
                outputStream.writeBytes("LOGIN|0\n");
            } else {
                users.add(data);
                player.setName(data);
                outputStream.writeBytes("LOGIN|1" + "|" + room + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
