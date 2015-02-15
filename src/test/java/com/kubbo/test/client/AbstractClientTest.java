package com.kubbo.test.client;

import com.kubbo.client.AbstractClient;
import com.kubbo.client.Client;
import com.kubbo.server.AbstractSocketServer;
import com.kubbo.server.Service;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuwei on 2015/2/16.
 */
public class AbstractClientTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientTest.class);
    @Override
    protected void setUp() throws Exception {
        final Service socketServer = new AbstractSocketServer() {
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringEncoder())
                        .addLast(new StringDecoder())
                        .addLast(new AppHandler("Server"));
            }
        };
        Runnable runnable = ()-> {
            socketServer.start();
        };
        new Thread(runnable).start();
    }

    public void testClient() throws Exception {
        Client client = new AbstractClient("localhost",2222){
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new StringEncoder())
                        .addLast(new StringDecoder())
                        .addLast(new AppHandler("Client"));
            }
        };

        Channel channel = client.newChannel();
        assertTrue(channel.isActive());

        channel.writeAndFlush("Test Message for " + this.getClass());
        Thread.sleep(10 * 1000);

    }

    static class AppHandler extends ChannelHandlerAdapter{
        private String name;
        public AppHandler(String name){
            this.name = name;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Thread.sleep(1000);
            LOGGER.info(name + ":" + msg);
            ctx.writeAndFlush(msg);
        }
    }

}
