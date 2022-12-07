package com.kcmp.core.ck.api;

import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.search.PageResult;
import com.kcmp.core.ck.search.Search;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by kikock
 * 分页查询业务实体API服务接口
 * @email kikock@qq.com
 **/
public interface IFindByPageService<T extends BaseEntity> {
    /**
     * 分页查询业务实体
     * @param search 查询参数
     * @return 分页查询结果
     */
    @POST
    @Path("findByPage")
    @ApiOperation(value = "分页查询业务实体", notes = "分页查询业务实体")
    PageResult<T> findByPage(Search search);
}
