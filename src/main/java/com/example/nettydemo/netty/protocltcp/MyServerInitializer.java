package com.example.nettydemo.netty.protocltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加一个出站的handler 对数据进行编码
        ch.pipeline()
                .addLast(new MyMessageDecoder())
                .addLast(new MyMessageEncoder())
                .addLast(new MyServerHandler());
    }
}
