package com.example.nettydemo.netty.boundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx           上下文对象
     * @param in            入站
     * @param out           list 集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // long 8个字节，需要判断有8个字节才能读取一个long
        System.out.println("MyByteToLongDecoder decode 被调用");
        if (in.readableBytes() >= 8){
            out.add(in.readLong());
        }

    }
}
