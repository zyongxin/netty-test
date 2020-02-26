package com.test.netty.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap;
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(loopGroup).channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());

            ChannelFuture localhost = bootstrap.connect("localhost", 8899).sync();
            localhost.channel().closeFuture().sync();
        } finally {
            loopGroup.shutdownGracefully();
        }

    }
}
