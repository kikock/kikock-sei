package com.kcmp.ck.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略登录认证.
 * 该注解用于一些不许进行登录认证的方法或类上
 *
 * @author kikock
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreAuthentication {
    boolean value() default true;
}
