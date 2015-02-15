package com.kubbo.server.protobuf;

import com.google.protobuf.MessageLite;
import com.kubbo.server.AbstractSocketServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by zhuwei on 2015/2/15.
 */
public abstract class AbstractProtobufSocketServer extends AbstractSocketServer {


    private MessageLite messageLite;
    public AbstractProtobufSocketServer(MessageLite messageLite) {
        this.messageLite = messageLite;
    }


    protected abstract void initAppChannel(Channel channel);


    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        //protobuf decoder
        pipeline.addLast("FrameDecoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("ProtoBufDecoder", new ProtobufDecoder(messageLite));

        //protobuf encoder
        pipeline.addLast("FrameEncoder", new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("ProtoBufEncoder", new ProtobufEncoder());

        pipeline.addLast("IdleStateHandler", new IdleStateHandler(config.getReadTimeout(), config.getWriteTimeout(), 0));

        //logging protobuf message
        if (config.isDebugProtocol()) {
            pipeline.addLast("ProtobufLogging", new ProtobufLogging());
        }

        //app handler
        initAppChannel(channel);

    }
}
