package com.service;

import com.controller.Main;
import com.controller.RoomThread;
import com.model.Player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Login {

    private static List<String> users = new ArrayList<>();
    private static Login login = null;
    private String room = "";

    private Login() {
        Map<Integer, RoomThread> roomMap = Main.getRoomMap();
        for (Integer entry : roomMap.keySet()) {
            room += (entry + "|");
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
                outputStream.writeBytes("LOGIN|0");
            } else {
                users.add(data);
                player.setName(data);
                outputStream.writeBytes("LOGIN|1" + "|" + room+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
