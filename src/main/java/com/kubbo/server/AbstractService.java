package com.kubbo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhuwei on 2015/2/14.
 */
public abstract class AbstractService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    protected ServiceType type;

    protected AtomicBoolean started = new AtomicBoolean(false);

    protected AbstractService(ServiceType type) {
        this.type = type;
    }

    @Override
    public ServiceType getType() {
        return type;
    }


    protected abstract void innerStart();

    protected abstract void innerStop();

    @Override
    public void start() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException(type.getDisplayName() + " already started");
        } else {
            LOGGER.info("Starting service {}", type.getDisplayName());
            innerStart();
        }
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping service {}", type.getDisplayName());
        synchronized (this) {
            if (!started.get()) {
                LOGGER.error("{} already stopped", type.getDisplayName());
            } else {
                this.innerStop();
                started.set(false);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }
}
