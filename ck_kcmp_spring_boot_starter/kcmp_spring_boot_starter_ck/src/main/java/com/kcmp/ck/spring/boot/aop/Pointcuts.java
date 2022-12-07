package com.kcmp.ck.spring.boot.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by kikock
 * 用于声明切入点表达式
 * @email kikock@qq.com
 **/
public class Pointcuts {

    /**
     * com.kcmp包下public方法的任意连接点
     */
    @Pointcut("execution(public * com.kcmp..*.*(..)))")
    public void kcmpPointcut() {
    }

    /**
     * 实现了 BaseService 接口/类的目标对象的任意连接点（在Spring AOP中只是方法执行）
     */
    @Pointcut("target(com.kcmp.core.jx.service.BaseService)")
    public void baseServicePointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void servicePointcut() {
    }

    /**
     * 使用@within(io.swagger.annotations.Api) 拦截带有 @Api 注解的类的所有方法
     */
    @Pointcut("@within(io.swagger.annotations.Api)")
    public void apiPointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Component) 拦截带有 @Component 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void componentPointcut() {
    }

    /**
     * 使用execution(public * *(..))表达式拦截所有公共方法
     */
    @Pointcut("execution(public * *(..))")
    public void publicPointcut() {
    }

    /**
     * 逻辑层异常日志切入点.
     */
    @Pointcut("kcmpPointcut() && (baseServicePointcut() || componentPointcut() || servicePointcut())")
    public void loggerPointcut() {
    }

    /**
     * 接口层重复操作切入点.
     */
    @Pointcut("kcmpPointcut() && apiPointcut()")
    public void loggerReCommitPointcut() {
    }
}
