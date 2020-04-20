package com.test;

public class Clients {

    public static void main(String[] args) throws InterruptedException {
        for (int i=1;i<5;++i){
            tcpclient tcp=new tcpclient(1024,"127.0.0.1",i);
            tcp.start();
        }
        Thread.sleep(500);
        for (int i=1;i<5;++i){
            join tcp=new join(1024,"127.0.0.1",i);
            tcp.start();
        }
    }
}
