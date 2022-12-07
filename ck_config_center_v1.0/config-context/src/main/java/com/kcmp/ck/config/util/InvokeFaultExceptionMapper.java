package com.kcmp.ck.config.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Locale;


/**
 * @author kikock
 * @version 1.0.0
 */
@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception ex) {
        StackTraceElement[] trace = new StackTraceElement[1];
        trace[0] = ex.getStackTrace()[0];
        ex.setStackTrace(trace);
        ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        rb.type("application/json;charset=UTF-8");
        rb.entity(ex);
        rb.language(Locale.SIMPLIFIED_CHINESE);
        Response r = rb.build();
        return r;
    }
}
