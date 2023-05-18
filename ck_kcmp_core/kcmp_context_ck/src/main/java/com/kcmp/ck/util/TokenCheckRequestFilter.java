package com.kcmp.ck.util;

import com.kcmp.ck.annotation.IgnoreCheckSession;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by kikock
 * token验证检查
 * @email kikock@qq.com
 **/
@Provider
@Priority(value = 5)
public class TokenCheckRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCheckRequestFilter.class);
    @Context
    private ResourceInfo resinfo;

    /**
     * 在将请求调度到资源之前调用的筛选器方法
     * @param requestContext request context.
     * @throws IOException if an I/O exception occurs.
     * @see PreMatching
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        //判断是否是swagger.json
        if (StringUtils.startsWithAny(path, "api-docs", "swagger", "actuator", "instances")) {
            return;
        }
        if (resinfo == null || resinfo.getResourceClass() == null || resinfo.getResourceMethod() == null) {
            return;
        }

        String traceId = requestContext.getHeaderString("traceId");
        if (StringUtils.isBlank(traceId)) {
            traceId = IdGenerator.uuid2();
        }
        MDC.put("traceId", traceId);
        MDC.put("requestUrl", requestContext.getUriInfo().getAbsolutePath().toString());

        MDC.put("UserAgent", requestContext.getHeaderString("User-Agent"));

        if (StringUtils.startsWith(path, "monitor")) {
            return;
        }

        boolean ignoreCheckSession = false;
        //检查API服务接口是否忽略session检查
        if (resinfo.getResourceClass().isAnnotationPresent(IgnoreCheckSession.class)) {
            LOGGER.debug(resinfo.getResourceClass().getName() + " 忽略session检查 - " + path);
            ignoreCheckSession = true;
        }
        //检查API方法是否忽略session检查
        else if (resinfo.getResourceMethod().isAnnotationPresent(IgnoreCheckSession.class)) {
            LOGGER.debug(resinfo.getResourceMethod().getName() + " 忽略session检查 - " + path);
            ignoreCheckSession = true;
        }

        Response response;
        Response.ResponseBuilder responseBuilder;

        //check token
        String accessToken = requestContext.getHeaderString(ContextUtil.AUTHORIZATION_KEY);
        if (StringUtils.isNotBlank(accessToken)) {
            LOGGER.debug("accessToken {}", accessToken);
            if (accessToken.startsWith("Bearer ")) {
                // 截取token
                accessToken = accessToken.substring("Bearer ".length());
            }

            try {
                SessionUser sessionUser = ContextUtil.getSessionUser(accessToken);
                LOGGER.debug("当前用户：{}", sessionUser);
            } catch (Exception e) {
                if (!ignoreCheckSession) {
                    LOGGER.error("JWT解析异常 --> " + accessToken, e);
                    //jwt解析异常返回403错误
                    responseBuilder = Response.serverError();
                    response = responseBuilder.status(Response.Status.FORBIDDEN).build();
                    requestContext.abortWith(response);
                }
            }
            return;
        }
        if (!ignoreCheckSession) {
            LOGGER.error("{} 接口调用Token不能为空！", path);
            //如果过期则返回401错误
            responseBuilder = Response.serverError();
            response = responseBuilder.status(Response.Status.UNAUTHORIZED).build();
            requestContext.abortWith(response);
        }
    }
}
