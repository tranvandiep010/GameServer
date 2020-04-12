package com.controller;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;

public class Main {

    static int serverPort;
    public static int flag = 1;

    public static void main(String[] args) {
        setup();
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
                    System.out.println("Client:" + socket.getPort());
                    SalveThread salveThread = new SalveThread(socket);
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


    private static void setup() {
        FileReader reader = null;
        Properties p = null;
        try {
            reader = new FileReader("config/application.properties");
            p = new Properties();
            p.load(reader);
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
    }
}