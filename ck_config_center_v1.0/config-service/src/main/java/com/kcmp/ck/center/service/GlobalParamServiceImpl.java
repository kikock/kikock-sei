package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.GlobalParamService;
import com.kcmp.ck.center.dao.ApplicationServiceDao;
import com.kcmp.ck.center.dao.EnvironmentVarConfigDao;
import com.kcmp.ck.center.dao.GlobalParamConfigDao;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.enums.ParamDataTypeEnum;
import com.kcmp.ck.config.entity.util.AesUtil;
import com.kcmp.ck.config.entity.vo.ParamConfigKey;
import com.kcmp.ck.config.util.JsonUtils;
import com.kcmp.ck.config.util.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kikock
 * 全局参数服务接口
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class GlobalParamServiceImpl implements GlobalParamService {

    @Autowired
    private ApplicationServiceDao applicationServiceDao;

    @Autowired
    private EnvironmentVarConfigDao environmentVarConfigDao;

    @Autowired
    private GlobalParamConfigDao globalParamConfigDao;

    @Autowired
    private ZkClient zkClient;

    /**
     * 环境变量的正则表达式
     */
    private static final Pattern VAR_PATTERN = Pattern.compile("\\{(.*?)\\}");
    /**
     * 配置中心的zookeeper命名空间
     */
    private static final String ZK_NAME_SPACE = "com.center.config";
    private static final String SELF_APP_SERVICE = "SELF_APP_SERVICE";
    private static final String SELF_APP = "SELF_APP";
    private static final String SELF_ENV = "SELF_ENV";

    /**
     * 获取应用服务的全局参数
     *
     * @param appId 应用服务Id
     * @return 全局参数
     */
    @Override
    public Map<String, Map<String, String>> getParams(String appId) {
        //获取应用服务
        ApplicationService applicationService = applicationServiceDao.findFirstByAppId(appId);
        if (Objects.isNull(applicationService)) {
            return new HashMap<>(0);
        }
        //构造键值对输出
        Map<String, Map<String, String>> params = new HashMap<>();
        //设置本应用模块的键和值
        Map<String, String> selfAppEnv = new HashMap<>();
        selfAppEnv.put(SELF_APP, applicationService.getApplicationModule().getCode());
        selfAppEnv.put(SELF_ENV, applicationService.getRuntimeEnvironment().getCode());
        params.put(SELF_APP_SERVICE, selfAppEnv);
        RuntimeEnvironment runtimeEnvironment = applicationService.getRuntimeEnvironment();
        ApplicationModule applicationModule = applicationService.getApplicationModule();
        List<EnvironmentVarConfig> varConfigs = new ArrayList<>();
        // 获取运行环境中平台级应用的环境变量
        List<EnvironmentVarConfig> platVarConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentId(applicationModule.getPlatformId(), runtimeEnvironment.getId());
        varConfigs.addAll(platVarConfigs);
        //对所有环境变量解密
        varConfigs.forEach((v) -> {
            String configValue = v.getConfigValue();
            if (v.getEncrypted()) {
                v.setConfigValue(AesUtil.decrypt(configValue));
            }
        });
        List<GlobalParamConfig> paramConfigs = new ArrayList<>();
        // 获取平台的全局参数
        List<GlobalParamConfig> platParamConfigs = globalParamConfigDao.findAllByPlatformIdAndApplicationModuleId(applicationModule.getPlatformId(), null);
        paramConfigs.addAll(platParamConfigs);
        //获取应用模块的全局配置
        List<GlobalParamConfig> appParmaConfigs = globalParamConfigDao.findAllByPlatformIdAndApplicationModuleId(applicationModule.getPlatformId(), applicationModule.getId());
        paramConfigs.addAll(appParmaConfigs);
        paramConfigs.forEach((GlobalParamConfig c) -> {
            Map<String, String> value = getConfigValue(c, varConfigs, paramConfigs);
            params.put(c.getParamKey(), value);
        });
        return params;
    }

    /**
     * 获取全局参数配置值(全局)
     *
     * @param configKey 全局参数配置键
     * @return 配置值
     */
    @Override
    public Map<String, String> getParamConfigValue(ParamConfigKey configKey) {
        String platformId = configKey.getPlatformId();
        String runtimeEnvironmentId = configKey.getRuntimeEnvironmentId();
        String paramKey = configKey.getParamKey();
        // 获取运行环境中平台级应用的环境变量
        List<EnvironmentVarConfig> platVarConfigs = environmentVarConfigDao.findAllByPlatformIdAndRuntimeEnvironmentId(platformId, runtimeEnvironmentId);
        //对所有环境变量解密
        platVarConfigs.forEach((v) -> {
            String configValue = v.getConfigValue();
            if (v.getEncrypted()) {
                v.setConfigValue(AesUtil.decrypt(configValue));
            }
        });
        // 获取平台的全局参数
        List<GlobalParamConfig> platParamConfigs = globalParamConfigDao.findAllByPlatformIdAndApplicationModuleId(platformId, null);
        // 获取全局参数配置
        Optional<GlobalParamConfig> paramConfigResult = platParamConfigs.stream().filter((c) -> c.getParamKey().equals(paramKey)).findFirst();
        if (!paramConfigResult.isPresent()) {
            return new HashMap<>();
        }
        GlobalParamConfig paramConfig = paramConfigResult.get();
        return getConfigValue(paramConfig, platVarConfigs, platParamConfigs);
    }

    /**
     * 通过参数配置获得参数配置值MAP
     *
     * @param paramConfig  参数配置
     * @param varConfigs   环境变量配置
     * @param paramConfigs 所有参数配置清单
     * @return 参数配置值
     */
    private Map<String, String> getConfigValue(GlobalParamConfig paramConfig, List<EnvironmentVarConfig> varConfigs, List<GlobalParamConfig> paramConfigs) {
        String key = paramConfig.getParamKey();
        ParamDataTypeEnum dataTypeEnum = paramConfig.getParamDataType();
        //处理键值对参数
        if (dataTypeEnum == ParamDataTypeEnum.KEY_VALUES) {
            HashMap<String, String> resultValues = new HashMap<>();
            HashMap<String, String> values = JsonUtils.fromJson(paramConfig.getParamValue(), HashMap.class);
            //替换全局配置的环境变量
            values.forEach((k, v) -> {
                String newValue = environmentFormat(v, varConfigs);
                // 如果值为空或空字符串时不加载配置项；
                if (StringUtils.isNotBlank(newValue)) {
                    resultValues.put(k, newValue);
                }
            });
            return resultValues;
        }
        //处理字符串参数
        if (dataTypeEnum == ParamDataTypeEnum.STRING) {
            Map<String, String> value = new HashMap<>(1);
            //替换全局配置的环境变量
            value.put(key, environmentFormat(paramConfig.getParamValue(), varConfigs));
            return value;
        }
        //处理引用参数
        if (dataTypeEnum == ParamDataTypeEnum.REFERENCE) {
            String configKey = paramConfig.getParamValue();
            //获取参数配置
            Predicate<GlobalParamConfig> predicate = c -> c.getParamKey().equals(configKey);
            if (paramConfigs.stream().anyMatch(predicate)) {
                GlobalParamConfig keyConfig = paramConfigs.stream().filter(predicate).findFirst().get();
                return getConfigValue(keyConfig, varConfigs, paramConfigs);
            }
        }
        //默认处理
        Map<String, String> value = new HashMap<>(1);
        value.put(key, paramConfig.getParamValue());
        return value;
    }

    /**
     * 格式化字符串 字符串中使用{key}表示占位符 用环境配置替换
     *
     * @param sourStr    需要匹配的字符串
     * @param varConfigs 环境变量清单
     * @return 替换后的字符串
     */
    private static String environmentFormat(String sourStr, List<EnvironmentVarConfig> varConfigs) {
        if (StringUtils.isBlank(sourStr)) {
            return sourStr;
        }
        String tagerStr = sourStr;
        if (varConfigs == null || varConfigs.isEmpty()) {
            return tagerStr;
        }
        Matcher matcher = VAR_PATTERN.matcher(tagerStr);
        while (matcher.find()) {
            String key = matcher.group();
            String keyclone = key.substring(1, key.length() - 1).trim();
            Predicate<EnvironmentVarConfig> predicate = v -> keyclone.equals(v.getEnvironmentVariable().getCode());
            if (varConfigs.stream().anyMatch(predicate)) {
                EnvironmentVarConfig value = varConfigs.stream().filter(predicate).findFirst().get();
                tagerStr = tagerStr.replace(key, value.getConfigValue());
            }
        }
        return tagerStr;
    }

    //    /**
    //     * 构建一个zookeeper客户端
    //     *
    //     * @return zookeeper客户端
    //     */
    //    private CuratorFramework getZookeeperClient() {
    //        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
    //        CuratorFramework client = builder
    //                .connectString(zookeeperConnectString)
    //                .sessionTimeoutMs(5000)
    //                .connectionTimeoutMs(5000)
    //                .canBeReadOnly(false)
    //                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
    //                .namespace(ZK_NAME_SPACE)
    //                .defaultData(null)
    //                .build();
    //        client.start();
    //        return client;
    //    }

    /**
     * 删除zookeeper服务节点
     *
     * @param appId 应用服务Id
     */
    void deleteZookeeperNode(String appId) {
        String path = String.format("/%s", appId);
        CuratorFramework client = zkClient.getClient();
        try {
            Stat stat = client.checkExists().forPath(path);
            if (Objects.isNull(stat)) {
                //删除节点
                client.delete().forPath(path);
            }
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            //        } finally {
            //            if (Objects.nonNull(client)) {
            //                client.close();
            //            }
        }
    }

    /**
     * 保存应用服务的全局参数配置到zookeeper服务节点
     *
     * @param appId 应用服务Id
     */
    void saveZookeeperNode(String appId) throws Exception {
        //获取应用服务的配置
        Map<String, Map<String, String>> params = getParams(appId);
        if (Objects.isNull(params) || params.isEmpty()) {
            return;
        }
        //序列化
        String paramsJson = JsonUtils.toJson(params);
        String path = String.format("/%s", appId);
        //检查节点是否存在
        CuratorFramework client = zkClient.getClient();
        try {
            Stat stat = client.checkExists().forPath(path);
            if (Objects.isNull(stat)) {
                //创建节点
                client.create().creatingParentsIfNeeded().forPath(path, paramsJson.getBytes());
            } else {
                //更新节点
                client.setData().forPath(path, paramsJson.getBytes());
            }
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
            //        } finally {
            //            if (Objects.nonNull(client)) {
            //                client.close();
            //            }
        }
    }

    /**
     * 从Zookeeper服务获取应用服务的全局参数配置
     *
     * @param appId 应用服务Id
     * @return 全局参数配置
     */
    HashMap getZookeeperNode(String appId) {
        //应用服务的配置节点
        String path = String.format("/%s", appId);
        CuratorFramework client = zkClient.getClient();
        try {
            byte[] nodeData = client.getData().forPath(path);
            if (Objects.isNull(nodeData)) {
                return new HashMap<>(0);
            }
            String jsonData = new String(nodeData);
            return JsonUtils.fromJson(jsonData, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            //        } finally {
            //            if (Objects.nonNull(client)) {
            //                client.close();
            //            }
        }
        return new HashMap(0);
    }
}
