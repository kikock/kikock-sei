package com.kcmp.ck.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.kcmp.ck.vo.ResponseData;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by kikock
 * 调用故障异常映射器
 * @email kikock@qq.com
 **/
@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        int code;
        String msg;
        if (ex instanceof NotFoundException) {
            //没有找到资源路径
            code = 404;
            msg = "404";
        } else if (ex instanceof JsonParseException) {
            code = 400;
            msg = "json转换异常";
        } else if (ex instanceof UnrecognizedPropertyException) {
            code = 400;
            msg = "";
        } else {
            code = 500;
            msg = ex.getMessage();
        }
        ResponseData date = ResponseData.build().setStatusCode(code).setMessage(msg);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(date).build();
    }
}
