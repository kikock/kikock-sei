package com.kcmp.core.ck.api;

import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.core.ck.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by kikock
 * 业务实体API基础服务接口
 * @author kikock
 * @email kikock@qq.com
 **/
public interface IBaseEntityService<T extends BaseEntity> {
    /**
     * 保存业务实体
     * @param entity 业务实体
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存业务实体", notes = "保存一个业务实体")
    OperateResultWithData<T> save(T entity);

    /**
     * 删除业务实体
     * @param id 业务实体Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除业务实体", notes = "删除一个业务实体")
    OperateResult delete(String id);

    /**
     * 通过Id获取一个业务实体
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    T findOne(@QueryParam("id") String id);
}
