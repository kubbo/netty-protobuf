package com.kubbo.test.server;

import com.kubbo.server.AbstractSocketServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import junit.framework.TestCase;

/**
 * Created by zhuwei on 2015/2/15.
 */
public class AbstractSocketServerTest extends TestCase {

    private AbstractSocketServer socketServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.socketServer = new AbstractSocketServer() {
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
            }
        };
    }

    public void testStart() throws InterruptedException {
        socketServer.start();
        assertTrue(socketServer.isStarted());
        Thread.sleep(Integer.MAX_VALUE);
        socketServer.stop();
        assertFalse(socketServer.isStarted());
    }


}
