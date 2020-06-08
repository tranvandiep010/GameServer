package com.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SendThread extends Thread {

    List<Socket> sockets;
    BlockingQueue<String> OQueue;

    public SendThread(List<Socket> sockets, BlockingQueue<String> OQueue) {
        this.sockets = sockets;
        this.OQueue = OQueue;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                String data = OQueue.take();
                transfer(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transfer(String message) {
        synchronized (sockets) {
            for (Socket socket : sockets) {
                try {
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeBytes(message + "\n");
                } catch (IOException e) {
//                    System.out.println(sockets.size());
//                    e.printStackTrace();
//                    synchronized (sockets) {
//                        sockets.remove(socket);
//                    }
//                    try {
//                        socket.close();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        }
    }
}