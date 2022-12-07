package com.kcmp.ck.config.entity;

import com.kcmp.ck.config.entity.enums.ParamDataTypeEnum;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by kikock
 * 全局参数配置实体类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "global_param_config")
@DynamicInsert
@DynamicUpdate
public class GlobalParamConfig extends BaseConfigEntity {

    /**
     * 平台Id
     */
    @Column(name = "platform_id", nullable = false, length = 36)
    private String platformId;
    /**
     * 平台
     */
    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false, insertable = false, updatable = false)
    private Platform platform;
    /**
     * 应用模块Id
     */
    @Column(name = "application_module_id", length = 36)
    private String applicationModuleId;
    /**
     * 应用模块
     */
    @ManyToOne
    @JoinColumn(name = "application_module_id", insertable = false, updatable = false)
    private ApplicationModule applicationModule;
    /**
     * 数据类型
     */
    @Enumerated
    @Column(name = "param_data_type", nullable = false)
    private ParamDataTypeEnum paramDataType;
    /**
     * 参数键
     */
    @Column(name = "param_key", length = 100, nullable = false)
    private String paramKey;
    /**
     * 参数值
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "param_value", nullable = false)
    private String paramValue;
    /**
     * 参数说明
     */
    @Column(name = "remark", length = 200, nullable = false)
    private String remark;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
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

    public ParamDataTypeEnum getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(ParamDataTypeEnum paramDataType) {
        this.paramDataType = paramDataType;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
