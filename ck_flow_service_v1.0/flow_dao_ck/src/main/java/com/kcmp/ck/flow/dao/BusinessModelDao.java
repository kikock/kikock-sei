package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.BusinessModel;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by kikock
 * 业务实体dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface BusinessModelDao extends BaseEntityDao<BusinessModel>, BusinessModelExtDao {

    /**
     * 根据应用模块的id来查询业务实体
     * @param appModuleId 应用模块Id,xx
     * @return 岗位清单
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<BusinessModel> findByAppModuleId(String appModuleId);

    /**
     * 根据className来查询业务实体
     * @param className 业务实例代码（类全路径）
     * @return 岗位清单
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    BusinessModel findByClassName(String className);
}
