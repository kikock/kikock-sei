package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.dto.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 监控API服务接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("monitor")
@Api(value = "MonitorService 监控")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MonitorService {

    /**
     * 监控业务健康
     *
     * @return
     */
    @GET
    @Path("health")
    @ApiOperation(value = "监控业务健康", notes = "监控业务健康")
    ResponseData health();
}
