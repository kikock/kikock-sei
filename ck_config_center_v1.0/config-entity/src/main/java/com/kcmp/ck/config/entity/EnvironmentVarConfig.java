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

/**
 * Created by kikock
 * 环境变量配置实体类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "environment_var_config")
@DynamicInsert
@DynamicUpdate
public class EnvironmentVarConfig extends BaseConfigEntity {

    /**
     * 环境变量Id
     */
    @Column(name = "environment_variable_id", length = 36, nullable = false)
    private String environmentVariableId;
    /**
     * 环境变量
     */
    @ManyToOne
    @JoinColumn(name = "environment_variable_id", nullable = false, insertable = false, updatable = false)
    private EnvironmentVariable environmentVariable;
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
     * 配置值
     */
    @Column(name = "config_value", length = 800)
    private String configValue = "";
    /**
     * 是否加密
     */
    @Column(name = "encrypted", nullable = false)
    private Boolean encrypted = Boolean.FALSE;

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

    public String getEnvironmentVariableId() {
        return environmentVariableId;
    }

    public void setEnvironmentVariableId(String environmentVariableId) {
        this.environmentVariableId = environmentVariableId;
    }

    public EnvironmentVariable getEnvironmentVariable() {
        return environmentVariable;
    }

    public void setEnvironmentVariable(EnvironmentVariable environmentVariable) {
        this.environmentVariable = environmentVariable;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }
}
