package com.kcmp.ck.flow.api;

import com.kcmp.ck.flow.vo.CompleteTaskVo;
import com.kcmp.ck.flow.vo.SolidifyStartFlowVo;
import com.kcmp.ck.flow.vo.StartFlowBusinessAndTypeVo;
import com.kcmp.ck.flow.vo.StartFlowVo;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 流程的默认服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("defaultFlowBase")
@Api(value = "IDefaultFlowBaseService 流程的默认服务API接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IDefaultFlowBaseService {

    /**
     * 通过流程定义key启动流程
     * @param businessModelCode 业务实体code
     * @param businessKey       业务实体key
     * @param opinion           审批意见
     * @param typeId            流程类型id
     * @param flowDefKey        流程定义key
     * @param taskList          任务完成传输对象
     * @param anonymousNodeId   传输对象为anonymous时传入的节点id
     * @return
     */
    @POST
    @Path("startFlow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过流程定义key启动流程", notes = "通过流程定义key启动流程")
    ResponseData startFlow(@QueryParam("businessModelCode") String businessModelCode,
                           @QueryParam("businessKey") String businessKey,
                           @QueryParam("opinion") String opinion,
                           @QueryParam("typeId") String typeId,
                           @QueryParam("flowDefKey") String flowDefKey,
                           @QueryParam("taskList") String taskList,
                           @QueryParam("anonymousNodeId") String anonymousNodeId) throws NoSuchMethodException, SecurityException;

    /**
     * 启动流程
     * 提供三种调用方式：
     * 第一种参数传businessModelCode（业务实体类路径）和businessKey（业务实体id），会返回给你flowTypeList（流程类型）一个或多个 ，nodeInfoList（节点信息，包含执行人信息）里面默认只是显示第一个类型的节点信息
     * 第二种参数传businessModelCode（业务实体类路径）和businessKey（业务实体id）和typeId（指定的流程类型），会返回给你flowTypeList（流程类型）一个，nodeInfoList（指定流程类型的节点信息，包含执行人信息）
     * 第三种是封装参数真正的流程启动 java.net.SocketException: SocketException invoking http://10.4.69.39:19006/flow-service/defaultFlowBase/startFlowNew: Unexpected end of file from server
     * @param startFlowVo   开始流程vo参数
     * @returnjava.net.SocketException: Unexpected end of file from server
     */
    @POST
    @Path("startFlowNew")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "启动流程", notes = "启动流程")
    ResponseData startFlow(StartFlowVo startFlowVo) throws NoSuchMethodException, SecurityException;

    /**
     * 固化流程的默认自动启动流程
     * 条件：先检查固化每个节点执行人情况，如果是非人工选择的情况（单候选人，或者单签为多候选人），系统默认进行启动；反之跳转到配置执行人界面进行人为配置
     * @param solidifyStartFlowVo   固定开始任务vo参数
     * @return
     */
    @POST
    @Path("solidifyCheckAndSetAndStart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "固化检查设置并启动流程", notes = "固化检查设置执行人并启动（如果没有人工选择情况）")
    ResponseData solidifyCheckAndSetAndStart(SolidifyStartFlowVo solidifyStartFlowVo);

    /**
     * 启动流程(通过业务信息和流程类型)
     * @param startFlowBusinessAndTypeVo 其中包含业务实体类路径，业务实例ID、以及需要启动的流程类型代码
     * @return
     */
    @POST
    @Path("startFlowByBusinessAndType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务信息和流程类型启动流程", notes = "通过业务信息和流程类型启动流程")
    ResponseData startFlowByBusinessAndType(StartFlowBusinessAndTypeVo startFlowBusinessAndTypeVo);

    /**
     * 签收任务
     * @param taskId 任务id
     * @return
     */
    @POST
    @Path("listFlowTask")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "签收任务", notes = "签收任务")
    ResponseData claimTask(@QueryParam("taskId") String taskId);

    /**
     * 通过vo对象完成任务
     * @param completeTaskVo    提交任务vo参数
     * @return 操作结果
     */
    @POST
    @Path("completeTaskNew")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过VO对象完成任务", notes = "通过VO对象完成任务")
    ResponseData completeTask(CompleteTaskVo completeTaskVo) throws Exception;

    /**
     * 完成任务
     * @param taskId            节点ID
     * @param businessId        业务表单ID
     * @param opinion           审批意见
     * @param taskList          任务完成传输对象
     * @param endEventId        结束事件id
     * @param manualSelected    是否手动选择
     * @param approved          意见
     * @param loadOverTime      加载时间
     * @return 操作结果
     */
    @POST
    @Path("completeTask")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "完成任务", notes = "完成任务")
    ResponseData completeTask(@QueryParam("taskId") String taskId,
                              @QueryParam("businessId") String businessId,
                              @QueryParam("opinion") String opinion,
                              @QueryParam("taskList") String taskList,
                              @QueryParam("endEventId") String endEventId,
                              @QueryParam("manualSelected") boolean manualSelected,
                              @QueryParam("approved") String approved,
                              @QueryParam("loadOverTime") Long loadOverTime) throws Exception;

    /**
     * 回退（撤销）任务
     * @param preTaskId 上一个任务ID
     * @param opinion   意见
     * @return 操作结果
     */
    @POST
    @Path("cancelTask")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = " 回退（撤销）任务", notes = " 回退（撤销）任务")
    ResponseData rollBackTo(@QueryParam("preTaskId") String preTaskId, @QueryParam("opinion") String opinion) throws CloneNotSupportedException;

    /**
     * 任务驳回
     * @param taskId  任务ID
     * @param opinion 意见
     * @return 操作结果
     */
    @POST
    @Path("rejectTask")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "任务驳回", notes = "任务驳回")
    ResponseData rejectTask(@QueryParam("taskId") String taskId, @QueryParam("opinion") String opinion) throws Exception;

    /**
     * 获取当前审批任务的决策信息
     * @param taskId    任务ID
     * @return 操作结果
     */
    @POST
    @Path("nextNodesInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取当前审批任务的决策信息", notes = "获取当前审批任务的决策信息")
    ResponseData nextNodesInfo(@QueryParam("taskId") String taskId) throws NoSuchMethodException;

    /**
     * 获取下一步的节点信息任务
     * @param taskId    任务ID
     * @return 操作结果
     */
    @POST
    @Path("getSelectedNodesInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取下一步的节点信息任务", notes = "获取下一步的节点信息任务")
    ResponseData getSelectedNodesInfo(@QueryParam("taskId") String taskId,
                                      @QueryParam("approved") String approved,
                                      @QueryParam("includeNodeIdsStr") String includeNodeIdsStr,
                                      @QueryParam("solidifyFlow") Boolean solidifyFlow) throws NoSuchMethodException;

    /**
     * 获取下一步的节点信息任务(带用户信息)
     * @param taskId    任务ID
     * @return 操作结果
     */
    @POST
    @Path("nextNodesInfoWithUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取下一步的节点信息任务(带用户信息)", notes = "获取下一步的节点信息任务(带用户信息)")
    ResponseData nextNodesInfoWithUser(@QueryParam("taskId") String taskId) throws NoSuchMethodException;

    /**
     * 获取任务抬头信息信息任务
     * @param taskId    任务ID
     * @return 操作结果
     */
    @POST
    @Path("getApprovalHeaderInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取任务抬头信息信息任务", notes = "获取任务抬头信息信息任务")
    ResponseData getApprovalHeaderInfo(@QueryParam("taskId") String taskId);

    /**
     * 通过流程实例id获取任务抬头信息信息任务
     * @param instanceId 流程实例id
     * @return 操作结果
     */
    @POST
    @Path("getApprovalHeaderByInstanceId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "流程实例id获取任务抬头信息", notes = "流程实例id获取任务抬头信息")
    ResponseData getApprovalHeaderByInstanceId(@QueryParam("instanceId") String instanceId);

    /**
     * 通过业务单据Id获取待办任务（中泰要求新增的功能:不包含子流程信息）
     * @param businessId 业务单据id
     * @return 待办任务集合
     */
    @POST
    @Path("findTasksByBusinessId")
    @ApiOperation(value = "获取待办任务", notes = "通过业务单据Id获取待办任务（不包含子流程）")
    ResponseData findTasksByBusinessId(@QueryParam("businessId") String businessId);

    /**
     * 通过业务单据Id集合获取待办执行人（中泰要求新增的功能:不包含子流程信息）
     * @param businessIdLists 业务单据id集合
     * @return 待办任务集合
     */
    @POST
    @Path("getExecutorsByBusinessIdList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务单据Id集合获取待办执行人", notes = "通过业务单据Id集合获取待办执行人（不包含子流程）")
    ResponseData getExecutorsByBusinessIdList(List<String> businessIdLists);
}
