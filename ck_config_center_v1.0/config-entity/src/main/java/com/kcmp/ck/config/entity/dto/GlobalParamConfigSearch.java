package com.kcmp.ck.config.entity.dto;

import java.io.Serializable;

/**
 * Created by kikock
 * 全局参数配置查询参数
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class GlobalParamConfigSearch implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 平台Id
     */
    private String platformId;

    /**
     * 关键字
     */
    private String name;
    /**
     * 是否为平台级全局参数
     */
    private boolean platformParam;
    /**
     * 应用模块Id
     */
    private String moduleId;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public boolean isPlatformParam() {
        return platformParam;
    }

    public void setPlatformParam(boolean platformParam) {
        this.platformParam = platformParam;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPlatformParam() {
        return platformParam;
    }
}
