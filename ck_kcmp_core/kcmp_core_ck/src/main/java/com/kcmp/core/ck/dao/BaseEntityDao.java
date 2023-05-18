package com.kcmp.core.ck.dao;

import com.kcmp.core.ck.dao.jpa.BaseDao;
import com.kcmp.core.ck.entity.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by kikock
 * 基础实体dao
 * @email kikock@qq.com
 **/
@NoRepositoryBean
public interface BaseEntityDao<T extends BaseEntity> extends BaseDao<T, String> {
}
