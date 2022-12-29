package com.kcmp.ck.config.entity.dto;


import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import com.kcmp.ck.config.entity.EnvironmentVariable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kikock
 * 环境变量配置dto
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class EnvVarConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 环境变量Id
     */
    private String envId;
    /**
     * 环境变量name
     */
    private String envConfigId;
    /**
     * 环境变量Id
     */
    private String environmentVariableId;
    /**
     * 环境变量name
     */
    private String environmentVariableName;
    /**
     * 平台Id
     */
    private String platformId;
    /**
     * 平台name
     */
    private String platformName;
    /**
     * 运行环境Id
     */
    private String runtimeEnvironmentId;
    /**
     * 运行环境name
     */
    private String runtimeEnvironmentName;
    /**
     * 应用模块id
     */
    private String applicationModuleId;
    /**
     * 应用模块name
     */
    private String applicationModuleName;
    /**
     * 应用模块
     */
    private ApplicationModule applicationModule;
    /**
     * 变量代码
     */
    private String code;
    /**
     * 变量名称
     */
    private String name;
    /**
     * 变量配置值
     */
    private String configValue;
    /**
     * 是加密的
     */
    private boolean encrypted;

    /**
     * 默认构造函数
     */
    public EnvVarConfig() {
    }

    public String getEnvironmentVariableName() {
        return environmentVariableName;
    }

    public void setEnvironmentVariableName(String environmentVariableName) {
        this.environmentVariableName = environmentVariableName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getRuntimeEnvironmentName() {
        return runtimeEnvironmentName;
    }

    public void setRuntimeEnvironmentName(String runtimeEnvironmentName) {
        this.runtimeEnvironmentName = runtimeEnvironmentName;
    }

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
    }

    public String getEnvConfigId() {
        return envConfigId;
    }

    public void setEnvConfigId(String envConfigId) {
        this.envConfigId = envConfigId;
    }

    public String getApplicationModuleId() {
        return applicationModuleId;
    }

    public void setApplicationModuleId(String applicationModuleId) {
        this.applicationModuleId = applicationModuleId;
    }

    public String getApplicationModuleName() {
        return applicationModuleName;
    }

    public void setApplicationModuleName(String applicationModuleName) {
        this.applicationModuleName = applicationModuleName;
    }

    /**
     * 通过配置值构造
     *
     * @param varConfig 变量配置值
     */
    public EnvVarConfig(EnvironmentVarConfig varConfig) {
        EnvironmentVariable environmentVariable = varConfig.getEnvironmentVariable();
        environmentVariableId = environmentVariable.getId();
        platformId = environmentVariable.getPlatformId();
        runtimeEnvironmentId = varConfig.getRuntimeEnvironmentId();
        applicationModule = environmentVariable.getApplicationModule();
        code = environmentVariable.getCode();
        name = environmentVariable.getName();
        configValue = varConfig.getConfigValue();
        encrypted = varConfig.getEncrypted();
        //环境变量
        environmentVariableName=varConfig.getEnvironmentVariable().getName();
        runtimeEnvironmentName = varConfig.getRuntimeEnvironment().getName();
        if(Objects.nonNull(applicationModule)){
            applicationModuleName =  applicationModule.getName();
            applicationModuleId =  applicationModule.getId();
        }
        platformName = environmentVariable.getPlatform().getName();
    }

    public String getEnvironmentVariableId() {
        return environmentVariableId;
    }

    public void setEnvironmentVariableId(String environmentVariableId) {
        this.environmentVariableId = environmentVariableId;
    }

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

    public ApplicationModule getApplicationModule() {
        return applicationModule;
    }

    public void setApplicationModule(ApplicationModule applicationModule) {
        this.applicationModule = applicationModule;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
