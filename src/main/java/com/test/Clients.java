package com.test;

public class Clients {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i < 4; ++i) {
            tcpclient tcp = new tcpclient(3308, "127.0.0.1", i);
            tcp.start();
        }
        Thread.sleep(1000);
        for (int i = 1; i < 4; ++i) {
            join tcp = new join(3308, "127.0.0.1", i);
            tcp.start();
        }
    }
}
