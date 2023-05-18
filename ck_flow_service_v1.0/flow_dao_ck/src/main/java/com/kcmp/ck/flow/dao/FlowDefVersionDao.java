package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by kikock
 * 流程定义版本dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowDefVersionDao extends BaseEntityDao<FlowDefVersion> {

    /**
     * 获取流程定义版本
     * @param defId         代码
     * @param versionCode   版本号
     * @return
     */

    @QueryHints(value={@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fv from FlowDefVersion fv where fv.flowDefination.id  = :defId and fv.versionCode = :versionCode")
    FlowDefVersion findByDefIdAndVersionCode(@Param("defId") String defId, @Param("versionCode") Integer versionCode);

    /**
     * 获取流程定义版本
     * @param defId     流程定义id
     * @return
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<FlowDefVersion> findByFlowDefinationId(String defId);

    /**
     * 获取激活的流程定义版本
     * @param defId     流程定义id
     * @return
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fv from FlowDefVersion fv where fv.flowDefination.id  = :defId and fv.flowDefinationStatus = 1 order by fv.versionCode desc")
    List<FlowDefVersion> findByFlowDefinationIdActivate(@Param("defId") String defId);

    /**
     * 获取激活的流程定义版本
     * @param defKey    定义key
     * @return
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fv from FlowDefVersion fv where fv.defKey  = :defKey and fv.flowDefinationStatus = 1 order by fv.versionCode desc")
    List<FlowDefVersion> findByKeyActivate(@Param("defKey") String defKey);

    /**
     * 获取流程定义版本
     * @param actDefId 定义id
     * @return
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    FlowDefVersion findByActDefId(String actDefId);

    /**
     * 获取流程定义版本
     * @param defKey        定义key
     * @param versionCode   版本号
     * @return
     */
    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select fv from FlowDefVersion fv where fv.defKey  = :defKey and fv.versionCode = :versionCode")
    FlowDefVersion findByKeyAndVersionCode(@Param("defKey") String defKey, @Param("versionCode") Integer versionCode);
}
