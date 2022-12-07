package com.kcmp.ck.eventcenter;

import com.kcmp.ck.vo.ResponseData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 * 事件处理类
 * @email kikock@qq.com
 **/
public abstract class EventHandler<E extends BaseEvent> implements InitializingBean {
    @Autowired
    public EventCenter eventCenter;

    public abstract ResponseData handle(E event);
}
