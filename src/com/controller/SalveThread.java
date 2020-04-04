package com.controller;

import com.model.Player;
import com.service.MessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SalveThread extends Thread {
    private int flag = 1;

    private Socket socket;
    Player player = new Player();
    MessageService service = null;

    public SalveThread(Socket socket) {
        this.socket = socket;
        service = new MessageService(player,socket);
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            while (flag == 1) {
                inputStreamReader = new InputStreamReader(this.socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String content = bufferedReader.readLine();
                if (content != null) {
                    Thread.sleep(10);
                    service.handle(content);
                }

            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

            if (socket != null) {
                try {
                    socket.close();

                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
