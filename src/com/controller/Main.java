package com.controller;

import com.model.Room;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class Main {

    public static Map<String, TCPThread> users = new HashMap<String, TCPThread>();
    static int numOfThread;
    static int serverPort;
    public static int flag = 1;
    public static final String GROUP_ADDRESS = "224.0.0.1";
    public static final int PORT = 8888;
    private static DatagramSocket broadSocket;
    private static InetAddress address;

    private static DatagramPacket outPacket = null;

    public Main() throws SocketException, UnknownHostException {
        address = InetAddress.getByName(GROUP_ADDRESS);
        broadSocket=new DatagramSocket();
    }

    public static void main(String[] args) {

        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
            numOfThread = Integer.parseInt(p.getProperty("NUM_OF_THREAD"));
            serverPort = Integer.parseInt(p.getProperty("SERVER_PORT"));
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

        // create NUM_OF_THREAD thread pool
        ExecutorService executorLogin = Executors.newFixedThreadPool(numOfThread);
        ExecutorService executorRoom = Executors.newFixedThreadPool(numOfThread);
        ServerSocket serverSocket = null;
        DatagramSocket datagramSocket = null;
        List<String> names = new ArrayList<>();
        int idRoom=1;
        Room room=new Room();
        RoomThread roomThread=new RoomThread(idRoom++);
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
                    DataOutputStream resp = new DataOutputStream(socket.getOutputStream());
                    // get message
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = bufferedReader.readLine();

                    //add player to list
                    if (!names.contains(message)) {
                        names.add(message);
                        roomThread.addPlayer(socket,message);
                        if (roomThread.getNumOfPlayer()==1){
                            executorRoom.execute(roomThread);
                        }else if (roomThread.getNumOfPlayer()==4){
                            roomThread=new RoomThread(idRoom++);
                        }
                        //response success
                        resp.writeBytes("1");
                    }
                    else resp.writeBytes("0");//response fail

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
            if (broadSocket!=null){
                broadSocket.close();
            }
        }
    }
    public static void sendBroadCast(String msg){
        outPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, PORT);
        try {
            broadSocket.send(outPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}