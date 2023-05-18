package com.kcmp.ck.flow.dao.impl;

import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;
import com.kcmp.ck.flow.dao.BusinessModelExtDao;
import com.kcmp.ck.flow.entity.BusinessModel;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by kikock
 * 业务实体扩展dao实现
 * @author kikock
 * @email kikock@qq.com
 **/
public class BusinessModelDaoImpl extends BaseEntityDaoImpl<BusinessModel> implements BusinessModelExtDao {

    public BusinessModelDaoImpl(EntityManager entityManager) {
        super(BusinessModel.class, entityManager);
    }

    /**
     * 根据编码列表查询业务实体列表
     * @param codeList  关联的应用模块编码列表
     * @return
     */
    @Override
    public List<BusinessModel> findByAppModuleCodes(List<String> codeList){
        TypedQuery<BusinessModel> businessModelQuery = entityManager.createQuery("select bm from BusinessModel bm where bm.appModule.code in :codeList  order by bm.lastEditedDate desc", BusinessModel.class);
        businessModelQuery.setParameter("codeList",codeList);
        List<BusinessModel> businessModelList = businessModelQuery.getResultList();
        return businessModelList;
    }
}
