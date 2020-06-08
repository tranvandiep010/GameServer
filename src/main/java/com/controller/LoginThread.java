package com.controller;

import com.model.Constant;
import com.model.Room;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class LoginThread extends Thread {

    Socket socket;
    String requestMessageLine = null;
    protected static List<String> users = new ArrayList<>();
    protected static Map<Integer, TaskThread> taskMap = new HashMap<>();
    protected static Map<Integer, ReceiveThread> IMap = new HashMap<>();
    protected static Map<Integer, Integer> numPlayers = new HashMap<>();
    protected static Map<Integer, Boolean> isRunning = new HashMap<>();

    public LoginThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                requestMessageLine = inFromClient.readLine();
                if (requestMessageLine != null) {
                    String[] data = requestMessageLine.split("\\|");
                    if (data.length > 1) {
                        if (data[0].equals("LOGIN")) {
                            if (users.contains(data[1])) outToClient.writeBytes("LOGIN|0\n");
                            else {
                                synchronized (users) {
                                    users.add(data[1]);
                                }
                                outToClient.writeBytes("LOGIN|1|" + Constant.NUM_OF_LEVEL + "|" + Constant.NUM_OF_ROOM + "\n");
                            }
                        } else if (data[0].equals("JOIN_ROOM")) {
                            synchronized (users) {
                                if (users.contains(data[1])) {
                                    int idRoom = Integer.parseInt(data[2]);
                                    TaskThread task = taskMap.get(idRoom);
                                    ReceiveThread io = IMap.get(idRoom);
                                    if (numPlayers.get(idRoom) < Room.MAX_PEOPLE && !isRunning.get(idRoom)) {
                                        io.addPlayer(socket, data[1]);
                                        task.addPlayer(socket, data[1], data[3]);
                                        if (!io.isAlive()) io.start();
                                        if (!task.isAlive()) task.start();
//                                        if (io.getState() == State.WAITING) io.notify();
//                                        if (task.getState() == State.WAITING) task.notify();
                                        numPlayers.replace(idRoom, numPlayers.get(idRoom) + 1);
                                        outToClient.writeBytes("JOIN_ROOM|1\n");
                                        break;
                                    } else {
                                        outToClient.writeBytes("JOIN_ROOM|0\n");
                                    }
                                } else {
                                    outToClient.writeBytes("JOIN_ROOM|0\n");
                                }
                            }
                        } else if (data[0].equals("QUITGAME")) {
                            synchronized (users) {
                                users.remove(data[1]);
                            }
                            break;
                        }
                    } else {
                        outToClient.writeBytes("EXCEPTION\n");
                    }
                } else {
                    outToClient.writeBytes("EXCEPTION|0\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.setKeepAlive(false);
                } catch (SocketException socketException) {
                    socketException.printStackTrace();
                }
                break;
            }
        }
    }

    public static synchronized boolean removePlayer(String name) {
        synchronized (users) {
            return users.remove(name);
        }
    }

    public static synchronized void decNumPlayerOfRoom(Integer idRoom) {
        synchronized (numPlayers) {
            Integer value = numPlayers.get(idRoom) - 1;
            numPlayers.replace(idRoom, value > 0 ? value : 0);
        }
    }
}