package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.vo.ParamConfigKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by kikock
 * 全局参数API服务接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("globalParam")
@Api(value = "GlobalParamService 全局参数")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GlobalParamService {

    /**
     * 获取应用服务的全局参数
     *
     * @param appId 应用服务Id
     * @return 全局参数
     */
    @GET
    @Path("getParams")
    @ApiOperation(value = "获取应用服务的全局参数", notes = "获取应用服务的全局参数")
    Map<String, Map<String, String>> getParams(@QueryParam("appId") String appId);

    /**
     * 获取全局参数配置值(平台级)
     *
     * @param configKey 全局参数配置键
     * @return 配置值
     */
    @POST
    @Path("getParamConfigValue")
    @ApiOperation(value = "获取全局参数配置值(平台级)", notes = "获取全局参数配置值(平台级)")
    Map<String, String> getParamConfigValue(ParamConfigKey configKey);
}
