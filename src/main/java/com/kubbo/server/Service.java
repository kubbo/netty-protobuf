package com.kubbo.server;

/**
 * Created by zhuwei on 2015/2/14.
 */
public interface Service {

    ServiceType getType();

    void start();

    void stop();

    boolean isStarted();
}
