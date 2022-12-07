package com.kcmp.ck.center.util;

import com.kcmp.ck.config.util.JsonUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kikock
 * 全局参数工具类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class GlobalParamUtil {
    private static final Logger logger = LoggerFactory.getLogger(GlobalParamUtil.class);

    /**
     * 应用服务AppId
     */
    private final static String kCMP_APP_ID = "kCMP_APP_ID";
    /**
     * 云平台配置中心zookeeper服务地址
     */
    private final static String kCMP_CONFIG_CENTER = "kCMP_CONFIG_CENTER";
    /**
     * 配置中心zookeeper的节点命名空间
     */
    private static final String ZK_NAME_SPACE = "com.center.config";
    /**
     * 运行服务器的系统环境变量配置
     */
    private static Map<String, String> runSystemEnvironments;
    /**
     * 全局参数配置清单
     */
    private static HashMap globalParameters;

    static {
        getRunSystemEnvs();
    }

    /**
     * 获取运行服务器的环境变量
     */
    private static void getRunSystemEnvs() {
        if (Objects.nonNull(runSystemEnvironments)) {
            return;
        }
        runSystemEnvironments = new HashMap<>(2);
        String appId = System.getenv(kCMP_APP_ID);
        if (Objects.isNull(appId)) {
            throw new ExceptionInInitializerError("运行服务器没有配置系统环境变量：" + kCMP_APP_ID + "！");
        }
        runSystemEnvironments.put(kCMP_APP_ID, appId);
        String configCenterUrl = System.getenv(kCMP_CONFIG_CENTER);
        if (Objects.isNull(configCenterUrl)) {
            throw new ExceptionInInitializerError("运行服务器没有配置系统环境变量：" + kCMP_CONFIG_CENTER + "！");
        }
        runSystemEnvironments.put(kCMP_CONFIG_CENTER, configCenterUrl);
    }

    /**
     * 获取运行服务器的系统环境变量
     *
     * @return 系统环境变量
     */
    public static Map<String, String> fetchSystemEnv() {
        return runSystemEnvironments;
    }

    /**
     * 构建一个zk客户端
     *
     * @return zk客户端
     */
    private static CuratorFramework getZookeeperClient() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = builder
                .connectString(runSystemEnvironments.get(kCMP_CONFIG_CENTER))
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .canBeReadOnly(true)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .namespace(ZK_NAME_SPACE)
                .defaultData(null)
                .build();
        client.start();
        return client;
    }

    /**
     * 启动zookeeper监听
     */
   public static void startZookeeperWatcher() {
        //应用服务的配置节点
        String path = String.format("/%s", runSystemEnvironments.get(kCMP_APP_ID));
        try {
            CuratorFramework client = getZookeeperClient();
            // Node watcher 监听nodeChanged事件
            NodeCache watcher = new NodeCache(client, path);
            watcher.getListenable().addListener(() -> {
                ChildData nodeData = watcher.getCurrentData();
                if (Objects.nonNull(nodeData)) {
                    //初始化或更新应用服务的全局参数
                    String jsonData = new String(nodeData.getData());
                    globalParameters = JsonUtils.fromJson(jsonData, HashMap.class);
                    //输出日志
                    logger.info("receive nodeChanged event：");
                    logger.info(jsonData);
                }
            });
            watcher.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //身份证验证

}
