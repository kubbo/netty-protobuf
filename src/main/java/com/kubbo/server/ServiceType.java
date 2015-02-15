package com.kubbo.server;

/**
 * Created by zhuwei on 2015/2/14.
 */
public enum ServiceType {

    SOCKET("socket-service"),;


    private String displayName;

    private ServiceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
