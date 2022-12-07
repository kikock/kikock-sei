package com.demo.ck.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author
 * @title: Test004
 * @description:
 * @date 2019/9/1922:08
 */
public class Test003 {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.66.88:2181", 50000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Watcher.Event.KeeperState state = watchedEvent.getState();
                // 如果当前连接成功,则开始放心
                if (state == Event.KeeperState.SyncConnected) {
                    System.out.println("zk连接成功~~");
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        String path = "/mayikt-service";
        // 获取该节点下子集
        List<String> children = zooKeeper.getChildren(path, null, new Stat());
        for (int i = 0; i < children.size(); i++) {
            String pathChildren = path + "/" + children.get(i);
            byte[] data = zooKeeper.getData(pathChildren, null, new Stat());
            System.out.println("服务接口地址:" + new String(data));
        }

    }
}
