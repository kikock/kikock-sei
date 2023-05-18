package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.AppModule;
import org.springframework.stereotype.Repository;

/**
 * Created by kikock
 * 应用模块dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface AppModuleDao extends BaseEntityDao<AppModule>, AppModuleExtDao {
}
