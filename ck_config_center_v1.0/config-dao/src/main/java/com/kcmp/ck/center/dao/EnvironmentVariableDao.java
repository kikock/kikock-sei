package com.kcmp.ck.center.dao;

import com.kcmp.ck.config.entity.EnvironmentVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kikock
 * 环境变量dao
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface EnvironmentVariableDao extends JpaRepository<EnvironmentVariable, String> {

    /**
     * 获取环境变量
     *
     * @param platformId 平台Id
     * @param code       变量代码
     * @return 环境变量
     */
    EnvironmentVariable findFirstByPlatformIdAndCode(String platformId, String code);
}
