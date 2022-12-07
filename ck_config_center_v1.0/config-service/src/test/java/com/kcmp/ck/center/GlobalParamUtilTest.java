package com.kcmp.ck.center;

import com.kcmp.ck.center.util.GlobalParamUtil;
import com.kcmp.ck.config.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by kikock
 * 全局参数测试
 * @author kikock
 * @email kikock@qq.com
 **/
public class GlobalParamUtilTest {
    @Test
    public void fetchSystemEnv() throws Exception {
        Map<String, String> envs = GlobalParamUtil.fetchSystemEnv();
        Assert.assertNotNull(envs);
        System.out.println(JsonUtils.toJson(envs));
    }

    @Test
    public void startZookeeperWatcher() throws Exception{
        GlobalParamUtil.startZookeeperWatcher();
        System.out.println("start zookeeper watcher successfully!");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
