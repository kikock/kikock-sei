package com.kcmp.ck.util;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * Created by kikock
 * API token检查的客户端过滤器
 * @email kikock@qq.com
 **/
@Priority(value = 10)
public class TokenClientRequestFilter implements ClientRequestFilter/*, ClientResponseFilter*/ {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenClientRequestFilter.class);

    /**
     * 在将请求发送到客户端传输层之前调用的筛选器方法
     * @param requestContext request context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();

        SessionUser sessionUser = ContextUtil.getSessionUser();
        //从运行环境上下文中获取当前session id
        String token = sessionUser.getAccessToken();
        LOGGER.debug("request AccessToken {}", token);
        if (StringUtils.isNotBlank(token)) {
            headers.putSingle(ContextUtil.AUTHORIZATION_KEY, token);
        }

        String traceId = MDC.get("traceId");
        if (StringUtils.isBlank(traceId)) {
            traceId = IdGenerator.uuid2();
        }
        LOGGER.debug("request traceId {}", traceId);
        headers.putSingle("traceId", traceId);

        LOGGER.debug("request uri {}", requestContext.getUri());

        headers.putSingle("appId", ContextUtil.getAppId());
    }

    /**
     * 在为请求提供响应后或当HTTP调用返回时调用的筛选器方法
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
//    @Override
//    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
//
//    }
}
