package com.kcmp.ck.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kikock
 * zookeeper工具类
 * @email kikock@qq.com
 **/
public class ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkClient.class);

    public static final String PATH_SEPARATOR = "/";
    public static final String SERVICE = "services";
    public static final String SERVICE_ROOT = PATH_SEPARATOR + SERVICE;

    /**
     * 客户端
     */
    private CuratorFramework client;
    /**
     * Zookeeper服务器地址
     */
    private String zookeeperServer;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * session超时时间
     */
    private int sessionTimeoutMs = 60000;
    /**
     * 连接超时时间
     */
    private int connectionTimeoutMs = 10000;
    /**
     * 基本重试时间差
     */
    private int baseSleepTimeMs = 1000;
    /**
     * 最大重试次数
     */
    private int maxRetries = 20;

    public ZkClient(String zookeeperServer, String namespace) {
        this.zookeeperServer = zookeeperServer;
        this.namespace = namespace;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * 初始创建客户端
     */
    public void init() {
        //自定义重试机制
        //基本重试间隔时间，重试次数(每次重试时间加长)
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder().namespace(namespace)
                .connectString(zookeeperServer).retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).build();
        client.start();
    }

    /**
     * 停用
     */
    public void stop() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * @return 返回zk客户端
     */
    public CuratorFramework getClient() {
        return client;
    }

    /**
     * 获取指定路径下的数据
     *
     * @param path 路径
     * @return 返回数据
     * @throws Exception
     */
    public String getData(String path) throws Exception {
        byte[] b = client.getData().forPath(path);
        return new String(b, "UTF-8");
    }

    public void nodeListen(String path, NodeCacheListener listener) throws Exception {
        final NodeCache cache = new NodeCache(client, path);
        cache.start();
        cache.getListenable().addListener(listener);
    }

    /**
     * 服务注册
     * CreateMode四种模式：
     * 1.PERSISTENT：持久化目录节点，存储的数据不会丢失
     * 2.PERSISTENT_SEQUENTIAL：顺序自动编号的持久化目录节点，存储的数据不会丢失，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名。
     * 3.EPHEMERAL：临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除。
     * 4.EPHEMERAL_SEQUENTIAL：临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名
     *
     * @param serviceName    服务名
     * @param serviceAddress 服务地址
     */
    public void register(String serviceName, String serviceAddress) {
        try {
            String serviceInstance = PATH_SEPARATOR + serviceName + "#" + serviceAddress + "#";
            //EPHEMERAL_SEQUENTIAL模式：服务关闭的时候session超时，zk节点会自动删除，同时自增id可以实现锁和负载均衡
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(SERVICE_ROOT + serviceInstance);
        } catch (Exception e) {
            logger.error("注册出错", e);
        }
    }

    /**
     * 按路径获取子节点
     * @param path zk路径
     * @return
     */
    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<>();
        try {
            childrenList = client.getChildren().forPath(path);
        } catch (Exception e) {
            logger.error("获取子节点出错", e);
        }
        return childrenList;
    }

    public int getChildrenCount(String path) {
        return getChildren(path).size();
    }

    public List<String> getInstances() {
        return getChildren(SERVICE_ROOT);
    }

    public int getInstancesCount() {
        return getInstances().size();
    }
}
