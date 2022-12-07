package com.kcmp.core.ck.dao.impl;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * Created by kikock
 * 工厂dao基类
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public class BaseDaoFactoryBean<R extends JpaRepository<T, Serializable>, T extends Persistable>
        extends JpaRepositoryFactoryBean<R, T, Serializable> {

    public BaseDaoFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager) {
            @Override
            protected SimpleJpaRepository<T, Serializable> getTargetRepository(
                    RepositoryInformation information, EntityManager entityManager) {
                Class<T> domainClass = (Class<T>) information.getDomainType();
                return new DaoImplMapper<T>().getTargetRepository(domainClass, entityManager);
            }

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
                if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
                    return QuerydslJpaRepository.class;
                } else {
                    Class clazz = metadata.getDomainType();

                    return new DaoImplMapper<T>().getRepositoryBaseClass(clazz);
                }
            }

            /**
             * 返回给定的存储库接口是否需要选择特定于QueryDsl的实现
             * @param repositoryInterface
             * @return
             */
            private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
                return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
            }
        };
    }
}
