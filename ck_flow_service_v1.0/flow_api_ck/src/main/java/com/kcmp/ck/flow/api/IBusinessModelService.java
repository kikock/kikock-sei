package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.vo.ConditionVo;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 业务实体服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("businessModel")
@Api(value = "IBusinessModelService 业务实体服务API接口")
public interface IBusinessModelService extends IBaseService<BusinessModel, String> {

//    /**
//     * 保存一个实体
//     * @param businessModel 实体
//     * @return 保存后的实体
//     */
//    @POST
//    @Path("save")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
//    OperateResultWithData<BusinessModel> save(BusinessModel businessModel);

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
    PageResult<BusinessModel> findByPage(Search searchConfig);

    /**
     * 根据应用模块id查询业务实体
     * @param appModuleId 业务模块id
     * @return 实体清单
     */
    @POST
    @Path("findByAppModuleId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取业务实体数据", notes = "测试 获取业务实体数据")
    List<BusinessModel> findByAppModuleId(@QueryParam("appModuleId") String appModuleId);

    /**
     * 根据应用模块id查询业务实体
     * @param classNmae 业务模块代码
     * @return 实体对象
     */
    @POST
    @Path("findByClassName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取业务实体数据", notes = "测试 获取业务实体数据")
    BusinessModel findByClassName(@QueryParam("classNmae") String classNmae);

    /**
     * 获取当前用户权限范围所有
     * @return 实体清单
     */
    @GET
    @Path("findAllByAuth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过当前用户筛选有权限的数据", notes = "通过当前用户筛选有权限的数据")
    List<BusinessModel> findAllByAuth();

    /**
     * 查询条件属性说明
     * @param businessModelCode 业务实体代码
     * @return 实体对象
     * @throws ClassNotFoundException
     */
    @POST
    @Path("getProperties")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询条件属性说明", notes = "查询条件属性说明")
    ResponseData getProperties(@QueryParam("businessModelCode") String businessModelCode)throws ClassNotFoundException;

    /**
     * 查询条件属性说明
     * @param businessModelCode 业务实体代码
     * @return 实体对象
     * @throws ClassNotFoundException
     */
    @POST
    @Path("getPropertiesForConditionPojo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询条件属性说明", notes = "查询条件属性说明")
    List<ConditionVo> getPropertiesForConditionPojo(@QueryParam("businessModelCode") String businessModelCode) throws ClassNotFoundException;

    /**
     * 通过任务ID获取表单明细（移动端专用）
     */
    @POST
    @Path("getPropertiesByTaskIdOfModile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过任务ID获取表单明细（移动端专用）", notes = "通过任务ID获取表单明细（移动端专用）")
    ResponseData getPropertiesByTaskIdOfModile(@QueryParam("taskId")String taskId,@QueryParam("typeId")String typeId, @QueryParam("id") String id);


    /**
     * 通过流程实例ID获取表单明细（长城移动端专用）
     */
    @POST
    @Path("getPropertiesByInstanceIdOfModile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过实例ID获取表单明细（长城移动端专用）", notes = "通过实例ID获取表单明细（长城移动端专用）")
    ResponseData getPropertiesByInstanceIdOfModile(@QueryParam("instanceId")String instanceId,@QueryParam("typeId")String typeId, @QueryParam("id") String id);
}
