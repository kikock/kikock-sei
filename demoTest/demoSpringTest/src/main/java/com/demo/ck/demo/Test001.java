package com.demo.ck.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author
 * @title: Test001
 * @description:
 * @date 2019/9/1921:13
 */
public class Test001 {
    //参数1 连接地址
    private static final String ADDRES = "192.168.66.88:2181";
    // 参数2 zk超时时间
    private static final int TIMEOUT = 5000;
    // 计数器
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException, NoSuchAlgorithmException {
        // zk核心节点+事件通知
        // 节点路径和节点value
        /**
         * 参数1 连接地址
         * 参数2 zk超时事件
         * 参数3 事件通知
         */
        // 1. 创建zk连接
        ZooKeeper zooKeeper = new ZooKeeper(ADDRES, TIMEOUT, new Watcher() {
            // 获取该连接是否成功
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state = watchedEvent.getState();
                if (state == Event.KeeperState.SyncConnected) {
                    System.out.println("zk连接成功");
                    // 计数器减去1
                    countDownLatch.countDown();
                }
            }
        });
        // 计数器结果必须是为0的情况才继续执行
        System.out.println("zk正在连接等待...");
        countDownLatch.await();
        System.out.println("开始创建我们的节点");
        // 2.创建账号权限 admin可以实现读写操作
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);

        // 3.创建权限guest 只允许做读操作
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));
        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
        // 4.添加该账号
        ArrayList<ACL> aces = new ArrayList<ACL>();
        aces.add(acl1);
        aces.add(acl2);


        // 2.创建我们的节点
        /**
         * 参数1 路径名称
         * 参数2 节点value
         * 参数3 节点权限acl
         * 参数4  节点类型 临时和持久
         */
        String s = zooKeeper.create("/meite", "meite_mayikt".getBytes(), aces,
                CreateMode.PERSISTENT);
        System.out.println(s);
        zooKeeper.close();
        /**
         * zk节点分为四种类型
         * 1.临时节点 会话关闭话 就自动消失
         * 2.持久节点 永久持久话存放到硬盘
         * 3.临时有序号节点
         * 4.持久有序号节点
         */
    }
}
