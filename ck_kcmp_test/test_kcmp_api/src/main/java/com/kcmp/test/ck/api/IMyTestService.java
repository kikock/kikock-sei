package com.kcmp.test.ck.api;

import com.kcmp.ck.annotation.IgnoreCheckAuth;
import com.kcmp.test.ck.entity.MyTest;
import com.kcmp.core.ck.api.IBaseEntityService;
import com.kcmp.core.ck.api.IFindAllService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by kikock
 * 测试服务接口
 * @email kikock@qq.com
 * @create 2019-12-26 11:13
 **/
@Path("test")
@Api(value = "ITestService 测试服务接口")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IMyTestService extends IBaseEntityService<MyTest>, IFindAllService<MyTest> {

    /**
     * 通过代码查询
     *
     * @param code 代码
     * @return 应用模块
     */
    @GET
    @Path("findByCode")
    @ApiOperation(value = "查询测试类", notes = "通过代码查询测试类")
    @IgnoreCheckAuth
    MyTest findByCode(@QueryParam("code") String code);
}
