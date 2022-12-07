package com.kcmp.ck.eventcenter;

import java.io.Serializable;

/**
 * 事件基类
 *
 * @author kikock
 * @version 1.0.0
 */
public abstract class BaseEvent implements Serializable {
    private static final long serialVersionUID = 7099057708183571937L;
    /**
     * 事件名
     */
    private final String eventName;
    /**
     * 事件发生时间戳
     */
    private final long timestamp = System.currentTimeMillis();

    public BaseEvent(String eventName) {
        if (eventName == null) {
            throw new IllegalArgumentException("null source");
        }
        this.eventName = eventName;
    }

    public final String getEventName() {
        return eventName;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[eventName=" + eventName + "]";
    }
}
