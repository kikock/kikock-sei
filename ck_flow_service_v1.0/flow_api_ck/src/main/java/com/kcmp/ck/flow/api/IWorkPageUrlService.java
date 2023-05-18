package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.WorkPageUrl;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 工作界面配置管理服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("workPageUrl")
@Api(value = "IWorkPageUrlService 工作界面配置管理服务API接口")
public interface IWorkPageUrlService extends IBaseService<WorkPageUrl, String> {

    /**
     * 保存一个实体
     * @param workPageUrl 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<WorkPageUrl> save(WorkPageUrl workPageUrl);

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return 实体清单
     */
    @Override
    @POST
    @Path("findByPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<WorkPageUrl> findByPage(Search searchConfig);

    /**
     * 根据应用模块id查询业务实体t
     * @param appModuleId 应用模块id
     * @return 实体清单
     */
    @POST
    @Path("findByAppModuleId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    List<WorkPageUrl> findByAppModuleId(@QueryParam("appModuleId") String appModuleId);

    /**
     * 查看对应业务实体已选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @GET
    @Path("findSelectEdByAppModuleId/{appModuleId}/{businessModelId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查看对应业务实体已选中的工作界面",notes = "测试")
    List<WorkPageUrl> findSelectEdByAppModuleId(@PathParam("appModuleId") String appModuleId, @PathParam("businessModelId") String businessModelId);

    /**
     * 查看对应业务实体未选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 未选中的工作界面
     */
    @POST
    @Path("listAllNotSelectEdByAppModuleId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查看对应业务实体未选中的工作界面",notes = "查看对应业务实体未选中的工作界面")
    ResponseData listAllNotSelectEdByAppModuleId(@QueryParam("appModuleId") String appModuleId, @QueryParam("businessModelId") String businessModelId);

    /**
     * 查看对应业务实体未选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 选中的工作界面
     */
    @GET
    @Path("findNotSelectEdByAppModuleId/{appModuleId}/{businessModelId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查看对应业务实体未选中的工作界面",notes = "测试")
    List<WorkPageUrl> findNotSelectEdByAppModuleId(@PathParam("appModuleId") String appModuleId, @PathParam("businessModelId") String businessModelId);

    /**
     * 通过流程类型id查找拥有的工作界面
     * @param flowTypeId 流程类型id
     * @return 工作界面list
     */
    @GET
    @Path("findByFlowTypeId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过流程类型id查找拥有的工作界面",notes = "测试")
    List<WorkPageUrl> findByFlowTypeId(String flowTypeId);

    /**
     * 查看对应业务实体已选中的工作界面
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @POST
    @Path("listAllSelectEdByAppModuleId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务实体ID查看业务实体已选中的工作界面",notes = "查看对应业务实体已选中的工作界面")
    ResponseData listAllSelectEdByAppModuleId(@QueryParam("businessModelId") String businessModelId);

    /**
     * 查看对应业务实体已选中的工作界面
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @GET
    @Path("findSelectEdByBusinessModelId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过业务实体ID查看业务实体已选中的工作界面",notes = "测试")
    List<WorkPageUrl> findSelectEdByBusinessModelId(@QueryParam("businessModelId") String businessModelId);
}
