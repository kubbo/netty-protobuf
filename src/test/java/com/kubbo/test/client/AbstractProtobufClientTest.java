package com.kubbo.test.client;

import com.kubbo.client.protobuf.AbstractProtobufClient;
import com.kubbo.server.protobuf.AbstractProtobufSocketServer;
import com.kubbo.test.protocol.MessageProtos;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuwei on 2015/2/16.
 */
public class AbstractProtobufClientTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProtobufClientTest.class);

    @Override
    protected void setUp() throws Exception {
        final AbstractProtobufSocketServer server = new AbstractProtobufSocketServer(MessageProtos.Message.getDefaultInstance()) {

            @Override
            protected void initAppChannel(Channel channel) {
                channel.pipeline().addLast(new AppHandler("Server"));
            }
        };
        new Thread(()->{server.start();}).start();

    }
    public void testClient() throws Exception {
        AbstractProtobufClient client = new AbstractProtobufClient("localhost", 2222, MessageProtos.Message.getDefaultInstance()) {

            @Override
            protected void initAppChannel(Channel channel) {
                channel.pipeline().addLast(new AppHandler("Client"));
            }
        };
        Channel channel = client.newChannel();
        channel.writeAndFlush(MessageProtos.Message.newBuilder().setId(1).setMsg("Hello world").build());
        Thread.sleep(10 * 1000);
    }


    static class AppHandler extends ChannelHandlerAdapter{
        private String name;

        public AppHandler(String name) {
            this.name = name;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Thread.sleep(2000);
            MessageProtos.Message m = (MessageProtos.Message) msg;
            LOGGER.info(name + " Message:id=" + m.getId() + ",msg=" + m.getMsg());
            ctx.writeAndFlush(msg);
        }
    }
}
