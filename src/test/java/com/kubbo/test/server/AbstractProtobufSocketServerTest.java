package com.kubbo.test.server;

import com.kubbo.server.protobuf.AbstractProtobufSocketServer;
import com.kubbo.test.protocol.MessageProtos;
import io.netty.channel.Channel;
import junit.framework.TestCase;

/**
 * Created by zhuwei on 2015/2/15.
 */
public class AbstractProtobufSocketServerTest extends TestCase {

    private AbstractProtobufSocketServer socketServer;

    @Override
    protected void setUp() throws Exception {
        socketServer = new AbstractProtobufSocketServer(MessageProtos.Message.getDefaultInstance()) {

            @Override
            protected void initAppChannel(Channel channel) {

            }
        };
    }

}
