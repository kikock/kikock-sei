package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.dto.OperateResult;
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
import java.util.Map;


/**
 * Created by kikock
 * 应用模块API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("applicationModule")
@Api(value = "ApplicationModuleService 应用模块")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ApplicationModuleService {

    /**
     * 获取所有应用模块数据
     *
     * @return 应用模块清单
     */
    @GET
    @Path("findAll")
    @ApiOperation(value = "获取所有应用模块数据", notes = "获取所有应用模块数据")
    List<ApplicationModule> findAll();

    /**
     * 获取所有应用模块数据
     *
     * @return 应用模块清单
     */
    @GET
    @Path("getPlatformModule")
    @ApiOperation(value = "获取所有应用模块数据", notes = "获取所有应用模块数据")
    List<ApplicationModule> getPlatformModule(@QueryParam("platformId") String platformId);

    /**
     * 根据平台代码获取应用模块
     * 允许platformCode为空，当为空时不按该条件查询
     *
     * @param platformCode 平台代码
     * @return
     */
    @GET
    @Path("getAppModules")
    @ApiOperation(value = "根据平台代码获取应用模块", notes = "根据平台代码获取应用模块")
    Map<String, String> getAppModules(@QueryParam("platformCode") String platformCode);

    /**
     * 通过Id获取应用模块
     *
     * @param id Id标识
     * @return 应用模块
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取应用模块", notes = "通过Id获取应用模块")
    ApplicationModule findOne(@QueryParam("id") String id);

    /**
     * 保存一个应用模块
     *
     * @param applicationModule 应用模块
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个应用模块", notes = "保存一个应用模块")
    OperateResult save(ApplicationModule applicationModule);

    /**
     * 批量删除应用模块
     *
     * @param ids 应用模块Ids (逗号分隔字符串)
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "批量删除应用模块", notes = "批量删除应用模块")
    OperateResult delete(String ids);
}
