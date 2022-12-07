package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.core.jx.service.MonitorService;
import com.kcmp.ck.spring.boot.aop.ServiceLogCollection;
import org.springframework.context.annotation.Bean;

/**
 * Created by kikock
 * 服务配置
 * @email kikock@qq.com
 **/
public class ServiceConfig {

    @Bean
    public MonitorService monitorService() {
        return new MonitorService();
    }

    @Bean
    public ServiceLogCollection serviceLogCollection() {
        return new ServiceLogCollection();
    }
}
