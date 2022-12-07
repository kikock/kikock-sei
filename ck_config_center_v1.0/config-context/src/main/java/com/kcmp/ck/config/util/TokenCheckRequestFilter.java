package com.kcmp.ck.config.util;


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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

/**
 * token验证检查
 *
 * @author kikock
 * @version 1.0.0
 */
@Provider
@Priority(value = 5)
public class TokenCheckRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCheckRequestFilter.class);
    @Context
    private ResourceInfo resinfo;

    /**
     * Filter method called before a request has been dispatched to a resource.
     * <p>
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * If a request filter produces a response by calling {@link ContainerRequestContext#abortWith}
     * method, the execution of the (either pre-match or post-match) request filter
     * chain is stopped and the response is passed to the corresponding response
     * filter chain (either pre-match or post-match). For example, a pre-match
     * caching filter may produce a response in this way, which would effectively
     * skip any post-match request filters as well as post-match response filters.
     * Note however that a responses produced in this manner would still be processed
     * by the pre-match response filter chain.
     * </p>
     *
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

        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        String traceId = null;
        List<String> traceIds = headers.get("traceId");
        if (traceIds != null && !traceIds.isEmpty()) {
            traceId = traceIds.get(0);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = IdGenerator.uuid2();
        }
        MDC.put("traceId", traceId);
        MDC.put("requestUrl", requestContext.getUriInfo().getAbsolutePath().toString());

        if (StringUtils.startsWith(path, "monitor")) {
            return;
        }

        boolean ignoreCheckSession = false;
        //检查API服务接口是否忽略session检查
        if (resinfo.getResourceClass().isAnnotationPresent(IgnoreCheckSession.class)) {
            LOGGER.debug(resinfo.getResourceClass().getName() + " 忽略session检查 - " + path);
            //ContextUtil.cleanUserToken();
            //return;
            ignoreCheckSession = true;
        }
        //检查API方法是否忽略session检查
        else if (resinfo.getResourceMethod().isAnnotationPresent(IgnoreCheckSession.class)) {
            LOGGER.debug(resinfo.getResourceMethod().getName() + " 忽略session检查 - " + path);
            //ContextUtil.cleanUserToken();
            //return;
            ignoreCheckSession = true;
        }

        Response response;
        Response.ResponseBuilder responseBuilder;

        //check token
        String accessToken;
        List<String> authorizationLines = headers.get(ContextUtil.AUTHORIZATION_KEY);
        if (authorizationLines != null) {
            accessToken = authorizationLines.get(0);
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
