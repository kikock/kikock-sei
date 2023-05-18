package com.kcmp.ck.flow.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kikock
 * 条件表达式自定义注解
 * @email kikock@qq.com
 **/
//@Target(ElementType.FIELD) //字段、枚举的常量
@Target(ElementType.METHOD) //方法
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionAnnotaion {
    /**
     * 字段中文名称
     * @return
     */
    public String name();

    /**
     * 排序，从大到小
     * @return
     */
    public int rank() default 0;

    /**
     * 前端表达式是否可见
     * @return
     */
    public boolean canSee() default true;
}
