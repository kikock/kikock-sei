package com.kcmp.ck.flow.api;

import com.kcmp.ck.flow.entity.TaskMakeOverPower;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 流程任务转授权API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("taskMakeOverPower")
@Api(value = "ITaskMakeOverPowerService 流程任务转授权API接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ITaskMakeOverPowerService extends IBaseService<TaskMakeOverPower, String> {

    /**
     * 查询自己的转授权单据
     */
    @POST
    @Path("findAllByUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询自己的转授权单据", notes = "查询自己的转授权单据")
    ResponseData findAllByUser();

    /**
     * 查询转授权处理历史（自己待办转授权人处理的）
     */
    @POST
    @Path("findAllHistoryByUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询转授权处理历史", notes = "查询转授权处理历史")
    ResponseData findAllHistoryByUser();

    /**
     * 保存一个实体
     * @param entity 实体
     * @return 保存后的实体
     */
    @POST
    @Path("setUserAndsave")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<TaskMakeOverPower> setUserAndsave(TaskMakeOverPower entity);

    /**
     * 通过ID修改启用状态
     * @param id
     * @return
     */
    @POST
    @Path("updateOpenStatusById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过ID修改启用状态", notes = "通过ID修改启用状态")
    ResponseData updateOpenStatusById(@QueryParam("id")String id);
}
