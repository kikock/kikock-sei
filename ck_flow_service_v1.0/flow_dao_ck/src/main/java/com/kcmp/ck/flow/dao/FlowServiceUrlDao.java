package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 流程服务地址dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowServiceUrlDao extends BaseEntityDao<FlowServiceUrl> {

    /**
     * 通过流程类型id查找可配置工作页面
     * @param flowTypeId 流程类型id
     * @return
     */
    @Query("select w from FlowServiceUrl w where w.businessModel.id =  (select t.businessModel.id from FlowType t  where t.id = :flowTypeId) ")
    List<FlowServiceUrl> findByFlowTypeId(@Param("flowTypeId") String flowTypeId);

    /**
     * 通过业务实体id查找可配置工作页面
     * @param businessModelId 流程类型id
     * @return
     */
    @Query("select w from FlowServiceUrl w where w.businessModel.id = :businessModelId ")
    List<FlowServiceUrl> findByBusinessModelId(@Param("businessModelId") String businessModelId);
}
