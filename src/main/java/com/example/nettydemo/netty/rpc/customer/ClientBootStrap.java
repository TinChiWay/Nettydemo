package com.example.nettydemo.netty.rpc.customer;

import com.example.nettydemo.netty.rpc.netty.NettyClient;
import com.example.nettydemo.netty.rpc.publicinterface.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientBootStrap {
    public static final String PROVIDER_NAME = "HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();

        HelloService helloServer = (HelloService) NettyClient.getBean(HelloService.class, PROVIDER_NAME);

        String result = helloServer.hello("你好 dubbo~");

        log.info("调用的结果 res=" + result);
    }
}
