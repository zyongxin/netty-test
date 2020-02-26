package com.test.netty.idle;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ServiceInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //没有读写时 会触发IdleStateHandler
        pipeline.addLast(new IdleStateHandler(5, 7, 3,
                TimeUnit.SECONDS));
        pipeline.addLast(new ServerHandler());
    }
}
