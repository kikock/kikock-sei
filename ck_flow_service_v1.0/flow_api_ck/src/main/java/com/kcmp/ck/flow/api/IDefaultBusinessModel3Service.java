package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.api.IBaseEntityService;
import com.kcmp.core.ck.api.IFindByPageService;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.entity.DefaultBusinessModel3;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 销售业务表单服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("defaultBusinessModel3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "IDefaultBusinessModel3Service 销售业务表单服务API接口")
public interface IDefaultBusinessModel3Service extends IBaseEntityService<DefaultBusinessModel3>, IFindByPageService<DefaultBusinessModel3> {

    /**
     * 测试自定义执行人选择
     * @param businessId 业务单据id
     * @param paramJson  json参数
     * @return 执行人列表
     */
    @POST
    @Path("getPersonToExecutorConfig")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "根据组织Id获取excutor",notes = "测试 根据组织Id获取excutor")
    List<Executor> getPersonToExecutorConfig(@QueryParam("businessId") String businessId, @QueryParam("paramJson") String paramJson);

}
