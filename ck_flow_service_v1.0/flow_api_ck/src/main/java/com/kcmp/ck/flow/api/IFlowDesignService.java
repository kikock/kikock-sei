package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.ck.flow.basic.vo.OrganizationDimension;
import com.kcmp.ck.flow.basic.vo.Position;
import com.kcmp.ck.flow.basic.vo.PositionCategory;
import com.kcmp.ck.flow.vo.SaveEntityVo;
import com.kcmp.ck.flow.vo.SearchVo;
import com.kcmp.ck.vo.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by kikock
 * 流程设计器默认服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("design")
@Api(value = "IFlowDesignService 流程设计器默认服务API接口")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IFlowDesignService {

    /**
     * 获取流程设计
     * @param id
     * @param versionCode
     * @param businessModelCode
     * @param businessId
     * @return
     */
    @POST
    @Path("getEntity")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取流程设计", notes = "获取流程设计")
    ResponseData getEntity(@QueryParam("id") String id,
                           @QueryParam("versionCode") Integer versionCode,
                           @QueryParam("businessModelCode") String businessModelCode,
                           @QueryParam("businessId") String businessId);

    /**
     * 流程设计保存(发布)
     * @param entityVo
     * @return
     * @throws JAXBException
     * @throws UnsupportedEncodingException
     * @throws CloneNotSupportedException
     */
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "流程设计保存(发布)", notes = "流程设计保存(发布)")
    ResponseData save(SaveEntityVo entityVo) throws JAXBException, UnsupportedEncodingException, CloneNotSupportedException;

    /**
     * 查询流程服务地址
     * @param busModelId
     * @return
     * @throws ParseException
     */
    @POST
    @Path("listAllServiceUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "查询流程服务地址", notes = "查询流程服务地址")
    ResponseData listAllServiceUrl(@QueryParam("busModelId") String busModelId) throws ParseException;

    /**
     * 获取工作界面
     * @param businessModelId
     * @return
     */
    @POST
    @Path("listAllWorkPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取工作界面", notes = "获取工作界面")
    ResponseData listAllWorkPage(@QueryParam("businessModelId") String businessModelId);

    /**
     * 获取岗位
     * @param searchVo
     * @return
     */
    @POST
    @Path("listPos")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取岗位", notes = "获取岗位")
    PageResult<Position> listPositon(SearchVo searchVo);

    /**
     * 获取岗位类别
     * @return
     */
    @POST
    @Path("listPositonType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取岗位类别", notes = "获取岗位类别")
    List<PositionCategory> listPositonType();

    /**
     * 获取组织维度
     * @return
     */
    @POST
    @Path("listOrganizationDimension")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取组织维度", notes = "获取组织维度")
    List<OrganizationDimension> listOrganizationDimension();

    /**
     * 根据流程实例获取当前流程所在节点
     * @param id
     * @param instanceId
     * @return
     */
    @POST
    @Path("getLookInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取组织维度", notes = "获取组织维度")
    ResponseData getLookInfo(@QueryParam("id") String id, @QueryParam("instanceId") String instanceId);
}
