package com.kcmp.ck.flow.api;

import com.kcmp.ck.flow.clientapi.ICommonConditionService;
import io.swagger.annotations.Api;

import javax.ws.rs.*;

/**
 * Created by kikock
 * 条件通用服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("condition")
@Api(value = "IFlowCommonConditionService 条件通用服务API接口")
public interface IFlowCommonConditionService extends ICommonConditionService {
}
