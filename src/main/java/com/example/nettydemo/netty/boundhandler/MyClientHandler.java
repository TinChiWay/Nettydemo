package com.example.nettydemo.netty.boundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("server ip = " + ctx.channel().remoteAddress());
        System.out.println("收到服务器消息 = " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        // ctx.writeAndFlush(Unpooled.copiedBuffer(""));

        ctx.writeAndFlush(1232L);
    }
}
