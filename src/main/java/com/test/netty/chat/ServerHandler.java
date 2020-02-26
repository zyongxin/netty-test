package com.test.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 接收string信息
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    //把连接的保存在一个组里边
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        channels.forEach(channel -> {
            if (channel != ctx.channel()) {
                channel.writeAndFlush(ctx.channel().remoteAddress() + " 发送消息 :" + msg+ "\n");
            } else {
                channel.writeAndFlush("[自己]:" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //可以直接使用group里边各个channel的方法
        channels.flushAndWrite("[服务器]-" + channel.remoteAddress() + " 加入\n");
        //避免发给自己 add在写后边
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.flushAndWrite("[服务器]-" + channel.remoteAddress() + " 离开\n");
        //这里为什么不需要从group里将离开的channel移除
        //因为group是由Netty维护的，当触发了这个事件，netty会自动把他移除出去
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线"+ "\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 下线"+ "\n");
    }
}
