package com.test.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServiceInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //http解码
        pipeline.addLast(new HttpServerCodec());
        //将请求以块的方式处理
        pipeline.addLast(new ChunkedWriteHandler());
        //netty会将请求以分块或者分段的方式接收
        //这个handler的作用是将这些段聚合成一个完整的请求
        //如果超过聚合长度 他里边有对应的方法
        pipeline.addLast(new HttpObjectAggregator(8192));
        //运行一个websocket服务器 ws是路由
        //需要实现他对帧的处理(frame)
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebsocketFrameHandler());
    }
}
