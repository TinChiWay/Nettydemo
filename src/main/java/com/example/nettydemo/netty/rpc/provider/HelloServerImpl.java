package com.example.nettydemo.netty.rpc.provider;

import com.example.nettydemo.netty.rpc.publicinterface.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServerImpl implements HelloService {
    @Override
    public String hello(String msg) {

        log.info("收到客户端消息:{}", msg);

        if (msg != null) {
            return "你好客户端，我已经收到你的消息 [" + msg + "]";
        }

        return "你好客户端，我已经收到你的消息";
    }
}
