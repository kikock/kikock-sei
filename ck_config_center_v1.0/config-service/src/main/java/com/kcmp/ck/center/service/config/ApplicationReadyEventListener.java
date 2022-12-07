package com.kcmp.ck.center.service.config;

import com.kcmp.ck.config.util.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;


/**
 * Created by kikock
 * 上下文已经准备完毕的时候触发
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent>, Ordered, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    private ZkClient zkClient = null;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent readyEvent) {
        ApplicationContext context = readyEvent.getApplicationContext();
        //服务注册
        zkClient = context.getBean(ZkClient.class);
        String serviceAddress = context.getEnvironment().getProperty("RPC_ADDRESS");
        if (StringUtils.isNotBlank(serviceAddress)) {
            logger.info("RPC服务地址{}", serviceAddress);
            zkClient.register("CONFIG_CENTER", serviceAddress);
        } else {
            logger.warn("没有配置[RPC_ADDRESS]环境变量，无法提供RPC服务");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void destroy() throws Exception {
        if (zkClient != null) {
            zkClient.stop();
        }
    }
}
