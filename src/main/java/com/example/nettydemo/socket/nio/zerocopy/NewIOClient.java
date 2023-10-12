package com.example.nettydemo.socket.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String filename = "file/protobuf-24.4.zip";

        FileChannel filechannel = new FileInputStream(filename).getChannel();

        long startTime = System.currentTimeMillis();
        filechannel.transferTo(0, filechannel.size(), socketChannel);
        System.out.println("发送的总字节数 = " + filechannel.size() + " 耗时：" + (System.currentTimeMillis() - startTime));

        filechannel.close();
    }
}
