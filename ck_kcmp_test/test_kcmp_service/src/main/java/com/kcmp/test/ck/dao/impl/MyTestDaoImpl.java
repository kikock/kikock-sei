package com.kcmp.test.ck.dao.impl;


import com.kcmp.test.ck.dao.ext.MyTestExtDao;
import com.kcmp.test.ck.entity.MyTest;
import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by kikock
 * 应用模块扩展dao实现
 * @author kikock
 * @email kikock@qq.com
 **/
public class MyTestDaoImpl extends BaseEntityDaoImpl<MyTest> implements MyTestExtDao {

    public MyTestDaoImpl(EntityManager entityManager) {
        super(MyTest.class, entityManager);
    }


    @Override
    public List<MyTest> findByCodes(List<String> codeList) {
        // TypedQuery<MyTest> appModuleQuery = entityManager.createQuery("select am from MyTest am where am.code in :codeList ", MyTest.class);
        // appModuleQuery.setParameter("codeList",codeList);
        // List<MyTest> AppModuleList = appModuleQuery.getResultList();
        // return AppModuleList;
        return null;
    }
}
