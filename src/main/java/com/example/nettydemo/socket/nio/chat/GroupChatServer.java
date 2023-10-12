package com.example.nettydemo.socket.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChanal;

    public static final int PORT = 6667;

    public GroupChatServer() {
        try {

            selector = Selector.open();

            listenChanal = ServerSocketChannel.open();

            listenChanal.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenChanal.configureBlocking(false);

            listenChanal.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {

        try {
            while (true) {

                int count = selector.select();

                if (count > 0) {
                    //
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {

                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChanal.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + " 上线 ");

                        }

                        if (key.isReadable()) {
                            readData(key);
                        }

                        iterator.remove();
                    }

                } else {
                    System.out.println("等待中...");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }

    private void readData(SelectionKey key) {

        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            buffer.flip();
            byte[] bytes = new byte[count];
            buffer.get(bytes, 0, count);
            buffer.clear();
            if (count > 0) {
                String msg = new String(bytes);
                System.out.println("from 客户端：" + msg);

                // 转发消息
                sendInfoToOtherClients(msg, channel);

            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了...");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }


    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {

        System.out.println("服务器转发消息中...");
        for (SelectionKey key : selector.keys()) {

            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                SocketChannel dest = (SocketChannel)targetChannel;
                dest.write(buffer);
            }
        }


    }


    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
