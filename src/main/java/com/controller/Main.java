package com.controller;

import com.model.Constant;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        setup();
        ServerSocket serverSocket = null;
        try {
            System.out.println(Level.INFO + "Binding to port: {0}" + Constant.SERVER_PORT);
            // create server socket with port = SERVER_PORT
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
            System.out.println(Level.INFO + "Server started: {0}" + serverSocket);
            System.out.println("Waiting for client ...");
            // wait client connect = TCP
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    LoginThread loginThread = new LoginThread(socket);
                    loginThread.start();
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


    private static void setup() {
        Map<Integer, TaskThread> taskMap = new HashMap<>();
        Map<Integer, IOThread> IOMap = new HashMap<>();
        Map<Integer, Integer> numPlayers = new HashMap<>();
        for (int level = 1; level <= Constant.NUM_OF_LEVEL; ++level) {
            for (int room = 1; room <= Constant.NUM_OF_ROOM; ++room) {
                BlockingQueue<String> queue = new LinkedBlockingDeque<>(50);
                taskMap.put(level * Constant.NUM_OF_ROOM + room, new TaskThread(queue));
                IOMap.put(level * Constant.NUM_OF_ROOM + room, new IOThread(queue));
                numPlayers.put(level * Constant.NUM_OF_ROOM + room, 0);
            }
        }
        LoginThread.taskMap = taskMap;
        LoginThread.IOMap = IOMap;
        LoginThread.numPlayers = numPlayers;
    }
}