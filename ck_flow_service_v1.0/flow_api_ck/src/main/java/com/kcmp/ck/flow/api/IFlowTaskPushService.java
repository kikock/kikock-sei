package com.kcmp.ck.flow.api;

import com.kcmp.ck.flow.entity.FlowTaskPush;
import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 流程推送控制任务服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowTaskPush")
@Api(value = "IFlowTaskPushService 流程推送任务服务API接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IFlowTaskPushService extends IBaseService<FlowTaskPush, String> {

}
