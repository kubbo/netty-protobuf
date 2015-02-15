package com.kubbo.server.protobuf;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuwei on 2015/2/15.
 */
public class ProtobufLogging extends ChannelHandlerAdapter{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufLogging.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageOrBuilder) {
            LOGGER.debug(TextFormat.printToString((MessageOrBuilder) msg));
        }
        ctx.fireChannelRead(msg);
    }



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if (msg instanceof MessageOrBuilder) {
            LOGGER.debug(TextFormat.printToString(((MessageOrBuilder) msg)));
        }
        ctx.write(msg, promise);

    }
}
