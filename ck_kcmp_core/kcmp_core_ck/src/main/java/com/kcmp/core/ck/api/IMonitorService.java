package com.kcmp.core.ck.api;

import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 监控服务接口
 * @email kikock@qq.com
 **/
@Api(value = "监控服务接口")
@Path("monitor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IMonitorService {
    /**
     * 监控业务健康
     */
    @GET
    @Path("health")
    @ApiOperation(value = "监控服务是否健康", notes = "监控服务是否健康")
    ResponseData health();
}
