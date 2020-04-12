package com.test;

public class Clients {

    public static void main(String[] args) {
        for (int i=1;i<5;++i){
            tcpclient tcp=new tcpclient(3308,"127.0.0.1",i);
            tcp.start();
        }
    }
}
