package com.kcmp.ck.eventcenter;

import com.kcmp.ck.vo.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kikock
 * @version 1.0.0
 */
//@Component
public class EventCenter {
    private static final Logger logger = LoggerFactory.getLogger(EventCenter.class);

    //todo 是否考虑线程安全？
    private Map<Class<? extends BaseEvent>, EventHandler<? extends BaseEvent>> regTable = new HashMap<>();

    /**
     * 向事件中心广播一个时间，驱使事件中心执行该事件的处理器
     */
    @SuppressWarnings("unchecked")
    public <E extends BaseEvent> ResponseData publishEvent(E event) {
        logger.info(event.getEventName());

        EventHandler<E> handler = (EventHandler<E>) regTable.get(event.getClass());
        if (handler == null) {
            // 当不存在事件处理对象时，说明未做二次扩展
            return null;
        }
        return handler.handle(event);
    }

    /**
     * 将自己注册为事件中心的某个事件的处理器
     */
    public <E extends BaseEvent> void register(Class<E> event, EventHandler<E> handler) {
        regTable.put(event, handler);
    }
}
