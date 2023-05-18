package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.BusinessWorkPageUrl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by kikock
 * 工作界面配置dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface BusinessWorkPageUrlDao extends BaseEntityDao<BusinessWorkPageUrl> {

    /**
     * 根据业务id和工作界面id获取工作界面配置
     * @param businessModuleId  业务id
     * @param workPageUrlId     工作界面id
     * @return
     */
    BusinessWorkPageUrl findByBusinessModuleIdAndWorkPageUrlId(String businessModuleId, String workPageUrlId);

    /**
     * 根据业务id获取工作界面配置
     * @param businessModuleId  业务id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<BusinessWorkPageUrl> findByBusinessModuleId(String businessModuleId);

    /**
     * 根据业务id删除工作界面配置
     * @param businessModuleId  业务id
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @Modifying
    @Query("delete from BusinessWorkPageUrl b where b.businessModuleId = :businessModuleId")
    void deleteBybusinessModuleId(@Param("businessModuleId") String businessModuleId);
}
