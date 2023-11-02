package com.example.nettydemo.netty.protocltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            String msg = "hello, bro";
            byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);
            int length = msgByte.length;

            MessageProtocol messageProtocol = MessageProtocol.builder().len(length).content(msgByte).build();
            ctx.writeAndFlush(messageProtocol);
        }


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        String msgString = new String(content, StandardCharsets.UTF_8);
        log.debug("客户端接收到信息如下：");
        log.debug("长度：{}", len);
        log.debug("内容：{}", msgString);
        log.debug("客户端接收到消息包数量：{}", ++this.count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("发生异常", cause);
    }
}
