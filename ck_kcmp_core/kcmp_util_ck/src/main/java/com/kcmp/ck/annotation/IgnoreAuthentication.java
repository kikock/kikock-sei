package com.kcmp.ck.annotation;

import java.lang.annotation.*;

/**
 * Created by kikock
 * 该注解用于一些不许进行登录认证的方法或类上
 * @email kikock@qq.com
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreAuthentication {
    boolean value() default true;
}
