package com.test;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class tcpclient {

    public static void main(String[] args) throws Exception {
        String sentence; String modifiedSentence;
        int server_port = 0;
        String server_ip = "";
        Socket clientSocket = null;

        if (args.length !=2) {
            System.out.println("Usage: tcpclient [server address] [server port]");
            System.exit(0);
        }
        try {
            server_port = Integer.parseInt(args[1]);
            server_ip = args[0];
        } catch (Exception e) {
            System.out.println("Usage: tcpclient [server address] [server port]");
            System.exit(0);
        }

        try {
            System.out.println("Connecting to TCP Server at: [" + server_ip + ":" + server_port + "]");
            clientSocket = new Socket(server_ip, server_port);
        } catch (Exception e) {
            System.out.println("Cannot connect to TCP Server.\n Please check the server and run tcpclient again.");
                    System.exit(0);
        }
        while (true) {
            System.out.println("Server connected. Local client port: " + clientSocket.getLocalPort());
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            System.out.print("Enter a sentence to send to server: ");
            sentence = inFromUser.readLine();
            if (sentence.equals("0")) break;
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("Received from server: " + modifiedSentence);
        }
        clientSocket.close();
    }
}

