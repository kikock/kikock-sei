package com.kcmp.test.ck.service;



import com.kcmp.ck.annotation.IgnoreCheckAuth;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.test.ck.api.IMyTestService;
import com.kcmp.test.ck.dao.MyTestDao;
import com.kcmp.test.ck.entity.MyTest;
import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.kcmp.core.ck.dto.Search.createSearch;


/**
 * Created by kikock
 * 应用模块服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class MyTestService extends BaseEntityService<MyTest> implements IMyTestService {


    @Autowired
    private MyTestDao myTestDao;

    @Override
    protected BaseEntityDao<MyTest> getDao() {
        return myTestDao;
    }


    @Override
    public MyTest findByCode(String code) {
        Search search = createSearch();
        SearchFilter searchFilter =new SearchFilter("id", 1, SearchFilter.Operator.EQ);
        search.addFilter(searchFilter);
        long byCountNum = myTestDao.findByCountNum(search);
        System.out.println(byCountNum);
        List<MyTest> searchFilter1 = myTestDao.getSearchFilter(searchFilter);
        List<MyTest> byFilters = myTestDao.findByFilters(search);
        System.out.println(searchFilter1.size());
        System.out.println(byFilters.size());
        return null;
    }
}
