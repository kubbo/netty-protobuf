package com.kubbo.server;

import com.kubbo.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * Created by zhuwei on 2015/2/14.
 */
public abstract class AbstractSocketServer extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSocketServer.class);

    protected Config config = Config.getInstance();

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public AbstractSocketServer() {
        super(ServiceType.SOCKET);
    }

    protected abstract void initChannel(Channel channel);

    @Override
    protected void innerStart() {

        this.bossGroup = new NioEventLoopGroup(config.getBossThreads());
        this.workerGroup = new NioEventLoopGroup(config.getWorkerThreads());
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        if (config.isDebugBinary()) {
                            ch.pipeline().addLast("BinaryLogging", new LoggingHandler());
                        }

                        AbstractSocketServer.this.initChannel(ch);
                    }
                });
        config.getTcpOptions().forEach((op, val) -> {
            b.childOption(op, val);
        });

        try {
            ChannelFuture f = b.bind(new InetSocketAddress(config.getHost(), config.getPort())).sync();
            LOGGER.info("Bind Channel{host={},port={},options={}}", config.getHost(), config.getPort(), config.getTcpOptions());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("SocketService exit", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    protected void innerStop() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}
