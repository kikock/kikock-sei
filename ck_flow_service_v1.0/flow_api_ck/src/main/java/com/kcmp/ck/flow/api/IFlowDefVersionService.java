package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.constant.FlowDefinationStatus;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

/**
 * Created by kikock
 * 流程定义版本服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("flowDefVersion")
@Api(value = "IFlowDefVersionService 流程定义版本服务API接口")
public interface IFlowDefVersionService extends IBaseService<FlowDefVersion, String> {

    /**
     * 保存一个实体
     * @param flowDefVersion 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<FlowDefVersion> save(FlowDefVersion flowDefVersion);

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
    PageResult<FlowDefVersion> findByPage(Search searchConfig);

    /**
     * 通过json流程定义数据，保存流程版本定义
     * @param definition json对象实体
     * @throws    JAXBException  jaxb解析异常
     * @throws    CloneNotSupportedException 复制异常
     * @return 保存后的流程版本定义实体
     */
    @POST
    @Path("jsonSave")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "json流程定义保存实体",notes = "测试 json流程定义保存实体")
    OperateResultWithData<FlowDefVersion> save(Definition definition) throws JAXBException, CloneNotSupportedException;

    /**
     * 切换版本状态
     * @param id 单据id
     * @param status 状态
     * @return 结果
     */
    @POST
    @Path("changeStatus/{id}/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "改变流程版本状态",notes = "测试 改变流程版本状态")
    OperateResultWithData<FlowDefVersion> changeStatus(@PathParam("id") String id, @PathParam("status") FlowDefinationStatus status);
}
