package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.dto.GlobalParamConfigSearch;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.dto.ParamConfigVo;
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
 * 全局参数配置API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("globalParamConfig")
@Api(value = "GlobalParamConfigService 全局参数配置")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GlobalParamConfigService {

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 全局参数配置
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    GlobalParamConfig findOne(@QueryParam("id") String id);

    /**
     * 通过查询参数获取全局参数配置清单
     *
     * @param search 查询参数
     * @return 全局参数配置清单
     */
    @POST
    @Path("findBySearch")
    @ApiOperation(value = "通过查询参数获取全局参数配置清单", notes = "通过查询参数获取全局参数配置清单")
    List<GlobalParamConfig> findBySearch(GlobalParamConfigSearch search);

    /**
     * 保存一个全局参数配置
     *
     * @param globalParamConfig 全局参数配置
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个全局参数配置", notes = "保存一个全局参数配置")
    OperateResult save(GlobalParamConfig globalParamConfig);

    /**
     * 删除一个全局参数配置
     *
     * @param id 全局参数配置Id
     * @return 操作结果
     */
    @DELETE
    @Path("remove")
    @ApiOperation(value = "删除一个全局参数配置", notes = "删除一个全局参数配置")
    OperateResult remove(String id);

    /**
     * 获取平台中可以引用的全局参数键和说明
     *
     * @param platformId 平台Id
     * @return 参数键和说明
     */
    @GET
    @Path("getCanReferenceParams")
    @ApiOperation(value = "获取平台中可以引用的全局参数键和说明", notes = "获取平台中可以引用的全局参数键和说明")
    List<ParamConfigVo> getCanReferenceParams(@QueryParam("platformId") String platformId);
}
