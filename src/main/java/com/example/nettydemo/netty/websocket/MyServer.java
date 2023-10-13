package com.example.nettydemo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workergroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 使用基于 http 协议， 使用http 的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块方式写，添加 ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // http 数据在传输过程中是分段的，HttpObjectAggregator 可以将多个段聚合起来
                            // 这就是为什么，当浏览器发送大量数据时，就会发出多次 http 请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 对于 websocket ，它的数据是以帧（frame）的形式传递
                            // 可以看到 WebSocketFrame 下面有六个子类
                            // 浏览器请求时 ws://localhost:7001/xxx 表示请求的uri
                            // WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议，保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义 handler ，处理业务逻辑
                            pipeline.addLast(new MyTestWebSocketFrameHandler());

                        }
                    })
            ;

            ChannelFuture channelFuture = serverBootstrap.bind(7001).sync();
            channelFuture.channel().closeFuture().sync();


        } finally {
            bossGroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }
}
