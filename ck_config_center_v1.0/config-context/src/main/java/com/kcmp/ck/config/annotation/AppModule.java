package com.kcmp.ck.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <strong>实现功能:</strong>.
 * <p>该注解用于API接口类上，以此获取对应应用模块的接口基地址</p>
 *
 * @author kikock
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
public @interface AppModule {
    /**
     * @return 系统应用模块代码
     */
    String value();
}
