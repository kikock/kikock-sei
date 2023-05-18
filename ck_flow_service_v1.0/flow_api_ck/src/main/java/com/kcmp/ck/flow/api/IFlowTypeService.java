package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowType;
import com.kcmp.ck.flow.vo.SearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 流程类型服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowType")
@Api(value = "IFlowTypeService 流程类型服务API接口")
public interface IFlowTypeService extends IBaseService<FlowType, String> {

//    /**
//     * 保存一个实体
//     * @param flowType 实体
//     * @return 保存后的实体
//     */
//    @POST
//    @Path("save")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
//    OperateResultWithData<FlowType> save(FlowType flowType);

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
    PageResult<FlowType> findByPage(Search searchConfig);

    /**
     * 获取流程类型分页数据
     * @return 实体清单
     */
    @POST
    @Path("listFlowType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<FlowType> listFlowType(SearchVo searchVo);

    /**
     * 根据业务实体id查询流程类型
     * @param businessModelId 业务实体id
     * @return 实体清单
     */
    @POST
    @Path("findByBusinessModelId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    List<FlowType> findByBusinessModelId(@QueryParam("businessModelId") String businessModelId);
}
