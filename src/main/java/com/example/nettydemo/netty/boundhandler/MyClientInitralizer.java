package com.example.nettydemo.netty.boundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitralizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加一个出站的handler 对数据进行编码
        ch.pipeline()
                .addLast(new MyLongToByteEncoder())
                .addLast(new MyByteToLongDecoder())
                .addLast(new MyClientHandler());
    }
}
