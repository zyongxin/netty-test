package com.test.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;

/**
 * use nio
 */
public class NioTest {
    public static void main(String[] args) throws Exception {
        String path = "E:\\idea\\workspace\\netty-test\\src\\main\\java\\com\\test\\netty\\nio\\niotext.txt";
        ByteBuffer buffer = ByteBuffer.allocate(512);
        //directBuffer
//        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        //read only  数据是不共享的
//        buffer.asReadOnlyBuffer()
        //截取原先的buff 数据是共享的
//        buffer.slice()
        write(path , buffer , "hello word");
        read(path, buffer);
        //报错 因为多次调用flip 并且写入的数据越来越少，limit的值会越来越小 所以会报错
        write(path , buffer , "hello wordaaaa");
        //这个时候可以调用clear 清零
        buffer.clear();
        write(path , buffer , "hello wordaaaa");
    }

    private static void read(String path , ByteBuffer buffer) throws Exception {
        System.out.println("read----------------------");
        FileInputStream fileInputStream = new FileInputStream(path);
        FileChannel channel = fileInputStream.getChannel();


        //从channel写数据到buffer
        //相当于 new Buffer(new OutPut(channel))
        channel.read(buffer);

        System.out.println(buffer.position());
        System.out.println(buffer.limit());

        //改变缓冲区指针的位置
        //如果不调用这个方法去执行读的话，会在尾部的位置开始读，并且可以读的长度是512
        //也就是说调用flip之后，读写指针指到缓存头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)。
        buffer.flip();

        System.out.println(buffer.position());
        System.out.println(buffer.limit());
//        while (buffer.hasRemaining()) {
//            byte b = buffer.get();
//            System.out.println((char) b);
//        }
        fileInputStream.close();
    }

    private static void write(String path , ByteBuffer buffer , String msg) throws Exception {
        System.out.println("write----------------------");
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        FileChannel channel = fileOutputStream.getChannel();

        byte[] bytes = msg.getBytes();

        for (byte aByte : bytes) {
            buffer.put(aByte);
        }
        //因为要写到channel 所以翻转缓冲区 将指针移到头部
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        buffer.flip();

        System.out.println(buffer.position());
        System.out.println(buffer.limit());

        channel.write(buffer);
        fileOutputStream.close();
    }
}
