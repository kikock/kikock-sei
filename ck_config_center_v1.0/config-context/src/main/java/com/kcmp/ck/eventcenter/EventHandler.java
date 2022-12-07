package com.kcmp.ck.eventcenter;

import com.kcmp.ck.vo.ResponseData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事件处理器
 *
 * @author kikock
 * @version 1.0.0
 */
public abstract class EventHandler<E extends BaseEvent> implements InitializingBean {
    @Autowired
    public EventCenter eventCenter;

    public abstract ResponseData handle(E event);
}
