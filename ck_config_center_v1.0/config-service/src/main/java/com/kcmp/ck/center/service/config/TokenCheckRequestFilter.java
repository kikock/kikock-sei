package com.kcmp.ck.center.service.config;

import com.kcmp.ck.config.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

/**
 * Created by kikock
 * 请求过滤
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Provider
@Priority(value = 5)
@Configuration
public class TokenCheckRequestFilter implements ContainerRequestFilter {
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
        if (StringUtils.startsWith(path, "api-docs") || StringUtils.startsWith(path, "swagger")) {
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
            traceId = IdGenerator.uuid();
        }
        MDC.put("traceId", traceId);
        MDC.put("requestUrl", requestContext.getUriInfo().getAbsolutePath().toString());
    }
}
