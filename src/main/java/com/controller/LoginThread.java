package com.controller;

import com.model.Constant;
import com.model.Room;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class LoginThread extends Thread {

    Socket socket;
    String requestMessageLine = null;
    protected static List<String> users = new ArrayList<>();
    protected static Map<Integer, TaskThread> taskMap = new HashMap<>();
    protected static Map<Integer, IOThread> IOMap = new HashMap<>();
    protected static Map<Integer, Integer> numPlayers = new HashMap<>();

    public LoginThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            requestMessageLine = inFromClient.readLine();
            if (requestMessageLine != null) {
                System.out.println(requestMessageLine);
                String[] data = requestMessageLine.split("\\|");
                if (data.length > 1) {
                    if (data[0].equals("LOGIN")) {
                        if (users.contains(data[1])) outToClient.writeBytes("LOGIN|0\n");
                        else {
                            users.add(data[1]);
                            outToClient.writeBytes("LOGIN|1|" + Constant.NUM_OF_LEVEL + "|" + Constant.NUM_OF_ROOM + "\n");
                        }
                        socket.close();
                    } else if (data[0].equals("JOIN_ROOM")) {
                        if (users.contains(data[1])) {
                            int idRoom = Integer.parseInt(data[2]);
                            TaskThread task = taskMap.get(idRoom);
                            IOThread io = IOMap.get(idRoom);
                            if (numPlayers.get(idRoom) < Room.MAX_PEOPLE) {
                                if (!io.isAlive()) {
                                    io.start();
                                    task.start();
                                }
                                io.addPlayer(socket, data[1]);
                                task.addPlayer(socket, data[1] ,data[2]);
                                numPlayers.replace(idRoom, numPlayers.get(idRoom) + 1);
                                outToClient.writeBytes("JOIN_ROOM|1\n");
                            } else {
                                outToClient.writeBytes("JOIN_ROOM|0\n");
                            }
                        } else outToClient.writeBytes("JOIN_ROOM|0\n");
                    }
                } else {
                    outToClient.writeBytes("EXCEPTION\n");
                    socket.close();
                }
            } else {
                outToClient.writeBytes("EXCEPTION|0\n");
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("  thread something happen in communication with client");
            e.printStackTrace();
        }
    }
}
