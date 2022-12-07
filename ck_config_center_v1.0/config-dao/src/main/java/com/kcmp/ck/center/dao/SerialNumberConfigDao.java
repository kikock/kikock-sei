package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.SerialNumberConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 编号生成器配置dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface SerialNumberConfigDao extends JpaRepository<SerialNumberConfig, String> {

    /**
     * 获取运行环境的所有配置
     *
     * @param envCode 运行环境代码
     * @return 编号生成器配置清单
     */
    List<SerialNumberConfig> findByEnvCode(String envCode);

    /**
     * 按名称筛选运行环境的所有配置
     *
     * @param envCode 运行环境代码
     * @param name 筛选字段
     * @return 编号生成器配置清单
     */
    @Query(value ="select a from SerialNumberConfig a where (a.entityClassName like  %:name%  " +
            "or a.name like  %:name%) and a.envCode = :envCode ")
    List<SerialNumberConfig> findAllByEnvCodeAndName(String envCode ,String name);



    /**
     * 获取编号生成器配置
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名
     * @param isolationCode   隔离码
     * @return 获取编号生成器配置
     */
    SerialNumberConfig findFirstByEnvCodeAndEntityClassNameAndIsolationCode(String envCode, String entityClassName, String isolationCode);
    /**
     * 获取编号生成器配置
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名
     * @return 获取编号生成器配置
     */
    SerialNumberConfig findFirstByEnvCodeAndEntityClassName(String envCode, String entityClassName);

    /**
     * 获取编号生成器配置
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名
     * @param isolationCode   隔离码
     * @param id              id
     * @return 获取编号生成器配置
     */
    Boolean existsByEnvCodeAndEntityClassNameAndIsolationCodeAndIdNot(String envCode, String entityClassName, String isolationCode, String id);
}
