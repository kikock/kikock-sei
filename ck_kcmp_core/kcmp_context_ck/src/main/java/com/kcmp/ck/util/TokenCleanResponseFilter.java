package com.kcmp.ck.util;

import com.kcmp.ck.context.ContextUtil;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by kikock
 * 返回token清理
 * @email kikock@qq.com
 **/
@Provider
public class TokenCleanResponseFilter implements ContainerResponseFilter {

    /**
     * 为请求提供响应后调用的筛选器方法
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        ContextUtil.cleanUserToken();
        MDC.clear();
    }
}
