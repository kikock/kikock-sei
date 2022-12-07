package com.kcmp.ck.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by kikock
 * 启动类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@SpringBootApplication
@EntityScan(basePackages = "com.kcmp.ck.config.entity")
@EnableJpaRepositories(basePackages = {"com.kcmp.ck.center.dao"})
@ComponentScan(value = {"com.kcmp.ck.center.service", "com.kcmp.ck.center.controller", "com.kcmp.ck.center.config","com.kcmp.ck.center.dao"})
@EnableScheduling
public class ConfigCenterRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterRestApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  配置中心--启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
