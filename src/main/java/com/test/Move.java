package com.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Move extends Thread {

    Socket socket;
    int name;

    public Move(Socket socket, int name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String sentence = "MOVE|" + name + "|" + name + "|" + name + "|tvd" + name;
                outToServer.writeBytes(sentence + '\n');
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
