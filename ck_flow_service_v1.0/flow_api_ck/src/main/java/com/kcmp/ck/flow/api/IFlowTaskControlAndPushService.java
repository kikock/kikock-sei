package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.api.IBaseRelationService;
import com.kcmp.ck.flow.entity.FlowTaskControlAndPush;
import com.kcmp.ck.flow.entity.FlowTaskPush;
import com.kcmp.ck.flow.entity.FlowTaskPushControl;
import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 推送任务关系API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowTaskControlAndPush")
@Api(value = "IFlowTaskControlAndPushService 推送任务关系API接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IFlowTaskControlAndPushService extends IBaseRelationService<FlowTaskControlAndPush, FlowTaskPushControl, FlowTaskPush> {

}
