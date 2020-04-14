package com.test.netty.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 编码问题
 */
public class CodeTest {
    public static void main(String[] args) throws Exception {
        String read = "E:\\idea\\workspace\\netty-test\\src\\main\\java\\com\\test\\netty\\nio\\code.txt";
        String out = "E:\\idea\\workspace\\netty-test\\src\\main\\java\\com\\test\\netty\\nio\\code1.txt";

        RandomAccessFile input = new RandomAccessFile(read, "rw");
        RandomAccessFile output = new RandomAccessFile(out, "rw");

        FileChannel inputFileChannel = input.getChannel();
        FileChannel outputFileChannel = output.getChannel();

        long length = new File(read).length();
        //内存映射
        MappedByteBuffer map = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);

        Charset charset = StandardCharsets.ISO_8859_1;

        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        //将IO buffer转换成字符buffer
        CharBuffer charBuffer = decoder.decode(map);
        System.out.println(charBuffer.toString());
        //将字符buffer转换为字节buffer
        ByteBuffer byteBuffer = encoder.encode(charBuffer);
//        Charset utf = StandardCharsets.UTF_8;
//        ByteBuffer byteBuffer = utf.newEncoder().encode(charBuffer);

        //使用通道把buffer写入
        outputFileChannel.write(byteBuffer);

        input.close();
        output.close();
    }
}
