package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.core.ck.dao.impl.BaseDaoFactoryBean;
import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
@ConditionalOnBean({DataSource.class})
@ConditionalOnClass({BaseDaoFactoryBean.class, BaseEntityDaoImpl.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableJpaRepositories(basePackages = {"com.kcmp.**.dao", "com.kcmp.**.ck.dao"}, repositoryFactoryBeanClass =
        BaseDaoFactoryBean.class)
@EnableTransactionManagement
public class JpaAutoConfiguration {

}
