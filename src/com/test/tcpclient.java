package com.test;
import java.io.*;
import java.net.*;
import java.util.Scanner;

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
            BufferedReader inFromUser1 = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer1 = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            System.out.print("Login: ");
            sentence = "LOGIN|tvd"+id;
            outToServer1.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer1.readLine();
            System.out.println("Received from server: " + modifiedSentence);
            //join_room
            System.out.println("Server connected. Local client port: " + clientSocket.getLocalPort());
            BufferedReader inFromUser2 = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer2 = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            System.out.print("join_room: ");
            sentence = "JOIN_ROOM|"+id;
            outToServer2.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer2.readLine();
            System.out.println("Received from server: " + modifiedSentence);
            //move
            while (true) {
                System.out.println("Server connected. Local client port: " + clientSocket.getLocalPort());
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                System.out.print("Enter a sentence to send to server: ");
                sentence = "MOVE|1|1|1";
                if (sentence.equals("0")) break;
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();
                System.out.println("Received from server: " + modifiedSentence);
                Thread.sleep(50);
            }
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to TCP Server.\n Please check the server and run tcpclient again.");
                    System.exit(0);
        }
    }
}

