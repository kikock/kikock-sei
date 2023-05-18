package com.kcmp.ck.flow.api;

import com.kcmp.ck.flow.vo.DefaultStartParam;
import com.kcmp.ck.vo.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 工作流业务集成服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowIntegrate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "IFlowIntegrateService 工作流业务集成服务API接口")
public interface IFlowIntegrateService {
    /**
     * 使用默认值启动业务流程
     * @param startParam 启动参数
     * @return 操作结果
     */
    @POST
    @Path("startDefaultFlow")
    @ApiOperation(value = "使用默认值启动流程", notes = "使用默认流程类型和第一节点执行人来启动流程")
    OperateResult startDefaultFlow(DefaultStartParam startParam);
}
