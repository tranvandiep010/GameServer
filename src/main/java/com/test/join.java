package com.test;

import java.io.*;
import java.net.*;
import java.time.Clock;

public class join extends Thread {
    int server_port = 0;
    String server_ip = "";
    int id = 0;

    public join(int server_port, String server_ip, int id) {
        this.server_port = server_port;
        this.server_ip = server_ip;
        this.id = id;
    }

    @Override
    public void run() {
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
            Thread.sleep(20);
            sentence = "JOIN_ROOM|tvd" + id + "|9|1";
            outToServer.writeBytes(sentence + '\n');
            modifiedSentence = inFromServer.readLine();
            System.out.println("Received from server login: " + modifiedSentence);
            if (modifiedSentence.equals("JOIN_ROOM|1")) {
                try {
                    Clock clock = Clock.systemDefaultZone();
                    long cycle1 = clock.millis();
                    long cycle2 = clock.millis();
                    while (true) {
                        long curr = clock.millis();
                        if (curr - cycle1 >= 14) {
                            String mess = "MOVE|" + id + "|" + id + "|" + id + "|tvd" + id;
                            outToServer.writeBytes(mess + '\n');
                            cycle1=clock.millis();
                        }
                        if (curr - cycle2 >= 500) {
                            String mess = "SHOT|tvd" + id;
                            outToServer.writeBytes(mess + '\n');
                            cycle2=clock.millis();
                        }
                        if (inFromServer.ready()){
                            System.out.println(inFromServer.readLine());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else clientSocket.close();
        } catch (Exception e) {
            System.out.println("Cannot connect to TCP Server.\n Please check the server and run tcpclient again.");
            System.exit(0);
        }
    }
}

