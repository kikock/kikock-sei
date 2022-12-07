package com.kcmp.ck.config.entity.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 全局参数键
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class ParamConfigKey implements Serializable {

    /**
     * 平台Id
     */
    private String platformId;
    /**
     * 运行环境Id
     */
    private String runtimeEnvironmentId;
    /**
     * 参数键
     */
    private String paramKey;

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

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
}
