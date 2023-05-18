package com.kcmp.ck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by kikock
 * 启动类
 * @author kikock
 * @email kikock@qq.com
 **/
@SpringBootApplication
@EntityScan(basePackages = "com.kcmp.ck.flow.entity")
@EnableJpaRepositories(basePackages = {"com.kcmp.ck.flow.dao"})
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }
}
