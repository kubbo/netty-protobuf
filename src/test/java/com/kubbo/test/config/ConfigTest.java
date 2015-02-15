package com.kubbo.test.config;

import com.kubbo.Config;
import io.netty.channel.ChannelOption;
import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by zhuwei on 2015/2/14.
 */
public class ConfigTest extends TestCase {

    public void testGetTcpOption() {
        Map<ChannelOption, Object> map = Config.getInstance().getTcpOptions();
        System.out.println(map);
    }
}
