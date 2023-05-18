package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowSolidifyExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kikock
 * 固化流程执行人dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowSolidifyExecutorDao  extends BaseEntityDao<FlowSolidifyExecutor> {

    /**
     * 根据业务id删除固化流程执行人
     * @param businessId    业务id
     * @return
     */
    @Transactional(readOnly = true)
    long deleteByBusinessId(String businessId);
}
