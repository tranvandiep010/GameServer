package com.controller;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

public class Main {

    static int serverPort;
    String NUM_OF_ROOM = "";
    String NUM_OF_LEVEL = "";
    public static int flag = 1;
    private static Map<Integer, RoomThread> roomMap = new HashMap<>();

    public Main() {
        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
            serverPort = Integer.parseInt(p.getProperty("SERVER_PORT"));
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
        //create room
        for (int i = 0; i < Integer.parseInt(NUM_OF_ROOM); ++i) {
            for (int level = 1; level < Integer.parseInt(NUM_OF_LEVEL); ++level) {
                roomMap.put(i*Integer.parseInt(NUM_OF_LEVEL)+level,new RoomThread(i*Integer.parseInt(NUM_OF_LEVEL)+level));
            }
        }
    }

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        try {
            System.out.println(Level.INFO + "Binding to port: {0}" + serverPort);
            // create server socket with port = SERVER_PORT
            serverSocket = new ServerSocket(serverPort);
            System.out.println(Level.INFO + "Server started: {0}" + serverSocket);
            System.out.println("Waiting for client ...");
            // wait client connect = TCP
            while (flag == 1) {
                try {
                    Socket socket = serverSocket.accept();
                    BlockingQueue<String> queue=new LinkedBlockingDeque<>();
                    SalveThread salveThread =new SalveThread(socket,queue);
                    salveThread.start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    System.out.println("server socket is closed");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static Map<Integer, RoomThread> getRoomMap() {
        return roomMap;
    }

    public static void setRoomMap(Map<Integer, RoomThread> roomMap) {
        Main.roomMap = roomMap;
    }
}