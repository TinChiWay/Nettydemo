package com.example.nettydemo.socket.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 6667;


    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public GroupChatClient() throws IOException {

        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(userName + " is ok...");
    }


    public void sendInfo(String info) {
        info = userName + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        int count = socketChannel.read(buffer);
                        buffer.flip();
                        String msg = new String(buffer.array(), 0, count);
                        System.out.println(msg);

                    }
                    iterator.remove();
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }

    }


}
