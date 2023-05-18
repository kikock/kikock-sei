package com.kcmp.ck.annotation;

import java.lang.annotation.*;

/**
 * Created by kikock
 * 平台默认检查权限，该注解用于一些不许进行权限检查的方法或类上
 * @email kikock@qq.com
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreCheckAuth {
    boolean value() default true;
}
