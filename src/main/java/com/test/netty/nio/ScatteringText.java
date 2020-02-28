package com.test.netty.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering gathering
 */
public class ScatteringText {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        //读特定的长度
        int messageLength = 2 + 3 + 4;

        ByteBuffer[] byteBuffers = new ByteBuffer[3];

        //读特点字节的buffer
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        //接受 使用telnet连接
        SocketChannel channel = serverSocketChannel.accept();

        while (true) {

            //读到的长度
            int byteRead = 0;

            //如果读到的小于要接收的  就不断读
            while (byteRead < messageLength) {
                long r = channel.read(byteBuffers);
                byteRead += r;
                System.out.println("byteRead " + byteRead);

                Arrays.stream(byteBuffers).map(buffer -> "position " + buffer.position() + ", limit " + buffer.limit())
                        .forEach(System.out::println);
            }
            //读满了写回给客户端
            Arrays.asList(byteBuffers).forEach(Buffer::flip);
            long byteWrite = 0;
            //一次没写完就继续写
            while (byteWrite < messageLength) {
                long r = channel.write(byteBuffers);
                byteWrite += r;
            }
            //清除
            Arrays.asList(byteBuffers).forEach(Buffer::clear);

            System.out.println("byteRead " + byteRead + " ,byteWrite " + byteWrite + " ,messageLength" + messageLength);
        }
    }
}
