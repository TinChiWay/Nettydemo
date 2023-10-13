package com.example.nettydemo.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class NettyBytebuf02 {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", StandardCharsets.UTF_8);

        // 使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            // 将 content 转成字符串
            System.out.println(new String(content, StandardCharsets.UTF_8));

            System.out.println("byteBuf=" + byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            // 可读取的字节数
            int len = byteBuf.readableBytes();

            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            // 按照某个范围读取
            System.out.println(byteBuf.getCharSequence(0, 4, StandardCharsets.UTF_8));

        }


    }
}
