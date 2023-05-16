package com.kcmp.test.ck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by kikock
 *
 * @email kikock@qq.com
 * @create 2019-12-20 16:51
 **/
@SpringBootApplication
public class TestServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
        System.out.println("测试服务--启动成功");
    }
}
