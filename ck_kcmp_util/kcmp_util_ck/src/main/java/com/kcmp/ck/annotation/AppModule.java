package com.kcmp.ck.annotation;

import java.lang.annotation.*;

/**
 * Created by kikock
 * 该注解用于API接口类上，以此获取对应应用模块的接口基地址
 * @email kikock@qq.com
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
public @interface AppModule {
    /**
     * @return 系统应用模块代码
     */
    String value();
}
