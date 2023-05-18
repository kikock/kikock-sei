package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.api.IBaseEntityService;
import com.kcmp.core.ck.api.IFindByPageService;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.entity.DefaultBusinessModel;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 默认业务表单服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("defaultBusinessModel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "IDefaultBusinessModelService 默认业务表单服务API接口")
public interface IDefaultBusinessModelService extends IBaseEntityService<DefaultBusinessModel>, IFindByPageService<DefaultBusinessModel> {

    /**
     * 测试事前
     * @param id        单据id
     * @param paramJson json参数
     * @return 执行结果
     */
    @POST
    @Path("changeCreateDepict")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试事件", notes = "测试事件")
    String changeCreateDepict(@QueryParam("id") String id, @QueryParam("paramJson") String paramJson);

    /**
     * 测试事后
     * @param id        单据id
     * @param paramJson json参数
     * @return 执行结果
     */
    @POST
    @Path("changeCompletedDepict")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试事件", notes = "测试事件")
    String changeCompletedDepict(@QueryParam("id") String id, @QueryParam("paramJson") String paramJson);

    /**
     * 测试自定义执行人选择
     * @param flowInvokeParams 流程参数
     * @return 执行结果
     */
    @POST
    @Path("getPersonToExecutorConfig")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "自定义获取excutor",notes = "测试 自定义获取excutor")
    List<Executor> getPersonToExecutorConfig(FlowInvokeParams flowInvokeParams);

    /**
     * 接收任务测试接口
     * @param id           业务单据id
     * @param changeText   参数文本
     * @return 结果
     */
    @POST
    @Path("testReceiveCall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试ReceiveCall",notes = "测试ReceiveCall")
    boolean testReceiveCall(@QueryParam("id") String id, @QueryParam("paramJson") String changeText);

    /**
     * 任务启动检查
     * @param id 业务单据id
     * @return
     */
    @GET
    @Path("checkStartFlow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试checkStartFlow",notes = "测试checkStartFlow")
    boolean checkStartFlow(@QueryParam("id") String id);

    /**
     * 任务结束
     * @param id 业务单据id
     */
    @POST
    @Path("endCall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试endCall",notes = "endCall")
    void endCall(@QueryParam("id") String id);

    /**
     * 新任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("newServiceCall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试newServiceCall",notes = "newServiceCall")
    FlowOperateResult newServiceCall(FlowInvokeParams flowInvokeParams);

    /**
     * 异常任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("newServiceCallFailure")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试异常服务",notes = "newServiceCallFailure")
    FlowOperateResult newServiceCallFailure(FlowInvokeParams flowInvokeParams);

    /**
     * 测试事前（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("changeCreateDepictNew")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试changeCreateDepictNew",notes = "changeCreateDepictNew")
    FlowOperateResult changeCreateDepictNew(FlowInvokeParams flowInvokeParams);

    /**
     * 测试事后（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("changeCompletedDepictNew")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试changeCompletedDepictNew",notes = "changeCompletedDepictNew")
    FlowOperateResult changeCompletedDepictNew(FlowInvokeParams flowInvokeParams);

    /**
     * 接收任务测试接口（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("testReceiveCallNew")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试testReceiveCallNew",notes = "testReceiveCallNew")
    FlowOperateResult testReceiveCallNew(FlowInvokeParams flowInvokeParams);

    /**
     * 业务属性和值
     * @param businessModelCode 业务属性编码
     * @param id                业务表单id
     * @return
     * @throws Exception
     */
    @GET
    @Path("testPJoin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "testPJoin",notes = "testPJoin")
    Map<String,Object> businessPropertiesAndValues(@QueryParam("businessModelCode") String businessModelCode, @QueryParam("id") String id) throws Exception;

    /**
     * 测试完整的工作池任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("testPoolTaskComplete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试testPoolTaskComplete",notes = "testPoolTaskComplete")
    FlowOperateResult testPoolTaskComplete(FlowInvokeParams flowInvokeParams);

    /**
     * 测试工作池任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("testPoolTaskSignal")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "测试testPoolTaskSignal",notes = "testPoolTaskSignal")
    FlowOperateResult testPoolTaskSignal(FlowInvokeParams flowInvokeParams);

    /**
     * 任务创建池
     * @param flowInvokeParams 流程参数
     * @return
     */
    @POST
    @Path("testPoolTaskCreatePool")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "testPoolTaskCreatePool",notes = "testPoolTaskCreatePool")
    FlowOperateResult testPoolTaskCreatePool(FlowInvokeParams flowInvokeParams);
}
