package com.kcmp.core.ck.dao.impl;

import com.kcmp.core.ck.entity.AbstractEntity;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.RelationEntity;
import com.kcmp.core.ck.entity.TreeEntity;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kikock
 * 数据访问实现基类装配类
 * @email kikock@qq.com
 **/
public class DaoImplMapper<T> {
    /**
     * 映射关系
     */
    class ImplMap {
        ImplMap(Class<?> key, Class<?> value) {
            this.key = key;
            this.value = value;
        }

        /**
         * 业务实体基类Class
         */
        Class<?> key;
        /**
         * 业务实体对应Dao基类实现Class
         */
        Class<?> value;

        public Class<?> getKey() {
            return key;
        }

        public void setKey(Class<?> key) {
            this.key = key;
        }

        public Class<?> getValue() {
            return value;
        }

        public void setValue(Class<?> value) {
            this.value = value;
        }
    }

    private List<ImplMap> map;

    //构造装配映射
    public DaoImplMapper() {
        map = new LinkedList<>();
        //分配关系业务实体
        map.add(new ImplMap(RelationEntity.class, BaseRelationDaoImpl.class));
        //树形业务实体
        map.add(new ImplMap(TreeEntity.class, BaseTreeDaoImpl.class));
        //业务实体基类
        map.add(new ImplMap(BaseEntity.class, BaseEntityDaoImpl.class));
        //持久化实体基类
        map.add(new ImplMap(AbstractEntity.class, BaseDaoImpl.class));
    }

    /**
     * 获取数据访问实现基类
     * @param domainClass   业务实体类型
     * @param entityManager 数据环境
     * @return 数据访问实现基类
     */
    public SimpleJpaRepository<T, Serializable> getTargetRepository(Class<T> domainClass, EntityManager entityManager) {
        //按装配顺序确定实现类
        for (ImplMap m : map) {
            try {
                if (m.getKey().isAssignableFrom(domainClass)) {
                    Constructor constructor = m.getValue().getConstructor(Class.class, EntityManager.class);
                    return (SimpleJpaRepository<T, Serializable>) constructor.newInstance(domainClass, entityManager);
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        //默认
        return new SimpleJpaRepository(domainClass, entityManager);
    }

    /**
     * 获取数据访问实现类类型
     * @param domainClass 业务实体类型
     * @return 实现类类型
     */
    public Class<?> getRepositoryBaseClass(Class<T> domainClass) {
        //按装配顺序确定实现类
        for (ImplMap m : map) {
            if (m.getKey().isAssignableFrom(domainClass)) {
                return m.getValue();
            }
        }
        //默认
        return SimpleJpaRepository.class;
    }
}
