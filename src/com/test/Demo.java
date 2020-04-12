package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Demo extends Thread {
    Socket socket;
    int id;
    int i=100;
    public Demo(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        while (i--!=0) {
            BufferedReader inFromServer = null;
            try {
                inFromServer = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String modifiedSentence = inFromServer.readLine();
                System.out.println("Received from server " + id + ":" + modifiedSentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
