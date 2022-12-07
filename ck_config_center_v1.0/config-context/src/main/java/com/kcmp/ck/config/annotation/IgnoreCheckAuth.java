package com.kcmp.ck.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <strong>实现功能:</strong>.
 * <p>平台默认检查权限，该注解用于一些不许进行权限检查的方法或类上</p>
 *
 * @author kikock
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreCheckAuth {
    boolean value() default true;
}
