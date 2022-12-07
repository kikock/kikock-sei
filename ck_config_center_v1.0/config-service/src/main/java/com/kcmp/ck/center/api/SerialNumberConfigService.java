package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.SerialNumberConfig;
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
 * 编号生成器配置API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("serialNumberConfig")
@Api(value = "SerialNumberConfigService 编号生成器配置")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SerialNumberConfigService {

    /**
     * 获取运行环境的所有配置
     *
     * @param envCode 运行环境代码
     * @return 编号生成器配置清单
     */
    @GET
    @Path("findByEnvCode")
    @ApiOperation(value = "获取运行环境的所有配置", notes = "获取运行环境的所有配置")
    List<SerialNumberConfig> findByEnvCode(@QueryParam("envCode") String envCode);

    /**
     * 获取运行环境的所有配置+实体代码和名称筛选
     *
     * @param envCode 运行环境代码
     * @return 编号生成器配置清单
     */
    @GET
    @Path("findByEnvCodeAndName")
    @ApiOperation(value = "按名称筛选运行环境的所有配置", notes = "按名称筛选运行环境的所有配置")
    List<SerialNumberConfig> findAllByEnvCodeAndName(@QueryParam("envCode") String envCode,
                                                  @QueryParam("name") String name);
    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 编号生成器配置
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    SerialNumberConfig findOne(@QueryParam("id") String id);

    /**
     * 保存一个编号生成器配置
     *
     * @param serialNumberConfig 编号生成器配置
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个编号生成器配置", notes = "保存一个编号生成器配置")
    OperateResult save(SerialNumberConfig serialNumberConfig);

    /**
     * 删除一个编号生成器配置
     *
     * @param id 编号生成器配置Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除一个编号生成器配置", notes = "删除一个编号生成器配置")
    OperateResult delete(String id);

    /**
     * 清除编号生成器配置缓存
     *
     * @param id 编号生成器配置Id
     * @return 操作结果
     */
    @GET
    @Path("clearConfigCache")
    @ApiOperation(value = "清除编号生成器配置缓存", notes = "清除编号生成器配置缓存")
    OperateResult clearConfigCache(@QueryParam("id") String id);

    /**
     * 获取一个序列编号(无隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    @GET
    @Path("getNumber")
    @ApiOperation(value = "获取一个序列编号(无隔离码)", notes = "获取一个序列编号(无隔离码)")
    String getNumber(@QueryParam("envCode") String envCode, @QueryParam("entityClassName") String entityClassName);

    /**
     * 获取一个序列编号(有隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    @GET
    @Path("getNumberWithIsolation")
    @ApiOperation(value = "获取一个序列编号(有隔离码)", notes = "获取一个序列编号(有隔离码)")
    String getNumberWithIsolation(@QueryParam("envCode") String envCode, @QueryParam("entityClassName") String entityClassName, @QueryParam("isolationCode") String isolationCode);

    /**
     * 获取一个序列编号并初始化(无隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    @GET
    @Path("getNumberAndInit")
    @ApiOperation(value = "获取一个序列编号并初始化(无隔离码)", notes = "获取一个序列编号并初始化(无隔离码)")
    String getNumberAndInit(@QueryParam("envCode") String envCode, @QueryParam("entityClassName") String entityClassName);

    /**
     * 获取一个序列编号并初始化(有隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    @GET
    @Path("getNumberWithIsolationAndInit")
    @ApiOperation(value = "获取一个序列编号并初始化(有隔离码)", notes = "获取一个序列编号并初始化(有隔离码)")
    String getNumberWithIsolationAndInit(@QueryParam("envCode") String envCode, @QueryParam("entityClassName") String entityClassName, @QueryParam("isolationCode") String isolationCode);
    /**
     * 初始化序列编号(有隔离码)
     *
     * @param id         编号生成器配置Id
     * @return 初始化编号
     */
    @GET
    @Path("id")
    @ApiOperation(value = "初始化序列编号", notes = "初始化序列编号")
    String initNumber(@QueryParam("id") String id);

}
