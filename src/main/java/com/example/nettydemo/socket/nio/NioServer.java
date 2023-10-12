package com.example.nettydemo.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个网络通道

        //需要一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把当前的ServerSocketChannel 注册到 Selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {
                //没有事件发生
                continue;
            }
            //如果返回的>0，就获取到相关的SelectionKey集合
            //1.如果返回的>0，表示已经获取到关注的事件
            //2.selector.selectedKeys() 返回关注事件的集合
            // 通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();

                //根据key 对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) {
                    //如果是OP_ACCEPT，有新的客户端连接
                    //给该客户端生成一个SocketChannel
                    //accept() 会阻塞，直到有客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel" + socketChannel.hashCode());
                    //将SocketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector，关注事件为OP_READ，同时给socketChannel
                    //关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));


                }
                // 发生 OP_READ
                if (key.isReadable()) {
                    // 通过key 反向获取到对应channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int count = channel.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[count];
                    buffer.get(bytes, 0, count);
                    buffer.clear();
                    System.out.println("from 客户端" + new String(bytes));
                }


                // 手动从集合中移除当前 selectionKey, 防止从父操作
                keyIterator.remove();



            }

        }

    }
}
