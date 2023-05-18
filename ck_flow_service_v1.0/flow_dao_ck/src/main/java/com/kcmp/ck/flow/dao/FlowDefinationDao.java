package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowDefination;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by kikock
 * 流程定义dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowDefinationDao extends BaseEntityDao<FlowDefination> {

    /**
     * 根据代码获取流程定义
     * @param defKey    代码
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    FlowDefination findByDefKey(String defKey);

    /**
     * 根据流程类型代码获取流程定义
     * @param typeCode  流程类型代码
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select f from FlowDefination f where f.flowType.id = (select ft.id from FlowType ft where ft.code  = :typeCode)  order by f.priority desc,f.lastEditedDate desc")
    List<FlowDefination> findByTypeCode(@Param("typeCode") String typeCode);

    /**
     * 获取流程定义
     * @param typeCode  流程类型代码
     * @param orgId     组织机构id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select f from FlowDefination f where f.flowType.id = (select ft.id from FlowType ft where ft.code  = :typeCode) and f.orgId = :orgId  order by f.priority desc,f.lastEditedDate desc")
    List<FlowDefination> findByTypeCodeAndOrgId(@Param("typeCode") String typeCode, @Param("orgId") String orgId);

    /**
     * 获取流程定义
     * @param typeCode  流程类型代码
     * @param orgCode   组织机构代码
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select f from FlowDefination f where f.flowDefinationStatus = 1 and f.flowType.id = (select ft.id from FlowType ft where ft.code  = :typeCode) and f.orgCode = :orgCode  order by f.priority desc,f.lastEditedDate desc")
    List<FlowDefination> findByTypeCodeAndOrgCode(@Param("typeCode") String typeCode, @Param("orgCode") String orgCode);
}
