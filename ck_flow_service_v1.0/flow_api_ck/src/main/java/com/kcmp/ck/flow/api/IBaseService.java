package com.kcmp.ck.flow.api;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Persistable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by kikock
 * 工作流程服务通用基本API
 * @author kikock
 * @email kikock@qq.com
 **/
public interface IBaseService<T extends Persistable<? extends Serializable> & Serializable, ID extends Serializable>  {

    /**
     * 通过Id获取实体
     * @param id 业务id
     * @return 实体
     */
    @GET
    @Path("getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过Id获取实体",notes = "测试 通过Id获取实体")
    T findOne(@PathParam("id")String id);

    /**
     * 获取所有实体
     * @return 实体清单
     */
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取所有实体",notes = "测试 获取所有实体")
    List<T> findAll();

    /**
     * 保存一个实体
     * @param entity 实体
     * @return 保存后的实体
     */
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "保存实体",notes = "测试 保存实体")
    OperateResultWithData<T> save(T entity);

//    /**
//     * 删除一个实体
//     * @param entity 实体
//     * @return 删除后的返回信息
//     */
//    @DELETE
//    @Path("delete")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "删除实体",notes = "测试 删除实体")
//    OperateResult delete(T entity);
//
//    /**
//     * 批量删除实体
//     * @param entities
//     */
//    @DELETE
//    @Path("deletes")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "删除实体",notes = "测试 删除实体")
//    void delete(Iterable<T> entities);

    /**
     * 通过ID删除实体
     * @param id ID
     * @return 删除后的返回信息
     */
    @DELETE
    @Path("deleteById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过ID删除实体",notes = "测试 通过ID删除实体")
    OperateResult delete(ID id);

    /**
     * 通过ID集合批量删除实体
     * @param ids id集合
     */
    @DELETE
    @Path("deleteByIds")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通过ID集合删除实体集",notes = "测试 通过ID集合删除实体集")
    void delete(Collection<ID> ids);

    /**
     * 获取分页数据
     * @param searchConfig 搜索条件对象
     * @return 实体清单
     */
    @POST
    @Path("findByPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取分页数据", notes = "测试 获取分页数据")
    PageResult<T> findByPage(Search searchConfig);
}
