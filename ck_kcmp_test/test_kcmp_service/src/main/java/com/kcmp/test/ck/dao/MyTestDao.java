package com.kcmp.test.ck.dao;

import com.kcmp.test.ck.dao.ext.MyTestExtDao;
import com.kcmp.test.ck.entity.MyTest;
import com.kcmp.core.ck.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;


/**
 * Created by kikock
 * 应用dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface MyTestDao extends BaseEntityDao<MyTest>, MyTestExtDao {

    MyTest findByCode(String code);
}
