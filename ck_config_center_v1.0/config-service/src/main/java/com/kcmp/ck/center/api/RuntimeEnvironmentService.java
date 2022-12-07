package com.kcmp.ck.center.api;

import com.kcmp.ck.config.entity.RuntimeEnvironment;
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
 * 运行环境API接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("runtimeEnvironment")
@Api(value = "RuntimeEnvironmentService 运行环境")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RuntimeEnvironmentService {

    /**
     * 获取所有数据
     *
     * @return 运行环境清单
     */
    @GET
    @Path("findAll")
    @ApiOperation(value = "获取所有数据", notes = "获取所有数据")
    List<RuntimeEnvironment> findAll();

    /**
     * 获取所有数据
     *
     * @return 运行环境清单
     */
    @GET
    @Path("findByNameAll")
    @ApiOperation(value = "获取所有数据", notes = "获取所有数据")
    List<RuntimeEnvironment> findByNameAll(String name);


    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 运行环境
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "通过Id获取实体", notes = "通过Id获取实体")
    RuntimeEnvironment findOne(@QueryParam("id") String id);

    /**
     * 保存一个运行环境
     *
     * @param runtimeEnvironment 运行环境
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存一个运行环境", notes = "保存一个运行环境")
    OperateResult save(RuntimeEnvironment runtimeEnvironment);

    /**
     * 删除一个运行环境
     *
     * @param id 运行环境Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除一个运行环境", notes = "删除一个运行环境")
    OperateResult delete(String id);
}
