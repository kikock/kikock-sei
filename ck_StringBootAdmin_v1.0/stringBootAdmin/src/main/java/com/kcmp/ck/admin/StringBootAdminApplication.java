package com.kcmp.ck.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @project_name: ck_config_center_v1.0
 * @description: 启动项
 * @create_name: kikock
 * @create_date: 2022-04-12 10:07
 **/
//StringBootAdmin注解
@EnableAdminServer
@SpringBootApplication
public class StringBootAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(StringBootAdminApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  StringBoot 管理平台--启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
