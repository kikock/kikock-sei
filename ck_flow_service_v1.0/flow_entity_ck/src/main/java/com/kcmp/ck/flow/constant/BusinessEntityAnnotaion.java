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
@Target(ElementType.TYPE) //类
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessEntityAnnotaion {
    /**
     * 转换对象全路径
     * @return
     */
    public String conditionBean();

    /**
     * 数据访问对象,spring管理的bean名称
     * @return
     */
    public String daoBean();
}
