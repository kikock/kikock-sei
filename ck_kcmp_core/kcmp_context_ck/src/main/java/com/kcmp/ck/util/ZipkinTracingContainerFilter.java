package com.kcmp.ck.util;

import brave.Span;
import brave.Tracer;
import brave.http.HttpServerHandler;
import brave.http.HttpTracing;
import brave.jaxrs2.ContainerAdapter;
import brave.propagation.TraceContext;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static javax.ws.rs.RuntimeType.SERVER;

/**
 * Created by kikock
 * jax-rs服务端过滤器
 * @email kikock@qq.com
 **/
@Provider
@ConstrainedTo(SERVER)
public class ZipkinTracingContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {
    final Tracer tracer;
    final HttpServerHandler<ContainerRequestContext, ContainerResponseContext> handler;
    final TraceContext.Extractor<ContainerRequestContext> extractor;

    /**
     * 构造函数
     *
     * @param httpTracing http跟踪
     */
    public ZipkinTracingContainerFilter(HttpTracing httpTracing) {
        if (httpTracing == null) {
            throw new NullPointerException("HttpTracing == null");
        }
        tracer = httpTracing.tracing().tracer();
        handler = HttpServerHandler.create(httpTracing, new ContainerAdapter());
        extractor = httpTracing.tracing().propagation()
                .extractor((carrier, key) -> carrier.getHeaderString(key));
    }

    @Context
    ResourceInfo resourceInfo;

    static boolean shouldPutSpanInScope(ResourceInfo resourceInfo) {
        if (resourceInfo == null) {
            return false;
        }
        for (Annotation[] annotations : resourceInfo.getResourceMethod().getParameterAnnotations()) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Suspended.class)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在将请求发送到客户端传输层之前调用的筛选器方法
     * @param requestContext request context.
     * @throws IOException if an I/O exception occurs.
     * @see PreMatching
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String url = requestContext.getUriInfo().getPath();
        //判断是否是swagger.json
        if (StringUtils.startsWith(url, "api-docs") || StringUtils.startsWith(url, "swagger")) {
            return;
        }
        if (resourceInfo != null) {
            requestContext.setProperty(ResourceInfo.class.getName(), resourceInfo);
        }
        Span span = handler.handleReceive(extractor, requestContext);
        requestContext.removeProperty(ResourceInfo.class.getName());
        if (shouldPutSpanInScope(resourceInfo)) {
            requestContext.setProperty(Tracer.SpanInScope.class.getName(), tracer.withSpanInScope(span));
        } else {
            requestContext.setProperty(Span.class.getName(), span);
        }
    }

    /**
     * 在为请求提供响应后或当HTTP调用返回时调用的筛选器方法
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String url = requestContext.getUriInfo().getPath();
        //判断是否是swagger.json
        if (StringUtils.startsWith(url, "api-docs") || StringUtils.startsWith(url, "swagger")) {
            return;
        }
        Span span = (Span) requestContext.getProperty(Span.class.getName());
        Tracer.SpanInScope spanInScope = (Tracer.SpanInScope) requestContext.getProperty(Tracer.SpanInScope.class.getName());
        if (span == null) {
            // synchronous response
            if (spanInScope != null) {
                span = tracer.currentSpan();
                spanInScope.close();
            } else if (responseContext.getStatus() == 404) {
                span = handler.handleReceive(extractor, requestContext);
            } else {
                return;
            }
        }
        handler.handleSend(responseContext, null, span);
    }
}
