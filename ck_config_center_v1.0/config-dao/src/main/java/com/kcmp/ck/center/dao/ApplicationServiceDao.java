package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.ApplicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 应用服务dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface ApplicationServiceDao extends JpaRepository<ApplicationService, String> {

    /**
     * 判断应用模块Id是否存在
     *
     * @param applicationModuleId 应用模块Id
     * @return 应用服务
     */
    ApplicationService findFirstByApplicationModuleId(String applicationModuleId);

    /**
     * 通过AppId获取应用服务
     *
     * @param appId AppId
     * @return 应用服务
     */
    ApplicationService findFirstByAppId(String appId);

    /**
     * 判断运行环境Id是否存在
     *
     * @param runtimeEnvironmentId 运行环境Id
     * @return 应用服务
     */
    ApplicationService findFirstByRuntimeEnvironmentId(String runtimeEnvironmentId);

    /**
     * 通过平台和环境获取应用服务
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @param name 模块名称
     * @return 应用服务清单
     */
    @Query("select a from ApplicationService a where a.applicationModule.platformId = :platformId " +
            " and a.runtimeEnvironmentId = :runtimeEnvironmentId and a.applicationModule.name like %:name%")
    List<ApplicationService> findAllByPlatformIdAndRuntimeEnvironmentIdAndAndName(@Param("platformId") String platformId,
                                                                                    @Param("runtimeEnvironmentId") String runtimeEnvironmentId,@Param("name")String name);

    /**
     * 获取应用服务的AppId
     *
     * @param id 应用服务Id
     * @return 应用服务的AppId
     */
    @Query("select a.id from ApplicationService a where a.id = ?1 ")
    String getAppIdById(String id);

    /**
     * 模块+平台+运行环境 唯一
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @param applicationModuleId 运行环境Id
     * @return 应用服务清单
     */
    @Query("select a from ApplicationService a where a.applicationModule.platformId = :platformId " +
            "and a.runtimeEnvironmentId = :runtimeEnvironmentId and a.applicationModuleId =:applicationModuleId ")
    ApplicationService checkIsSave(@Param("platformId") String platformId,@Param("runtimeEnvironmentId") String runtimeEnvironmentId,@Param("applicationModuleId")String applicationModuleId);

}
