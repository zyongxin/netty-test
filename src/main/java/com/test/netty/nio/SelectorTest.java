package com.test.netty.nio;

import jdk.nashorn.internal.ir.CallNode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * selector test
 */
public class SelectorTest {
    public static void main(String[] args) throws IOException {
        //监听端口
        int[] ports = {5000, 5001, 5002, 5003, 5004};

        //创建selector
        Selector selector = Selector.open();

        //监听
        for (int port : ports) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(port);
            //获取和channel想关联的socket
            ServerSocket socket = serverSocketChannel.socket();
            socket.bind(address);
            //注册一个事件selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("port = " + port);
        }
        Set<SocketChannel> channels = new HashSet<>();

        while (true) {
            //返回key
            int number = selector.select();
            System.out.println("number = " + number);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selecjitionKeys = " + selectionKeys);

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
//            这里的处理的操作相当于Acceptor吗
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //如果是连接事件
                if (key.isAcceptable()) {
                    //为什么这里是接受ServerSocketChannel ， 因为在前边注册的就是ServerSocketChannel
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //获取连接的socket
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    channels.add(socketChannel);
                    //注册一个新的事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //移除当前事件，因为这个事件已经被处理了
                    iterator.remove();
                    System.out.println("socketChannel = " + socketChannel);
                    //监听到服务端的可读事件
                } else if (key.isReadable()) {
                    //获取客户端的连接 = java.nio.channels.SocketChannel[connected local=/0:0:0:0:0:0:0:1:5001 remote=/0:0:0:0:0:0:0:1:51929]
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    boolean add = channels.add(socketChannel);
                    System.out.println(add);
                    if(add){
                        System.out.println("this socketChannel not the same as the previous one");
                        System.out.println(channels);
                    }
                    System.out.println(channels.size());
                    int byteRead = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int len = socketChannel.read(byteBuffer);
                        if (len <= 0) {
                            break;
                        }
                        byteBuffer.flip();
                        //将信息返回去
                        socketChannel.write(byteBuffer);
                        byteRead += len;
                    }
                    System.out.println("byteRead = " + byteRead + "  channel " + socketChannel);
                    iterator.remove();
                }
            }
        }
    }
}
