package com.example.nettydemo.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            switch (event.state()) {
                case READER_IDLE:
                    System.out.print("读空闲");
                    break;
                case WRITER_IDLE:
                    System.out.print("写空闲");
                    break;
                case ALL_IDLE:
                    System.out.print("读写空闲");
                    break;
                default:
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "超时事件");
            // 如果发生空闲，我们关闭通道
            // ctx.channel().close();
        }
    }
}
