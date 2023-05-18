package com.kcmp.ck.flow.dao.impl;

import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;
import com.kcmp.ck.flow.dao.AppModuleExtDao;
import com.kcmp.ck.flow.entity.AppModule;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by kikock
 * 应用模块扩展dao实现
 * @author kikock
 * @email kikock@qq.com
 **/
public class AppModuleDaoImpl extends BaseEntityDaoImpl<AppModule> implements AppModuleExtDao {

    public AppModuleDaoImpl(EntityManager entityManager) {
        super(AppModule.class, entityManager);
    }

    /**
     * 根据编码列表查询应用模块列表
     * @param codeList  编码列表
     * @return
     */
    @Override
    public List<AppModule> findByCodes(List<String> codeList){
        TypedQuery<AppModule> appModuleQuery = entityManager.createQuery("select am from AppModule am where am.code in :codeList  order by am.lastEditedDate desc", AppModule.class);
        appModuleQuery.setParameter("codeList",codeList);
        List<AppModule> AppModuleList = appModuleQuery.getResultList();
        return AppModuleList;
    }
}
