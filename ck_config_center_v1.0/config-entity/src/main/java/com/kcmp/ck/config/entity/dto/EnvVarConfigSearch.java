package com.kcmp.ck.config.entity.dto;

import java.io.Serializable;

/**
 * Created by kikock
 * 环境变量配置查询参数
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class EnvVarConfigSearch implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 平台Id
     */
    private String platformId;
    /**
     * 运行环境Id
     */
    private String runtimeEnvironmentId;
    /**
     * 是否为平台级全局参数
     */
    private boolean platformParam;
    /**
     * 应用模块Id
     */
    private String applicationModuleId;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getRuntimeEnvironmentId() {
        return runtimeEnvironmentId;
    }

    public void setRuntimeEnvironmentId(String runtimeEnvironmentId) {
        this.runtimeEnvironmentId = runtimeEnvironmentId;
    }

    public boolean isPlatformParam() {
        return platformParam;
    }

    public void setPlatformParam(boolean platformParam) {
        this.platformParam = platformParam;
    }

    public String getApplicationModuleId() {
        return applicationModuleId;
    }

    public void setApplicationModuleId(String applicationModuleId) {
        this.applicationModuleId = applicationModuleId;
    }
}
