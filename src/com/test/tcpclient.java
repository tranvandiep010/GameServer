package com.test;

import java.io.*;
import java.net.*;

public class tcpclient extends Thread{
    int server_port = 0;
    String server_ip = "";
    int id=0;
    int move=0;

    public tcpclient(int server_port, String server_ip, int id) {
        this.server_port = server_port;
        this.server_ip = server_ip;
        this.id=id;
    }

    @Override
    public void run(){
        String sentence;
        String modifiedSentence;
        Socket clientSocket = null;

        try {
            System.out.println("Connecting to TCP Server at: [" + server_ip + ":" + server_port + "]");
            clientSocket = new Socket(server_ip, server_port);
            //login
            System.out.println("Server connected. Local client port: " + clientSocket.getLocalPort());
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            System.out.print("Login: ");
            sentence = "LOGIN|tvd"+id;
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("Received from server login: " + modifiedSentence);
            //join_room
            System.out.print("join_room: ");
            sentence = "JOIN_ROOM|"+9;
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("Received from server join room: " + modifiedSentence);
            sentence = "MOVE|1|1|1";
            outToServer.writeBytes(sentence + '\n');
            //move
            this.sleep(50);
            modifiedSentence = inFromServer.readLine();
            System.out.println("Received from server " + id + ":" + modifiedSentence);
            outToServer.writeBytes("QUIT" + '\n');
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to TCP Server.\n Please check the server and run tcpclient again.");
                    System.exit(0);
        }
    }
}

