package com.kcmp.ck.center.service;

import com.google.common.collect.Collections2;
import com.kcmp.ck.center.api.EnvironmentVariableService;
import com.kcmp.ck.center.dao.EnvironmentVarConfigDao;
import com.kcmp.ck.center.dao.EnvironmentVariableDao;
import com.kcmp.ck.center.dao.RuntimeEnvironmentDao;
import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import com.kcmp.ck.config.entity.EnvironmentVariable;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.EnvVarConfig;
import com.kcmp.ck.config.entity.dto.EnvVarConfigSearch;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kikock
 * 环境变量服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class EnvironmentVariableServiceImpl implements EnvironmentVariableService {

    @Autowired
    private EnvironmentVariableDao dao;

    @Autowired
    private EnvironmentVarConfigDao environmentVarConfigDao;

    @Autowired
    private RuntimeEnvironmentDao runtimeEnvironmentDao;

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 环境变量
     */
    @Override
    public EnvironmentVariable findOne(String id) {
        Optional<EnvironmentVariable> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存环境变量配置
     *
     * @param envVarConfig 环境变量配置
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult save(EnvVarConfig envVarConfig) {
        OperateResult result = new OperateResult("保存环境变量配置成功！");
        if (Objects.isNull(envVarConfig)) {
            return result;
        }
        if (Objects.isNull(envVarConfig.getConfigValue())) {
            envVarConfig.setConfigValue("");
        }
        //获取环境变量，判断是否已经存在
        String platformId = envVarConfig.getPlatformId();
        String code = envVarConfig.getCode();
        EnvironmentVariable environmentVariable = dao.findFirstByPlatformIdAndCode(platformId, code);
        //检查平台环境变量的代码
        if (org.apache.commons.lang3.StringUtils.isBlank(envVarConfig.getEnvironmentVariableId())) {
            if (Objects.nonNull(environmentVariable)) {
                result.setOperateStatusEnum(OperateStatusEnum.ERROR);
                result.setMessage(String.format("[%s]已经存在！", code));
                return result;
            }
        }
        String runtimeEnvironmentId = envVarConfig.getRuntimeEnvironmentId();
        String applicationModuleId = null;
        if (Objects.nonNull(envVarConfig.getApplicationModule())) {
            applicationModuleId = envVarConfig.getApplicationModule().getId();
        }
        //如果不存在则创建
        if (Objects.isNull(environmentVariable)) {
            environmentVariable = new EnvironmentVariable();
            environmentVariable.setPlatformId(platformId);
            environmentVariable.setApplicationModuleId(applicationModuleId);
            environmentVariable.setCode(code);
        }
        environmentVariable.setName(envVarConfig.getName());
        //保存环境变量
        dao.save(environmentVariable);
        //保存所有运行环境的环境变量的配置值
        List<RuntimeEnvironment> runtimeEnvironments = runtimeEnvironmentDao.findAll();
        for (RuntimeEnvironment runtimeEnvironment : runtimeEnvironments
        ) {
            //获取变量配置值，判断是否存在
            EnvironmentVarConfig environmentVarConfig = environmentVarConfigDao.findFirstByRuntimeEnvironmentIdAndEnvironmentVariableId(runtimeEnvironment.getId(), environmentVariable.getId());
            //如果不存在则创建
            if (Objects.isNull(environmentVarConfig)) {
                environmentVarConfig = new EnvironmentVarConfig();
                environmentVarConfig.setEnvironmentVariableId(environmentVariable.getId());
                environmentVarConfig.setRuntimeEnvironmentId(runtimeEnvironment.getId());
            }
            if (runtimeEnvironmentId.equals(runtimeEnvironment.getId())) {
                //设置配置值
                environmentVarConfig.setConfigValue(envVarConfig.getConfigValue());
                environmentVarConfig.setEncrypted(envVarConfig.isEncrypted());
            }
            //保存变量配置值
            environmentVarConfigDao.save(environmentVarConfig);
        }
        result.setMessage(String.format("环境变量[%s-%s]保存成功！", code, environmentVariable.getName()));
        return result;
    }

    /**
     * 删除环境变量配置
     *
     * @param environmentVariableId 环境变量Id
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult delete(String environmentVariableId) {
        OperateResult result = new OperateResult("环境变量配置删除成功！");
        //先获取平台全局环境变量
        EnvironmentVariable environmentVariable = findOne(environmentVariableId);
        if (Objects.isNull(environmentVariable)) {
            result.setOperateStatusEnum(OperateStatusEnum.ERROR);
            result.setMessage("要删除的环境变量不存在！");
            return result;
        }
        //执行删除所有配置值
        environmentVarConfigDao.deleteAllByEnvironmentVariableId(environmentVariable.getId());
        //执行删除环境变量
        dao.delete(environmentVariable);
        result.setMessage(String.format("环境变量[%s-%s]删除成功！", environmentVariable.getCode(), environmentVariable.getName()));
        return result;
    }

    /**
     * 通过平台和环境获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @return 环境变量配置清单
     */
    @Override
    public List<EnvVarConfig> findByPlatformAndEnv(String platformId, String runtimeEnvironmentId) {
        //获取配置值清单
        List<EnvironmentVarConfig> varConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentId(platformId, runtimeEnvironmentId);
        if (Objects.isNull(varConfigs) || varConfigs.isEmpty()) {
            return Collections.emptyList();
        }
        //构造DTO
        return varConfigs.stream().map(EnvVarConfig :: new).collect(Collectors.toList());
    }

    /**
     * 通过查询参数获取环境变量配置清单
     *
     * @param search 查询参数
     * @return 环境变量配置清单
     */
    @Override
    public List<EnvVarConfig> findBySearch(EnvVarConfigSearch search) {
        //获取配置值清单
        List<EnvironmentVarConfig> varConfigs;
        //平台id
        String platformId = search.getPlatformId();
        //运行环境id
        String runtimeEnvId = search.getRuntimeEnvironmentId();
        //应用id
        String applicationModuleId = search.getApplicationModuleId();
        //判断是否为平台级配置
        if (search.isPlatformParam()) {
            varConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleIdIsNull(platformId, runtimeEnvId);
        } else if (StringUtils.isEmpty(applicationModuleId)) {
            varConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleIdIsNotNull(platformId, runtimeEnvId);
        } else {
            varConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentIdAndApplicationModuleId(platformId, runtimeEnvId, applicationModuleId);
        }
        if (Objects.isNull(varConfigs) || varConfigs.isEmpty()) {
            return Collections.emptyList();
        }
        //构造DTO
        return varConfigs.stream().map(EnvVarConfig :: new).collect(Collectors.toList());
    }

    /**
     * 为新的运行环境同步保存环境变量配置
     *
     * @param runtimeEnvironment 环境变量
     */
    void saveAllForEnvironment(RuntimeEnvironment runtimeEnvironment) {
        if (Objects.isNull(runtimeEnvironment) || StringUtils.isEmpty(runtimeEnvironment.getId())) {
            return;
        }
        //获取所有环境变量
        List<EnvironmentVariable> environmentVariables = dao.findAll();
        List<EnvironmentVarConfig> environmentVarConfigs = new ArrayList<>();
        environmentVariables.forEach((v) -> {
            EnvironmentVarConfig varConfig = new EnvironmentVarConfig();
            varConfig.setRuntimeEnvironmentId(runtimeEnvironment.getId());
            varConfig.setEnvironmentVariableId(v.getId());
            environmentVarConfigs.add(varConfig);
        });
        //全部保存
        environmentVarConfigDao.saveAll(environmentVarConfigs);
    }

    /**
     * 删除运行环境时，级联删除环境变量配置值
     *
     * @param runtimeEnvironmentId 环境变量Id
     */
    void deleteAllForEnvironment(String runtimeEnvironmentId) {
        environmentVarConfigDao.deleteAllByRuntimeEnvironmentId(runtimeEnvironmentId);
    }
}
