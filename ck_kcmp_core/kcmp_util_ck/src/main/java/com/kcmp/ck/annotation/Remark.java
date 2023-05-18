package com.kcmp.ck.annotation;

import java.lang.annotation.*;

/**
 * Created by kikock
 * 用于注解类或属性的备注数据，这些元数据用于运行时动态内容生成
 * @email kikock@qq.com
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE })
public @interface Remark {
    /**
     * 简要注解说明
     */
    String value();
    /**
     * 排序
     */
    int rank() default 0;
    /**
     * 注释说明，用于描述代码内部用法说明，一般不用于业务
     */
    String comments() default "";
}
