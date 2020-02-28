package com.test.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件
 */
public class MapperBufferText {
    public static void main(String[] args) throws Exception {
        String path = "E:\\idea\\workspace\\netty-test\\src\\main\\java\\com\\test\\netty\\nio\\niotext.txt";
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();
        MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0, (byte) 'a');
        map.put(3, (byte) 'b');

        randomAccessFile.close();
    }
}
