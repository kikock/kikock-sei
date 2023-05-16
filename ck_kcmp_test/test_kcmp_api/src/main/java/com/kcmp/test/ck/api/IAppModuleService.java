package com.kcmp.test.ck.api;


import com.kcmp.core.ck.api.IBaseEntityService;
import com.kcmp.core.ck.api.IFindAllService;
import com.kcmp.test.ck.entity.AppModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kikock
 * 应用模块服务API接口
 * @author kikock
 * @email kikock@qq.com
 **/
@Path("appModule")
@Api(value = "IAppModuleService 应用模块服务API接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IAppModuleService extends IBaseEntityService<AppModule>, IFindAllService<AppModule> {

    /**
     * 获取当前用户权限范围所有
     * @return 实体清单
     */
    @GET
    @Path("findAllByAuth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过当前用户筛选有权限的数据", notes = "通过当前用户筛选有权限的数据")
    List<AppModule> findAllByAuth();

}
