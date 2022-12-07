package com.kcmp.core.ck.api;

import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.auth.AuthTreeEntityData;
import com.kcmp.core.ck.entity.auth.IDataAuthTreeEntity;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by kikock
 * 权限管理的树形业务实体API服务接口
 * @email kikock@qq.com
 **/
public interface IDataAuthTreeEntityService<T extends BaseEntity & IDataAuthTreeEntity> {
    /**
     * 通过业务实体Id清单获取数据权限树形实体清单
     * @param ids 业务实体Id清单
     * @return 数据权限树形实体清单
     */
    @POST
    @Path("getAuthTreeEntityDataByIds")
    @ApiOperation(value = "获取数据权限树形实体清单", notes = "通过业务实体Id清单获取数据权限树形实体清单")
    List<AuthTreeEntityData> getAuthTreeEntityDataByIds(List<String> ids);

    /**
     * 获取所有数据权限树形实体清单
     * @return 数据权限树形实体清单
     */
    @GET
    @Path("findAllAuthTreeEntityData")
    @ApiOperation(value = "获取所有数据权限树形实体清单", notes = "获取当前租户所有数据权限树形实体清单")
    List<AuthTreeEntityData> findAllAuthTreeEntityData();

    /**
     * 获取当前用户有权限的树形业务实体清单
     * @param featureCode 功能项代码
     * @return 有权限的树形业务实体清单
     */
    @GET
    @Path("getUserAuthorizedTreeEntities")
    @ApiOperation(value = "获取当前用户有权限的树形业务实体清单", notes = "获取当前用户有权限的树形业务实体清单")
    List<T> getUserAuthorizedTreeEntities(@QueryParam("featureCode") String featureCode);
}
