package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import com.kcmp.ck.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 流程服务地址服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowServiceUrl")
@Api(value = "IFlowServiceUrlService 流程服务地址服务API接口")
public interface IFlowServiceUrlService extends IBaseService<FlowServiceUrl, String> {

    /**
     * 保存一个实体
     * @param flowServiceUrl 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<FlowServiceUrl> save(FlowServiceUrl flowServiceUrl);

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return 实体清单
     */
    @Override
    @POST
    @Path("findByPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<FlowServiceUrl> findByPage(Search searchConfig);

    /**
     * 通过流程类型id查找拥有的服务方法
     * @param flowTypeId 流程类型id
     * @return 服务方法list
     */
    @GET
    @Path("findByFlowTypeId/{flowTypeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过流程类型id查找拥有的服务方法",notes = "测试")
    List<FlowServiceUrl> findByFlowTypeId(@PathParam("flowTypeId") String flowTypeId);

    /**
     * 通过业务实体id查找拥有的服务方法
     * @param businessModelId 业务实体id
     * @return 服务方法list
     */
    @GET
    @Path("findByBusinessModelId/{businessModelId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务实体id查找拥有的服务方法",notes = "测试")
    List<FlowServiceUrl> findByBusinessModelId(@PathParam("businessModelId") String businessModelId);
}
