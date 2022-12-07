package com.kcmp.ck.center.service.config;

import com.kcmp.ck.config.util.ZkClient;
import com.kcmp.ck.context.BaseApplicationContext;
import com.kcmp.ck.context.common.ConfigConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kikock
 * CXF 功能配置
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Configuration
public class ZookeeperConfig {
    @Value("${zk_connectString}")
    private String zkHost;

    @Bean
    public BaseApplicationContext ecmpContext() {
        return new BaseApplicationContext();
    }

    @Bean(destroyMethod = "stop")
    public ZkClient zkClient() {
        //实例化zk客户端
        ZkClient zkClient = new ZkClient(zkHost, ConfigConstants.ZK_NAME_SPACE);
        //初始化zk
        zkClient.init();
        return zkClient;
    }
}
