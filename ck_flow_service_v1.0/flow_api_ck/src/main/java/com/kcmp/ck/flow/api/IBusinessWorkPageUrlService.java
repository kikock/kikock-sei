package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.BusinessWorkPageUrl;
import com.kcmp.ck.vo.OperateResultWithData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 业务实体工作界面配置管理服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("businessWorkPageUrl")
@Api(value = "IBusinessWorkPageUrlService 业务实体工作界面配置管理服务API接口")
public interface IBusinessWorkPageUrlService extends IBaseService<BusinessWorkPageUrl, String> {

    /**
     * 保存一个实体
     * @param businessWorkPageUrl 实体
     * @return 保存后的实体
     */
    @Override
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<BusinessWorkPageUrl> save(BusinessWorkPageUrl businessWorkPageUrl);

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
    PageResult<BusinessWorkPageUrl> findByPage(Search searchConfig);

    /**
     * 保存设置的工作界面
     * @param id                业务实体id
     * @param selectWorkPageIds 工作界面的所有id
     * @param id                流程id
     */
    @POST
    @Path("saveBusinessWorkPageUrlByIds/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存设置的工作界面",notes = "测试") void saveBusinessWorkPageUrlByIds(@PathParam("id") String id, String selectWorkPageIds);
}
