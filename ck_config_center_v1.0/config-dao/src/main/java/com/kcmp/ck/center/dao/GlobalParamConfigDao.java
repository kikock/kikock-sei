package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.dto.ParamConfigVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 全局参数配置dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface GlobalParamConfigDao extends JpaRepository<GlobalParamConfig, String> {

    /**
     * 获取全局参数配置清单
     *
     * @param platformId          平台Id
     * @param applicationModuleId 应用模块Id
     * @return 全局参数配置清单
     */
    List<GlobalParamConfig> findAllByPlatformIdAndApplicationModuleId(String platformId, String applicationModuleId);

    /**
     * 获取平台的应用模块全局参数配置清单
     *
     * @param platformId 平台Id
     * @return 全局参数配置清单
     */
    List<GlobalParamConfig> findAllByPlatformIdAndApplicationModuleIdIsNotNull(String platformId);

    /**
     * 获取平台中可以引用的全局参数
     *
     * @param platformId 平台Id
     * @return 全局参数键和说明
     */
    @Query("select new com.kcmp.ck.config.entity.dto.ParamConfigVo(p.paramKey,p.remark) from GlobalParamConfig p where p.platformId = ?1 and p.applicationModuleId is null ")
    List<ParamConfigVo> getCanReferenceParams(String platformId);

    /**
     * 通过平台Id和配置键获取全局参数配置
     *
     * @param platformId 平台Id
     * @param paramKey   配置键
     * @return 全局参数配置
     */
    List<GlobalParamConfig> findByPlatformIdAndParamKey(String platformId, String paramKey);

    /**
     * 通过平台Id，应用模块Id和配置键获取全局参数配置
     *
     * @param platformId          平台Id
     * @param applicationModuleId 应用模块Id
     * @param paramKey            配置键
     * @return 全局参数配置
     */
    GlobalParamConfig findFirstByPlatformIdAndApplicationModuleIdAndParamKey(String platformId, String applicationModuleId, String paramKey);
}
