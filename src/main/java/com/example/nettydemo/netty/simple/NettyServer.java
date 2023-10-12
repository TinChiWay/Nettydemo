package com.example.nettydemo.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        // 创建 BossGroup 和 WorkerGroup
        // 说明：
        // 1. 创建两个线程组 bossGroup 和 workerGroup
        // 2. bossGroup 只是处理连接请求，真正的和客户端业务处理，会交给 workerGroup 完成
        // 3. 两个都是无限循环
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 使用链式编程来进行设置
        // 设置两个线程组
        bootstrap.group(boosGroup, workerGroup)
                // 使用 NioServerSocketChannel 作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                // 设置线程队列得等待连接个数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设置 workerGroup 的 EventLoop 对应的管道处理器
                // 创建一个通道初始化对象（匿名对象）
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // 给 pipeline 设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(null);
                    }
                });

        System.out.println("...服务器 is ready...");

        // 绑定一个端口并且同步，生成了一个 ChannelFuture 对象
        // 启动服务器（并绑定端口）
        ChannelFuture cf = bootstrap.bind(6668).sync();

        // 对关闭通道进行监听
        cf.channel().closeFuture().sync();
    }
}
