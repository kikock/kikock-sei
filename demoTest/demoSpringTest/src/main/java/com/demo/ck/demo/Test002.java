package com.demo.ck.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

/**
 * @author
 * @title: Test001
 * @description:
 * @date 2019/9/1921:13
 */
public class Test002 {
    //参数1 连接地址
    private static final String ADDRES = "192.168.66.88:2181";
    // 参数2 zk超时时间
    private static final int TIMEOUT = 5000;
    // 计数器
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException, NoSuchAlgorithmException {
        // 1. 创建zk连接
        ZooKeeper zooKeeper = new ZooKeeper(ADDRES, TIMEOUT, new Watcher() {
            // 获取该连接是否成功
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state = watchedEvent.getState();
                if (state == Event.KeeperState.SyncConnected) {

                    // 计数器减去1
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        // 2.设置zk连接账号
        zooKeeper.addAuthInfo("digest", "guest:guest123".getBytes());

        // 获取节点的内容
        byte[] bytes = zooKeeper.getData("/meite", null, new Stat());
        System.out.println(new String(bytes));


    }
}
