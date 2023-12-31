package com.example.nettydemo.netty.protocltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    //
                    .handler(new MyClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("localhost", 7001).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            eventExecutors.shutdownGracefully();
        }


    }
}
