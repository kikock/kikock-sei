package com.kcmp.core.ck.dao.impl;

import com.kcmp.core.ck.dao.jpa.impl.BaseDaoImpl;
import com.kcmp.core.ck.entity.BaseEntity;

import javax.persistence.EntityManager;

/**
 * Created by kikock
 * 基础实体dao实现
 * @email kikock@qq.com
 **/
public class BaseEntityDaoImpl<T extends BaseEntity> extends BaseDaoImpl<T, String> {

    public BaseEntityDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

}
