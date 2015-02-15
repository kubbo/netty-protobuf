package com.kubbo.client;

import io.netty.channel.Channel;

/**
 * Created by zhuwei on 2015/2/16.
 */
public interface Client {

    Channel newChannel() throws Exception;

}
