package com.controller;

import com.model.Enermy;
import com.model.Player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SendThread extends Thread {

    List<Socket> sockets = new ArrayList<>();
    BlockingQueue<String> OQueue = null;

    public SendThread(List<Socket> sockets, BlockingQueue<String> OQueue) {
        this.sockets = sockets;
        this.OQueue = OQueue;
    }

    @Override
    public void run() {
        for(;;){
            try {
                String data = OQueue.take();
                transfer(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transfer(String message) {
        for (Socket socket : sockets) {
            try {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeBytes(message + "\n");
            } catch (IOException e) {
                //logger.warning("Send data error");
                e.printStackTrace();
            }
        }
    }
}
