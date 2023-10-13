package com.example.nettydemo.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * 说明
 * 1. 我们自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 * 2. 这时我们自定义一个Handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    // 读取数据实际(这里我们可以读取客户端发送的消息)

    /**
     * 1. ChannelHandlerContext ctx:上下文对象，含有管道pipeline，通道channel，地址
     * 2. Object msg: 就是客户端发送的数据 默认Object
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {







        // 比如这里我们有一个非常耗时长的业务 -> 异步执行 -> 提交该 channel 对应的 NIOEventLoop 的 taskQueue 中，
        // 该 taskQueue 会被 NIOEventLoop 的线程处理
        // 解决方案1：用户程序自定义的普通任务

        //  ctx.channel().eventLoop().execute(() -> {
        //      try {
        //          Thread.sleep(10 * 1000);
        //      } catch (InterruptedException e) {
        //          System.out.println("发生异常" + e.getMessage());
        //      }
        //      ctx.writeAndFlush(Unpooled.copiedBuffer("hello,小喵喵 ～ 2", StandardCharsets.UTF_8));
        //  });
        //
        //  // 解决方案2：用户自定义定时任务 -> 该任务是提交到 scheduleTaskQueue 中
        // ctx.channel().eventLoop().schedule(() -> {
        //     try {
        //         Thread.sleep(10 * 1000);
        //     } catch (InterruptedException e) {
        //         System.out.println("发生异常" + e.getMessage());
        //     }
        //     ctx.writeAndFlush(Unpooled.copiedBuffer("hello,小喵喵 ～ 4", StandardCharsets.UTF_8));
        // }, 5, TimeUnit.SECONDS);
        //
        //
        //
        //
        // System.out.println("go on ...");

        // System.out.println("[Server] 服务器读取线程：" + Thread.currentThread().getName());
        // System.out.println("[Server] ctx = " + ctx);
        // System.out.println("[Server] 看看 channel 和 pipeline 的关系");
        // Channel channel = ctx.channel();
        //
        // // 本质是一个双向链表，出栈入栈
        // ChannelPipeline pipeline = ctx.pipeline();
        //
        // // 将msg转成一个ByteBuf
        // // ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
        // ByteBuf buffer = (ByteBuf) msg;
        // System.out.println("客户端发送的消息是：" + (buffer.toString(StandardCharsets.UTF_8)));
        // System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,小喵喵～", StandardCharsets.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
