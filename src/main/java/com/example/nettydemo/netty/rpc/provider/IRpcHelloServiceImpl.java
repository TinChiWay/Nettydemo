package com.example.nettydemo.netty.rpc.provider;

import com.example.nettydemo.gupaoedu.rpc.IRpcHelloService;

public class IRpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "hello " + name + "!";
    }
}
