package com.example.nettydemo.netty.rpc.netty;

import com.example.nettydemo.netty.rpc.customer.ClientBootStrap;
import com.example.nettydemo.netty.rpc.provider.HelloServerImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("msg:{}", msg);
        if (msg.toString().startsWith(ClientBootStrap.PROVIDER_NAME)) {
            String result = new HelloServerImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
