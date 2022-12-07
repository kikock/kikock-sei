package com.kcmp.ck.config.util;

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
 * <strong>实现功能:</strong>
 * <p>jax-rs服务端过滤器</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.0
 */
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

    /**
     * This implementation peeks to see if the request is async or not, which means {@link
     * PreMatching} cannot be used: pre-matching doesn't inject the resource info!
     */
    @Context
    ResourceInfo resourceInfo;

    /**
     * We shouldn't put a span in scope unless we know for sure the request is not async. That's
     * because we cannot detach if from the calling thread when async is used.
     */
    // TODO: add benchmark and cache if slow
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
     * Filter method called before a request has been dispatched to a resource.
     *
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
     * Filter method called after a response has been provided for a request
     * (either by a {@link ContainerRequestFilter request filter} or by a
     * matched resource method.
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * </p>
     *
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
        if (span != null) { // asynchronous response or we couldn't figure it out
        } else if (spanInScope != null) { // synchronous response
            span = tracer.currentSpan();
            spanInScope.close();
        } else if (responseContext.getStatus() == 404) {
            span = handler.handleReceive(extractor, requestContext);
        } else {
            return; // unknown state
        }
        handler.handleSend(responseContext, null, span);
    }
}
