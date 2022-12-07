package com.kcmp.ck.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <strong>实现功能:</strong>.
 * <p>用于注解类或属性的备注数据，这些元数据用于运行时动态内容生成</p>
 *
 * @author kikock
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE})
public @interface Remark {
    /**
     * @return 简要注解说明
     */
    String value();

    /**
     * @return 排序
     */
    int rank() default 0;

    /**
     * @return 注释说明：用于描述代码内部用法说明，一般不用于业务
     */
    String comments() default "";
}
