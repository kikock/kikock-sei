package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowType;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by kikock
 * 流程类型dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowTypeDao extends BaseEntityDao<FlowType> {

    /**
     * 根据业务实体的id来查询流程类型
     * @param businessModelId   业务实体id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<FlowType> findByBusinessModelId(String businessModelId);

    /**
     * 通过业务实体类名获取流程类型
     * @param className 业务实体类名
     * @return 流程类型清单
     */
    List<FlowType> findByBusinessModel_ClassName(String className);
}
