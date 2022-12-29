package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import com.kcmp.ck.config.entity.EnvironmentVariable;
import com.kcmp.ck.config.entity.dto.EnvVarConfig;
import com.kcmp.ck.config.entity.dto.EnvVarConfigSearch;
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

/**
 * Created by kikock
 * 环境变量API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("environmentVariable")
@Api(value = "EnvironmentVariableService 环境变量")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface EnvironmentVariableService {

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 环境变量
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    EnvironmentVariable findOne(@QueryParam("id") String id);

    /**
     * 保存环境变量配置
     *
     * @param envVarConfig 环境变量配置
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存环境变量配置", notes = "保存环境变量配置")
    OperateResult save(EnvVarConfig envVarConfig);
    /**
     * 保存环境变量配置
     *
     * @param envVarConfig 环境变量配置
     * @return 操作结果
     */
    @POST
    @Path("editSave")
    @ApiOperation(value = "保存环境变量配置", notes = "保存环境变量配置")
    OperateResult editSave(EnvVarConfig envVarConfig);
    /**
     * 删除环境变量配置
     *
     * @param environmentVariableId 环境变量Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除环境变量配置", notes = "删除环境变量配置")
    OperateResult delete(String environmentVariableId);

    /**
     * 通过平台和环境获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @return 环境变量配置清单
     */
    @GET
    @Path("findByPlatformAndEnv")
    @ApiOperation(value = "通过平台和环境获取环境变量配置", notes = "通过平台和环境获取环境变量配置")
    List<EnvVarConfig> findByPlatformAndEnv(@QueryParam("platformId") String platformId, @QueryParam("runtimeEnvironmentId") String runtimeEnvironmentId);

    /**
     * 通过查询参数获取环境变量配置清单
     *
     * @param search 查询参数
     * @return 环境变量配置清单
     */
    @POST
    @Path("findBySearch")
    @ApiOperation(value = "通过查询参数获取环境变量配置清单", notes = "通过查询参数获取环境变量配置清单")
    List<EnvVarConfig> findBySearch(EnvVarConfigSearch search);

    /**
     * 通过id获取环境变量配置
     *
     * @param environmentVariableId 环境变量id
     * @param runtimeEnvironmentId 运行环境id     *
     * @return 环境变量配置清单
     */
    @POST
    @Path("findByEnvId")
    @ApiOperation(value = "根据环境变量id获取运行环境", notes = "根据环境变量id获取运行环境")
    EnvironmentVarConfig findByEnvId(String runtimeEnvironmentId,String environmentVariableId);

}
