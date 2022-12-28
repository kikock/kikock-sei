package com.kcmp.ck.center;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import java.util.List;

/**
 * Created by kikock
 * zk客户端测试
 * @author kikock
 * @email kikock@qq.com
 **/
public class CuratorWatcherTest {
    /**
     * Zookeeper info
     */
    private static final String ZK_ADDRESS = "192.168.66.88:2181";
    private static final String ZK_PATH = "/test";
    private static final String ZK_ROOT = "/";
    private static final String ZK_NAME_SPACE = "com.center.config";

    /**
     * 构建一个zk客户端
     * @return zk客户端
     */
    private CuratorFramework getClient() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = builder
                .connectString(ZK_ADDRESS)
                .sessionTimeoutMs(15000)
                .connectionTimeoutMs(15000)
                .canBeReadOnly(true)
                .retryPolicy(new ExponentialBackoffRetry(5000,10))
                .namespace(ZK_NAME_SPACE)
                .defaultData(null)
                .build();
        client.start();
        System.out.println("zk client start successfully!");
        return client;
    }

    /**
     * 监听测试
     */
    @Test
    public void watcherTest() throws Exception{
        CuratorFramework client = getClient();
        // 1.Node watcher
        NodeCache watcher = new NodeCache(client, ZK_PATH);
        watcher.getListenable().addListener(() -> {
            ChildData data = watcher.getCurrentData();
            if (data == null) {
                System.out.println("No data in event[nodeChanged]");
            } else {
                System.out.println("Receive event: "
                        + "type=[nodeChanged]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        watcher.start();
        System.out.println("Register zk watcher successfully!");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getNode() throws Exception {
        byte[] data = getClient().getData().forPath(ZK_PATH);
        System.out.println(new String(data));
    }



    public static void main(String[] args) throws Exception{

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = CuratorFrameworkFactory.newClient("kikock.tk:12181", new RetryNTimes(10, 5000));
        client.start();// 连接
        // // 获取子节点，顺便监控子节点
        // List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
        //     public void process(WatchedEvent event) throws Exception
        //     {
        //         System.out.println("监控： " + event);
        //     }
        // }).forPath("/");
        // System.out.println("children = "+children);

        // 创建节点
        String result = client.create().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/kikock.tk", "Data".getBytes());

        System.out.println("result = "+result);
        // 设置节点数据
        // client.setData().forPath("/test1", "1112".getBytes());
        client.setData().forPath("/kikock.tk", "2224".getBytes());
        // 删除节点
        //System.out.println(client.checkExists().forPath("/test"));
        /*client.delete().withVersion(-1).forPath("/test");
        System.out.println(client.checkExists().forPath("/test"));*/
        client.close();
        System.out.println("OK！");
    }

}
