package com.example.nettydemo.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<Object> {

    private ChannelHandlerContext context;

    /**
     * 调用后，会返回一个结果
     */
    private String result;

    /**
     * 客户端调用方法时，传入的参数
     */
    private String para;

    /**
     * 被代理对象调用,发送数据给服务器 -> wait -> 等待被唤醒(channelRead) -> 返回结果
     */
    @Override
    public synchronized Object call() throws Exception {
        log.info("call1被调用");
        context.writeAndFlush(para);
        // 进行wait
        wait();
        log.info("call2被调用");
        return result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive被调用");
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("channelRead被调用");
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    void setPara(String para) {
        this.para = para;
    }


}
