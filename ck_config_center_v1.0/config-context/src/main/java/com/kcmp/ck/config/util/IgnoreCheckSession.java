package com.kcmp.ck.config.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：在API方法上忽略session检查
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.0                          kikock                新建
 * <p/>
 * *************************************************************************************************
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreCheckSession {
}
