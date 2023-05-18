package com.kcmp.ck.eventcenter;

import java.io.Serializable;

/**
 * Created by kikock
 * 事件基类
 * @email kikock@qq.com
 **/
public abstract class BaseEvent implements Serializable {
    private static final long serialVersionUID = 599134713898952862L;
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
