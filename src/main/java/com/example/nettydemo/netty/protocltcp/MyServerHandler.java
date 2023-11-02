package com.example.nettydemo.netty.protocltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        String msgString = new String(content, StandardCharsets.UTF_8);

        log.debug("服务器接收到信息如下：");
        log.debug("长度：{}", len);
        log.debug("内容：{}", msgString);
        log.debug("服务器接收到消息包数量：{}", ++this.count);

        String response = "我已经收到你的消息，消息内容是：" + msgString;
        byte[] responseContent = response.getBytes(StandardCharsets.UTF_8);
        int responseLen = responseContent.length;

        MessageProtocol responseMessageProtocol = MessageProtocol.builder()
                .len(responseLen).content(responseContent).build();
        ctx.writeAndFlush(responseMessageProtocol);

    }
}
