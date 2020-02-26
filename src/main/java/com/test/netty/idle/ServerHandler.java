package com.test.netty.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 读写空闲处理
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 事件被触发后 调用这个方法 然后这个方法会把时间传给管道里的下一个Handler
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = null;
            switch (event.state()) {
                case READER_IDLE:
                    type = "读空闲";
                    break;
                case WRITER_IDLE:
                    type = "写空闲";
                    break;
                case ALL_IDLE:
                    type = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "  " + type);
            ctx.close();
        }

    }
}
