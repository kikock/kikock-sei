package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.Platform;
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
 * 平台API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("platform")
@Api(value = "PlatformService 平台")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PlatformService {

    /**
     * 获取所有数据
     *
     * @return 平台清单
     */
    @GET
    @Path("findAll")
    @ApiOperation(value = "获取所有数据", notes = "获取所有数据")
    List<Platform> findAll();


    /**
     * 平台名称搜索数据
     *
     * @return 平台清单
     */
    @GET
    @Path("findAllByName")
    @ApiOperation(value = "平台名称搜索数据", notes = "平台名称搜索数据")
    List<Platform> findAllByName(String name);

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 平台
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    Platform findOne(@QueryParam("id") String id);

    /**
     * 保存一个平台
     *
     * @param platform 平台
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个平台", notes = "保存一个平台")
    OperateResult save(Platform platform);

    /**
     * 删除一个平台
     *
     * @param ids 平台Ids
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "批量删除平台", notes = "批量删除平台")
    OperateResult delete(String ids);
}
