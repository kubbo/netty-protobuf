package com.kubbo;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.kubbo.Constants.*;

/**
 * Created by zhuwei on 2015/2/14.
 */
public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);


    public String getHost() {
        return config.getString(SERVER_HOST);
    }


    public int getPort() {
        return config.getInt(SERVER_PORT);
    }


    public int getBossThreads() {
        return config.getInt(SERVER_NIO_BOSS_THREADS);
    }

    public int getWorkerThreads() {
        return config.getInt(SERVER_NIO_WORKER_THREADS);
    }

    public int getReadTimeout() {
        return config.getInt(SERVER_NIO_READ_TIMEOUT);
    }

    public int getWriteTimeout() {
        return config.getInt(SERVER_NIO_WRITE_TIMEOUT);
    }

    public Map<ChannelOption,Object> getTcpOptions() {
        com.typesafe.config.Config c = config.getConfig(SERVER_TCP);
        Iterator<Map.Entry<String, ConfigValue>> it = c.entrySet().iterator();
        Map<ChannelOption, Object> map = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<String, ConfigValue> next = it.next();
            map.put(ChannelOption.valueOf(next.getKey().toUpperCase()), next.getValue().unwrapped());
        }
        return map;
    }


    public boolean isDebugBinary() {
        return config.getBoolean(SERVER_DEBUG_BINARY);
    }

    public boolean isDebugProtocol() {
        return config.getBoolean(SERVER_DEBUG_PROTOCOL);
    }



    public static final Config getInstance() {
        return Holder.getConfig();
    }



    private com.typesafe.config.Config config;


    private Config() {
        LOGGER.info("Loading {}", Constants.CONFIG);
        config = ConfigFactory.load(Constants.CONFIG);
        LOGGER.info("Loaded {} Successfully", Constants.CONFIG);
    }


    private static class Holder {
        private static final Config config = new Config();

        public static final Config getConfig() {
            return config;
        }
    }

}
