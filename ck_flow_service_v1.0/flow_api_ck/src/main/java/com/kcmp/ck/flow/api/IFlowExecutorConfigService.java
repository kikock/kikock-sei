package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowExecutorConfig;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 自定义执行人配置的服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowExecutorConfig")
@Api(value = "IFlowExecutorConfigService 自定义执行人配置的API接口")
public interface IFlowExecutorConfigService extends IBaseService<FlowExecutorConfig, String> {

    /**
     * 保存一个实体
     * @param flowExecutorConfig 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<FlowExecutorConfig> save(FlowExecutorConfig flowExecutorConfig);

    /**
     * 保存一个实体(并验证code的唯一性)
     * @param flowExecutorConfig 实体
     * @return 保存后的实体
     */
    @POST
    @Path("saveValidateCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<FlowExecutorConfig> saveValidateCode(FlowExecutorConfig flowExecutorConfig);

    /**
     * 获取分页数据
     * @return 实体清单
     */
    @Override
    @POST
    @Path("findByPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<FlowExecutorConfig> findByPage(Search searchConfig);

    /**
     * 获取所有符合条件的数据
     * @param searchConfig 搜索对象
     * @return 实体清单
     */
    @POST
    @Path("findByFilters")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    List<FlowExecutorConfig> findByFilters(Search searchConfig);

    /**
     * 通过业务实体ID获取自定义执行人
     * @param businessModelId
     * @return
     */
    @POST
    @Path("listCombo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务实体ID获取自定义执行人", notes = "通过业务实体ID获取自定义执行人")
    ResponseData listCombo(@QueryParam("businessModelId") String businessModelId);
}
