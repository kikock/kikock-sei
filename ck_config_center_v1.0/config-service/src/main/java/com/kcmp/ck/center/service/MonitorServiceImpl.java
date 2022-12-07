package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.MonitorService;
import com.kcmp.ck.config.entity.dto.ResponseData;
import com.kcmp.ck.config.util.DateUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by kikock
 * 监控服务
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class MonitorServiceImpl implements MonitorService {

    /**
     * 监控业务健康
     *
     * @return
     */
    @Override
    public ResponseData health() {
        String msg = DateUtils.formatTime(new Date()) + " Request Uri: " + MDC.get("requestUrl");
        System.out.println(msg);
        return ResponseData.build().setMessage("OK").setData(msg);
    }
}
