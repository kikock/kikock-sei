package com.kcmp.ck.spring.boot.aop;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.exception.ServiceException;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.SessionUser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by kikock
 * AOP方式收集异常日志
 * @author kikock
 * @email kikock@qq.com
 **/
@Aspect
@Order(3)//使用@Order注解指定切面的优先级，值越小优先级越高
public class ServiceLogCollection {

    public static Integer REPEAT_COMMIT_TIMEOUT = 5;

    static {
        try {
            String timeout = ContextUtil.getGlobalProperty("REPEAT_COMMIT_TIMEOUT");
            REPEAT_COMMIT_TIMEOUT = Integer.valueOf(timeout);
            LogUtil.bizLog("重复操作的REPEAT_COMMIT_TIMEOUT为：{}秒。",REPEAT_COMMIT_TIMEOUT);
        } catch (Exception e) {
            LogUtil.error("获取重复操作REPEAT_COMMIT_TIMEOUT失败，使用默认的超时时间5秒。",e);
        }
    }

    /**
     * 注入缓存模板
     */
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 拦截所有 @Api 注解的类
     * 防止短时间内重复调用
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("com.kcmp.ck.spring.boot.aop.Pointcuts.loggerReCommitPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        //目标类、方法
        String className = method.getDeclaringClass().getName();
        String targetName = point.getTarget().getClass().getName();
        String name = method.getName();
        String preKey = String.format("%s#%s",targetName,name);
        String value = sessionUser.getUserId() + "_" + preKey;
        String key = "Repeat::Commit:" + value;
        LogUtil.debug("around key = {},value = {}",key,value);
        //重复操作超时时间大于0时，执行重复操作判断
        if(REPEAT_COMMIT_TIMEOUT > 0){
            //filter包下的类不用判断重复操作，方法名以get或find开头的也不用判断重复操作
            if(className.indexOf(".filter.") > -1 || name.startsWith("get") || name.startsWith("find")){

            }else {
                // 判断redis缓存中是否存在该key值，如果为true，是当前首次操作允许通过，
                // 如果为false，说明当前已存在缓存，在设置的时间内不允许重复操作
                Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, value);
                if(!flag){
                    LogUtil.bizLog("用户【{}】请勿重复操作，操作方法【{}】！",sessionUser.getUserName(),preKey);
                    throw new ServiceException("请勿重复操作");
                }else {
                    redisTemplate.opsForValue().set(key, value, REPEAT_COMMIT_TIMEOUT,TimeUnit.SECONDS);
                }
            }
        }
        //执行方法
        Object object = point.proceed();
        return object;
    }

    /**
     * 拦截带有 @Service 注解的类的所有方法
     * 方法开始之前执行
     */
    @Before("com.kcmp.ck.spring.boot.aop.Pointcuts.loggerPointcut()")
    public void before(JoinPoint joinPoint) {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        MDC.put("userId", sessionUser.getUserId());
        MDC.put("account", sessionUser.getAccount());
        MDC.put("userName", sessionUser.getUserName());
        MDC.put("tenantCode", sessionUser.getTenantCode());
        MDC.put("accessToken", sessionUser.getAccessToken());

        String argJson = " ";
        Object[] args = joinPoint.getArgs();
        try {
            argJson = args != null ? JsonUtils.toJson(args) : " ";
            MDC.put("args", argJson);
        } catch (Exception ignored) {
        }
        if (LogUtil.isDebugEnabled()) {
            String methodName = joinPoint.getSignature().getName();
            LogUtil.debug("\n\r用户:{}\n\r方法:{}\n\r参数:{}\n\rToken:{}",
                    sessionUser.toString(), methodName, argJson, sessionUser.getAccessToken());
        }
    }

//    /**
//     * 拦截带有 @Service 注解的类的所有方法
//     * 方法执行之后执行
//     * 注：无论该方法是否出现异常都会执行
//     */
//    @After("com.kcmp.ck.spring.boot.aop.Pointcuts.loggerPointcut()")
//    public void after(JoinPoint joinPoint) {
//        SessionUser sessionUser = ContextUtil.getSessionUser();
//        //获取注解
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        //目标类、方法
//        String className = method.getDeclaringClass().getName();
//        String name = method.getName();
//        String ipKey = String.format("%s#%s",className,name);
//        int hashCode = Math.abs(ipKey.hashCode());
//        String key = sessionUser.getUserId() + "_" + hashCode;
//        LOGGER.debug("after ipKey={},hashCode={}",ipKey,hashCode);
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        LOGGER.debug("The method " + methodName + " ends with " + Arrays.asList(args));
//    }
//
//    /**
//     * 拦截带有 @Service 注解的类的所有方法
//     * 方法正常结束后执行的代码
//     * 返回通知是可以访问到方法的返回值的
//     */
//    @AfterReturning(value = "servicePointcut()", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        LOGGER.debug("The method " + methodName + " return with " + result);
//    }

    /**
     * @param joinPoint 常用方法:
     *                  Object[] getArgs(): 返回执行目标方法时的参数。
     *                  Signature getSignature(): 返回被增强的方法的相关信息。
     *                  Object getTarget(): 返回被织入增强处理的目标对象。
     *                  Object getThis(): 返回 AOP 框架为目标对象生成的代理对象。
     * @param throwable 异常对象
     *                  拦截service层异常，记录异常日志，并设置对应的异常信息
     */
    @AfterThrowing(pointcut = "com.kcmp.ck.spring.boot.aop.Pointcuts.loggerPointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Exception {
        LogUtil.error(ExceptionUtils.getMessage(throwable), throwable);
    }
}
