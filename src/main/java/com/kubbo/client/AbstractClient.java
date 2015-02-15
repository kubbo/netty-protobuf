package com.kubbo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



/**
 * Created by zhuwei on 2015/2/16.
 */
public abstract class AbstractClient implements Client {

    private  final EventLoopGroup group = new NioEventLoopGroup();
    protected String remote;
    protected int port;

    protected AbstractClient(String remote, int port) {
        this.remote = remote;
        this.port = port;
    }

    protected abstract void initChannel(Channel channel);

    @Override
    public Channel newChannel() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        return bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        AbstractClient.this.initChannel(ch);
                    }
                }).connect(remote, port).sync().channel();

    }
}
