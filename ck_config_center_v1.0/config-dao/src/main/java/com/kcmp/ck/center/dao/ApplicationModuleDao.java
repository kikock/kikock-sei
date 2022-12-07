package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.ApplicationModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 应用模块dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface ApplicationModuleDao extends JpaRepository<ApplicationModule, String> {

    /**
     * 判断平台Id是否存在
     *
     * @param platformId 平台Id
     * @return 是否存在
     */
    ApplicationModule findFirstByPlatformId(String platformId);


    /**
     * 判断该平台模块是否存在
     *
     * @param platformId 平台Id
     * @return 是否存在
     */
    ApplicationModule findFirstByPlatformIdAndCode(String platformId,String code);

    /**
     * 获取平台模块
     *
     * @param platformId 平台Id
     * @return 是否存在
     */
    List<ApplicationModule> findByPlatformId(String platformId);
}
