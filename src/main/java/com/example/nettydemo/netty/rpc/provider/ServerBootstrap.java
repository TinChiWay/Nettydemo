package com.example.nettydemo.netty.rpc.provider;

import com.example.nettydemo.netty.rpc.netty.NetttyServer;

public class ServerBootstrap {

    public static void main(String[] args) {
        NetttyServer.startServer("127.0.0.1", 7000);
    }

}
