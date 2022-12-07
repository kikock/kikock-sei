package com.kcmp.ck.config.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by kikock
 * 应用服务实体类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "application_service")
@DynamicInsert
@DynamicUpdate
public class ApplicationService extends BaseConfigEntity {

    /**
     * 应用标识Id
     */
    @Column(name = "app_id", length = 36, nullable = false, unique = true)
    private String appId;
    /**
     * 应用服务说明
     */
    @Column(name = "remark", length = 100, nullable = false)
    private String remark;
    /**
     * 应用模块Id
     */
    @Column(name = "application_module_id", length = 36, nullable = false)
    private String applicationModuleId;
    /**
     * 应用模块
     */
    @ManyToOne
    @JoinColumn(name = "application_module_id", nullable = false, insertable = false, updatable = false)
    private ApplicationModule applicationModule;
    /**
     * 运行环境Id
     */
    @Column(name = "runtime_environment_id", length = 36, nullable = false)
    private String runtimeEnvironmentId;
    /**
     * 运行环境
     */
    @ManyToOne
    @JoinColumn(name = "runtime_environment_id", nullable = false, insertable = false, updatable = false)
    private RuntimeEnvironment runtimeEnvironment;
    /**
     * API文档地址
     */
    @Column(name = "api_docs_url", length = 800)
    private String apiDocsUrl;


    @Transient
    private String platformId;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplicationModuleId() {
        return applicationModuleId;
    }

    public void setApplicationModuleId(String applicationModuleId) {
        this.applicationModuleId = applicationModuleId;
    }

    public ApplicationModule getApplicationModule() {
        return applicationModule;
    }

    public void setApplicationModule(ApplicationModule applicationModule) {
        this.applicationModule = applicationModule;
    }

    public String getRuntimeEnvironmentId() {
        return runtimeEnvironmentId;
    }

    public void setRuntimeEnvironmentId(String runtimeEnvironmentId) {
        this.runtimeEnvironmentId = runtimeEnvironmentId;
    }

    public RuntimeEnvironment getRuntimeEnvironment() {
        return runtimeEnvironment;
    }

    public void setRuntimeEnvironment(RuntimeEnvironment runtimeEnvironment) {
        this.runtimeEnvironment = runtimeEnvironment;
    }

    public String getApiDocsUrl() {
        return apiDocsUrl;
    }

    public void setApiDocsUrl(String apiDocsUrl) {
        this.apiDocsUrl = apiDocsUrl;
    }
}
