/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.example.nettydemo.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

/**
 * Handler implementation for the echo server.
 */
@Sharable
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final EventExecutorGroup GROUP = new DefaultEventExecutorGroup(16);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("echo server handler:{}", Thread.currentThread().getName());
        //ctx.channel().eventLoop().execute(() -> {
        //    try {
        //        Thread.sleep(10 * 1000);
        //        log.info("echo server execute:{}", Thread.currentThread().getName());
        //        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,小喵喵 ～ 2", StandardCharsets.UTF_8));
        //    } catch (InterruptedException e) {
        //        log.warn("发生异常", e);
        //    }
        //});
        GROUP.submit(() -> {
            // 接收客户端消息
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String body = new String(bytes, StandardCharsets.UTF_8);
            try {
                Thread.sleep(9 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("echo server execute:{}", Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,小喵喵 ～ 1", StandardCharsets.UTF_8));

            return null;
        });
        log.info("go on");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
