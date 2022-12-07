package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 环境变量配置dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface EnvironmentVarConfigDao extends JpaRepository<EnvironmentVarConfig, String> {

    /**
     * 获取环境变量配置
     *
     * @param runtimeEnvironmentId  运行环境Id
     * @param environmentVariableId 环境变量Id
     * @return 环境变量配置
     */
    EnvironmentVarConfig findFirstByRuntimeEnvironmentIdAndEnvironmentVariableId(String runtimeEnvironmentId, String environmentVariableId);

    /**
     * 获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 环境变量Id
     * @return 环境变量配置清单
     */
    @Query("select c from EnvironmentVarConfig c where c.environmentVariable.platformId = ?1 and c.runtimeEnvironmentId = ?2")
    List<EnvironmentVarConfig> findAllByPlatformIdAndRuntimeEnvironmentId(String platformId, String runtimeEnvironmentId);

    /**
     * 获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 环境变量Id
     * @param applicationModuleId  应用模块Id
     * @return 环境变量配置清单
     */
    @Query("select c from EnvironmentVarConfig c where c.environmentVariable.platformId = ?1 and c.runtimeEnvironmentId = ?2 and c.environmentVariable.applicationModuleId = ?3")
    List<EnvironmentVarConfig> findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleId(String platformId, String runtimeEnvironmentId, String applicationModuleId);

    /**
     * 获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 环境变量Id
     * @return 环境变量配置清单
     */
    @Query("select c from EnvironmentVarConfig c where c.environmentVariable.platformId = ?1 and c.runtimeEnvironmentId = ?2 and c.environmentVariable.applicationModuleId is not null")
    List<EnvironmentVarConfig> findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleIdIsNotNull(String platformId, String runtimeEnvironmentId);

    /**
     * 获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 环境变量Id
     * @return 环境变量配置清单
     */
    @Query("select c from EnvironmentVarConfig c where c.environmentVariable.platformId = ?1 and c.runtimeEnvironmentId = ?2 and c.environmentVariable.applicationModuleId is null")
    List<EnvironmentVarConfig> findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleIdIsNull(String platformId, String runtimeEnvironmentId);

    /**
     * 获取环境变量配置
     *
     * @param runtimeEnvironmentId 环境变量Id
     * @param applicationModuleId  应用模块Id
     * @return 环境变量配置清单
     */
    @Query("select c from EnvironmentVarConfig c where c.environmentVariable.applicationModuleId is not null and c.runtimeEnvironmentId = ?1 and c.environmentVariable.applicationModuleId = ?2")
    List<EnvironmentVarConfig> findAllByRuntimeEnvironmentIdAndApplicationModuleId(String runtimeEnvironmentId, String applicationModuleId);

    /**
     * 通过环境变量Id删除所有配置值
     *
     * @param environmentVariableId 环境变量Id
     */
    void deleteAllByEnvironmentVariableId(String environmentVariableId);

    /**
     * 通过运行环境Id删除所有配置值
     *
     * @param runtimeEnvironmentId 运行环境Id
     */
    void deleteAllByRuntimeEnvironmentId(String runtimeEnvironmentId);
}
