package com.example.nettydemo.netty.codec2;

import com.example.nettydemo.netty.codec.StudentPOJO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

// public class NettyClientHandler extends ChannelInboundHandlerAdapter {
public class NettyClientHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    /**
     * 当通道就绪就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;

        if (0 == random) {
            myMessage = MyDataInfo.MyMessage.newBuilder().
                    setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(5).setName("学生").build())
                    .build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("李四").build())
                    .build();
        }

        ctx.writeAndFlush(myMessage);
    }

    /**
     * 当通道有读取事件时，会触发
     */
    // @Override
    // public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //
    //     ByteBuf buf = (ByteBuf) msg;
    //     System.out.println("服务器回复的消息：" + buf.toString(StandardCharsets.UTF_8));
    //     System.out.println("服务器的地址：" + ctx.channel().remoteAddress());
    //
    // }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.Student student) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
