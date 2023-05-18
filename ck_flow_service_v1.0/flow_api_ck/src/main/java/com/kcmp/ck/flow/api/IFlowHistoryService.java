package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.vo.phone.FlowHistoryPhoneVo;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 流程历史服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowHistory")
@Api(value = "IFlowHistoryService 流程历史服务API接口")
public interface IFlowHistoryService extends IBaseService<FlowHistory, String> {

    /**
     * 保存一个实体
     * @param flowHistory 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<FlowHistory> save(FlowHistory flowHistory);

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
    PageResult<FlowHistory> findByPage(Search searchConfig);

    /**
     * 根据流程实例ID查询流程历史
     * @param instanceId 实例id
     * @return 执行历史
     */
    @POST
    @Path("findByInstanceId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询流程历史", notes = "查询流程历史")
    List<FlowHistory> findByInstanceId(String instanceId);

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return
     */
    @POST
    @Path("findByPageAndUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<FlowHistory> findByPageAndUser(Search searchConfig);


    /**
     * 查询流程已办汇总列表
     * @return
     */
    @POST
    @Path("listFlowHistoryHeader")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询流程已办汇总列表", notes = "查询流程已办汇总列表")
    ResponseData listFlowHistoryHeader(@QueryParam("dataType") String dataType);

    /**
     * 获取已办信息
     * @param businessModelId   业务实体id
     * @param searchConfig      查询条件
     * @return 已办汇总信息
     */
    @POST
    @Path("listFlowHistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "已办汇总信息",notes = "已办汇总信息")
    ResponseData listFlowHistory(@QueryParam("businessModelId") String businessModelId, Search searchConfig);

    /**
     * 获取待办汇总信息
     * @param businessModelId 业务实体id
     * @param searchConfig 查询条件
     * @return 待办汇总信息
     */
    @POST
    @Path("findByBusinessModelId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取待办汇总信息",notes = "测试")
    PageResult<FlowHistory> findByBusinessModelId(@QueryParam("businessModelId") String businessModelId, Search searchConfig);

    /**
     * 获取已办汇总信息（最新移动端专用）
     * @param businessModelId 业务实体id
     * @param property 需要排序的字段
     * @param direction 排序规则
     * @param page 当前页数
     * @param rows 每页条数
     * @param quickValue 模糊查询字段内容
     * @return 待办汇总信息
     */
    @POST
    @Path("findByBusinessModelIdOfMobile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取已办汇总信息（最新移动端专用）",notes = "获取已办汇总信息（最新移动端专用）")
    PageResult<FlowHistoryPhoneVo> findByBusinessModelIdOfMobile(
            @QueryParam("businessModelId") String businessModelId,
            @QueryParam("property") String property,
            @QueryParam("direction") String direction,
            @QueryParam("page") int page,
            @QueryParam("rows") int rows,
            @QueryParam("quickValue") String quickValue);

    /**
     * 获取已办汇总信息（移动端专用）
     * @param businessModelId 业务实体id
     * @param property 需要排序的字段
     * @param direction 排序规则
     * @param page 当前页数
     * @param rows 每页条数
     * @param quickValue 模糊查询字段内容
     * @return 待办汇总信息
     */
    @POST
    @Path("findByBusinessModelIdOfPhone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取已办汇总信息（移动端专用）",notes = "获取已办汇总信息（移动端专用）")
    PageResult<FlowHistory> findByBusinessModelIdOfPhone(
            @QueryParam("businessModelId") String businessModelId,
            @QueryParam("property") String property,
            @QueryParam("direction") String direction,
            @QueryParam("page") int page,
            @QueryParam("rows") int rows,
            @QueryParam("quickValue") String quickValue);
}
