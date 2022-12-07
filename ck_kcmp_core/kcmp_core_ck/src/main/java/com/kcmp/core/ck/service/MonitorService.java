package com.kcmp.core.ck.service;

import com.kcmp.ck.util.DateUtils;
import com.kcmp.ck.vo.ResponseData;
import com.kcmp.core.ck.api.IMonitorService;
import org.slf4j.MDC;

import java.util.Date;

/**
 * Created by kikock
 * 监控服务
 * @email kikock@qq.com
 **/
public class MonitorService implements IMonitorService {

    @Override
    public ResponseData health() {
        String msg = DateUtils.formatTime(new Date()) + " Request Uri: " + MDC.get("requestUrl");
        System.out.println(msg);
        return ResponseData.build().setMessage("OK");
    }
}
