package com.kubbo.client.protobuf;

import com.google.protobuf.MessageLite;
import com.kubbo.client.AbstractClient;
import io.netty.channel.Channel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by zhuwei on 2015/2/16.
 */
public abstract class AbstractProtobufClient extends AbstractClient {
    private MessageLite messageLite;
    protected AbstractProtobufClient(String remote, int port, MessageLite messageLite) {
        super(remote, port);
        this.messageLite = messageLite;
    }

    protected abstract void initAppChannel(Channel channel);


    @Override
    protected void initChannel(Channel channel) {

        channel.pipeline().addLast("FrameDecoder", new ProtobufVarint32FrameDecoder())
                .addLast("ProtoBufDecoder", new ProtobufDecoder(messageLite))
                .addLast("FrameEncoder", new ProtobufVarint32LengthFieldPrepender())
                .addLast("ProtoBufEncoder", new ProtobufEncoder());
        AbstractProtobufClient.this.initAppChannel(channel);
    }


}
