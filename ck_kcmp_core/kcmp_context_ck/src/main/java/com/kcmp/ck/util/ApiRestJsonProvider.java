package com.kcmp.ck.util;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by kikock
 * 平台API服务使用的JSON序列化提供类
 * @email kikock@qq.com
 **/
@Provider
@Consumes({MediaType.APPLICATION_JSON, "application/json", "text/json"})
@Produces({MediaType.APPLICATION_JSON, "application/json", "text/json"})
public class ApiRestJsonProvider extends JacksonJsonProvider {

    //重载构造函数
    public ApiRestJsonProvider() {
        super(ApiJsonUtils.mapper());
    }
}
