package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 应用服务API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("applicationService")
@Api(value = "ApplicationServiceService 应用服务")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ApplicationServiceService {

    /**
     * 获取所有应用服务数据
     *
     * @return 应用服务清单
     */
    @GET
    @Path("findAll")
    @ApiOperation(value = "获取所有应用服务数据", notes = "获取所有应用服务数据")
    List<ApplicationService> findAll();

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 应用服务
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    ApplicationService findOne(@QueryParam("id") String id);

    /**
     * 保存一个应用服务
     *
     * @param applicationService 应用服务
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个应用服务", notes = "保存一个应用服务")
    OperateResult save(ApplicationService applicationService);

    /**
     * 删除一个应用服务
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除一个应用服务", notes = "删除一个应用服务")
    OperateResult delete(String ids);

    /**
     * 发布全局参数配置
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @POST
    @Path("distribute")
    @ApiOperation(value = "发布全局参数配置", notes = "发布全局参数配置")
    OperateResult distribute(List<String> ids);


    /**
     * 查看全局参数
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @POST
    @Path("findZookeeperData")
    @ApiOperation(value = "发布全局参数配置", notes = "发布全局参数配置")
    ResponseData findZookeeperData(List<String> ids);
    /**
     * 通过平台和环境获取应用服务
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @return 应用服务清单
     */
    @GET
    @Path("findByPlatformAndEnv")
    @ApiOperation(value = "通过平台和环境获取应用服务", notes = "通过平台和环境获取应用服务")
    List<ApplicationService> findByPlatformAndEnv(@QueryParam("platformId") String platformId, @QueryParam(
            "runtimeEnvironmentId") String runtimeEnvironmentId,@QueryParam("name") String name);

    /**
     * 通过AppId标识获取实体
     *
     * @param appId AppId标识
     * @return 应用服务
     */
    @GET
    @Path("findByAppId")
    @ApiOperation(value = "通过AppId标识获取实体", notes = "通过AppId标识获取实体")
    ApplicationService findByAppId(@QueryParam("appId") String appId);
}
