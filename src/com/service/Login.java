package com.service;

import com.model.Player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Login {

    private static List<String> users = new ArrayList<>();
    private static Login login = null;

    private Login() {
    }

    public static Login getInstance() {
        if (login == null) login = new Login();
        return login;
    }

    public void execute(Socket socket, String data, Player player) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            if (users.contains(data)) {
                outputStream.writeBytes("040");
            } else {
                users.add(data);
                player.setName(data);
                outputStream.writeBytes("041");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
