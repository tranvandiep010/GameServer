package com.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UDPThread extends Thread{
    private int flag = 1;

    private DatagramSocket socket;
    BlockingQueue<String> queue = null;

    public UDPThread(int id,BlockingQueue<String> queue) {
        this.queue=queue;
        try {
            this.socket = new DatagramSocket(id);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] receiveData = new byte[1024];
        try {
            while (flag == 1) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
//                System.out.println("  - Received data from client: " +
//                        receivePacket.getAddress().getHostAddress() + ":" + receivePacket.getPort());
                String sentence = new String(receivePacket.getData());
                queue.put(sentence);
            }
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }finally {
            if (socket!=null) socket.close();
        }
    }
}
