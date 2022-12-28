package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.ck.context.BaseApplicationContext;
import com.kcmp.ck.context.ConfigConstants;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.util.ZkClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by kikock
 * 全局配置处理
 * @email kikock@qq.com
 **/
public abstract class KCMPEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered, DisposableBean {
    private String appId = null;
    private ZkClient zkClient = null;

    protected static Logger log = LoggerFactory.getLogger(ZkClient.class);;
    protected Set<String> excludeConfigClass = new HashSet<>();
    /**
     * 是否是本地AppId从配置中心加载配置
     */
    protected static volatile boolean isLocalConfig = false;

    /**
     * 全局配置信息处理
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //当依赖有spring cloud时，跳过bootstrap配置
        if (environment.getPropertySources().contains("bootstrap")) {
            return;
        }
        // 此时日志还未初始化，故使用System.out.println打印日志
        log.info("开始加载全局配置参数...");
        System.out.println("开始加载全局配置参数...");

        Properties properties = new Properties();
        try {
            //构造KCMP默认的全局配置
            PropertiesPropertySource propertiesPropertySource = setKCMPDefaultConfig(environment, properties);

            Set<String> localGRPCServers = new HashSet<>();

            Object obj;
            MutablePropertySources sources = environment.getPropertySources();
            for (PropertySource source : sources) {
                obj = source.getSource();
                if (obj instanceof Map) {
                    Map map = (Map) obj;
                    if (map.containsKey("spring.autoconfigure.exclude")) {
                        excludeConfigClass.add(map.remove("spring.autoconfigure.exclude").toString());
                    }

                    if (StringUtils.startsWithIgnoreCase(source.getName(), "applicationConfig:")) {
                        //只有本地环境，才读取本地配置的服务
                        if (isLocalConfig) {
                            //本地RPC服务处理
                            int i = 0;

                            Map<String, String> data = new HashMap<>(map);
                            for (Object key : data.keySet()) {
                                if (StringUtils.startsWith(key.toString(), "spring.grpc.remote-servers[")
                                        && StringUtils.endsWith(key.toString(), "].server")) {
                                    String idx = String.valueOf(key).replace("spring.grpc.remote-servers[", "").replace("].server", "");
                                    Object address = map.get("spring.grpc.remote-servers[" + idx + "].address");
                                    if (address == null) {
                                        Object host = map.get("spring.grpc.remote-servers[" + idx + "].host");
                                        Object port = map.get("spring.grpc.remote-servers[" + idx + "].port");
                                        if (host != null && port != null) {
                                            address = host + ":" + port;

                                            map.remove("spring.grpc.remote-servers[" + idx + "].host");
                                            map.remove("spring.grpc.remote-servers[" + idx + "].port");
                                        }
                                    } else {
                                        map.remove("spring.grpc.remote-servers[" + idx + "].address");
                                    }
                                    if (address != null) {
                                        String serverName = String.valueOf(map.get(key));
                                        properties.setProperty("spring.grpc.remote-servers[" + i + "].server", serverName);
                                        properties.setProperty("spring.grpc.remote-servers[" + i + "].address", String.valueOf(address));
                                        i++;

                                        localGRPCServers.add(serverName);

                                        map.remove(key);
                                    }
                                }
                            }
                            data.clear();
                        }
                    }
                }
            }

            if (excludeConfigClass.size() > 0) {
                properties.setProperty("spring.autoconfigure.exclude", StringUtils.join(excludeConfigClass, ","));
            }

            if (zkClient != null) {
                //获取注册服务器列表
                List<String> instances = zkClient.getInstances();
                if (CollectionUtils.isNotEmpty(instances)) {
                    int i = localGRPCServers.size();
                    for (String instance : instances) {
                        String[] arr = instance.split("[#]");
                        if (arr.length == 3) {
                            if (!localGRPCServers.contains(arr[0])) {
                                properties.setProperty("spring.grpc.remote-servers[" + i + "].server", arr[0]);
                                properties.setProperty("spring.grpc.remote-servers[" + i + "].address", arr[1]);
                                i++;
                            }
                        }
                    }
                }
            }

        /*
            检查是否是本地开发环境
            是：本地配置文件优先，即当配置中心和本地配置文件存在相同key时，使用本地该key的配置值
            否：配置中心配置文件优先，即当配置中心和本地配置文件存在相同key时，使用配置中心该key的配置值
         */
            if (isLocalConfig) {
                environment.getPropertySources().addLast(propertiesPropertySource);
            } else {
                environment.getPropertySources().addFirst(propertiesPropertySource);
            }
            BaseApplicationContext baseContext = new BaseApplicationContext();
            baseContext.setEnvironment(environment);

            log.info("全局配置参数加载完成。");
            System.out.println("全局配置参数加载完成。");
        } catch (Exception e) {
            throw new RuntimeException("初始化配置异常", e);
        } finally {
            if (zkClient != null) {
                zkClient.stop();
            }
        }
    }

    /**
     * 设置KCMP默认设置
     * @param environment 运行容器环境
     * @param properties  配置属性对象
     * @return 返回PropertiesPropertySource
     */
    protected abstract PropertiesPropertySource setKCMPDefaultConfig(ConfigurableEnvironment environment, Properties properties);

    /**
     * 初始化KCMP运行环境配置
     */
    protected void initKCMPEnvironment(ConfigurableEnvironment environment, Properties properties) {
        //是否是加载本地配置文件配置
        boolean isConfigFile = false;
        //系统环境变量
        Map<String, Object> sysEnvMap = environment.getSystemEnvironment();
        //从系统环境变量中获取APP_ID
        appId = (String) sysEnvMap.get(ConfigConstants.ENV_KCMP_APP_ID);
        String zkHost = (String) sysEnvMap.get(ConfigConstants.ENV_KCMP_CONFIG_CENTER);
        //TODO 环境变量读取
        if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(zkHost)) {
            isLocalConfig = false;
        } else if (StringUtils.isNotBlank(appId) && StringUtils.isBlank(zkHost)) {
            zkHost = environment.getProperty(ConfigConstants.ENV_KCMP_CONFIG_CENTER);
            if (StringUtils.isBlank(zkHost)) {
                throw new ExceptionInInitializerError("运行服务器没有配置系统环境变量：" + ConfigConstants.ENV_KCMP_CONFIG_CENTER);
            }
            isLocalConfig = false;
        } else if (StringUtils.isBlank(appId) && StringUtils.isNotBlank(zkHost)) {
            appId = environment.getProperty(ConfigConstants.ENV_KCMP_APP_ID);
            if (StringUtils.isBlank(appId)) {
                throw new ExceptionInInitializerError("运行服务器没有配置系统环境变量：" + ConfigConstants.ENV_KCMP_APP_ID);
            }
            isLocalConfig = false;
        } else {
            appId = environment.getProperty(ConfigConstants.ENV_KCMP_APP_ID);
            if (StringUtils.isBlank(appId)) {
                isConfigFile = true;
                log.warn("运行服务器没有配置系统环境变量：" + ConfigConstants.ENV_KCMP_APP_ID + "！");
            }
            if (!isConfigFile) {
                zkHost = environment.getProperty(ConfigConstants.ENV_KCMP_CONFIG_CENTER);
                if (StringUtils.isBlank(zkHost)) {
                    log.warn("运行服务器没有配置系统环境变量：" + ConfigConstants.ENV_KCMP_CONFIG_CENTER + "！");
                    throw new ExceptionInInitializerError("运行服务器没有配置系统环境变量：" + ConfigConstants.ENV_KCMP_CONFIG_CENTER);
                }
            }
            isLocalConfig = true;
        }
        System.out.println("读取配置中心zookeeper地址:" + ConfigConstants.ENV_KCMP_CONFIG_CENTER + "="+zkHost+",配置应用id"+ConfigConstants.ENV_KCMP_APP_ID+
                "="+appId);
        properties.put("isConfigFile", isConfigFile);

        if (!isConfigFile) {
            //初始化zk客户端
            initZkClient(zkHost);

            //读取zk数据
            Map<String, Map<String, String>> zkMapData = getZookeeperData(appId, zkClient.getClient());
            //格式化数据，以支持springboot
            conversionData(zkMapData, properties);
        }
    }

    /**
     * 初始化zk客户端
     *
     * @param zkHost zk地址
     */
    private void initZkClient(String zkHost) {
        //实例化zk客户端
        zkClient = new ZkClient(zkHost, ConfigConstants.ZK_NAME_SPACE);
        //初始化zk
        zkClient.init();
    }

    /**
     * 格式化数据，以支持springboot
     */
    private Properties conversionData(Map<String, Map<String, String>> zkMapData, Properties properties) {
        if (zkMapData != null && !zkMapData.isEmpty()) {
            String key;
            String subKey;
            Map<String, String> value;
            for (Map.Entry<String, Map<String, String>> entry : zkMapData.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (StringUtils.isNotBlank(key) && key.contains(".")) {
                    key = key.toLowerCase();
                    for (Map.Entry<String, String> entryMap : value.entrySet()) {
                        subKey = entryMap.getKey().toLowerCase();
                        if (StringUtils.isBlank(entryMap.getValue())) {
                            continue;
                        }
                        if (StringUtils.equalsIgnoreCase(key, subKey)) {
                            properties.setProperty(key, entryMap.getValue());
                        } else {
                            properties.setProperty(key + "." + subKey, entryMap.getValue());
                        }
                    }
                } else {
                    properties.put(key, value);
                }
            }
            zkMapData.clear();
        }
        return properties;
    }

    /**
     * 读取zk数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> getZookeeperData(String node, CuratorFramework client) {
        try {
            //初始化或更新应用服务的全局参数
            String jsonData = new String(client.getData().forPath("/" + node), "UTF-8");
            if (StringUtils.isNotBlank(jsonData)) {
                log.info("receive nodeChanged event：" + jsonData);
                System.out.println("配置中心配置参数：" + jsonData);

                Map<String, Map<String, String>> configMap = JsonUtils.fromJson(jsonData, HashMap.class);
                if (Objects.isNull(configMap) || configMap.isEmpty()) {
                    throw new ExceptionInInitializerError("未获取到配置中心数据");
                }
                return configMap;
            } else {
                throw new ExceptionInInitializerError("未获取到配置中心数据");
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError("获取配置中心数据异常" + e.getMessage());
        }
    }

    /**
     * 启动zookeeper监听
     */
    public void configListener(String node, CuratorFramework client) {
        final boolean[] isFirst = {true};
        try {
            // Node watcher 监听nodeChanged事件
            NodeCache watcher = new NodeCache(client, "/" + node);
            watcher.getListenable().addListener(() -> {
                //避免第一次启动的时候加载两次配置
                if (!isFirst[0]) {
                    ChildData nodeData = watcher.getCurrentData();
                    if (Objects.nonNull(nodeData)) {
                        //初始化或更新应用服务的全局参数
                        String data = new String(nodeData.getData(), StandardCharsets.UTF_8);
                        if (StringUtils.isNotBlank(data)) {
                            //输出日志
                            log.info("receive nodeChanged event：" + data);
                            HashMap configMap1 = JsonUtils.fromJson(data, HashMap.class);
                            if (Objects.isNull(configMap1) || configMap1.isEmpty()) {
                                throw new ExceptionInInitializerError("未获取到配置中心数据");
                            }
//                            properties.putAll(configMap1);
                        } else {
                            log.warn("未获取到配置中心数据");
                        }
                    }
                } else {
                    isFirst[0] = false;
                }
            });
            watcher.start();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("获取配置中心数据异常" + e.getMessage());
        }
    }


    protected final void excludeConfigClass(Class<?> clazz) {
        if (null == clazz) {
            return;
        }
        excludeConfigClass.add(clazz.getName());
    }

    @Override
    public void destroy() {
        if (zkClient != null) {
            zkClient.stop();
        }
        zkClient = null;
    }
}
