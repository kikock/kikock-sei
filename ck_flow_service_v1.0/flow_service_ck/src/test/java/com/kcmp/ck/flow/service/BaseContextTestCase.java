package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.service.MonitorService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.ResponseData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kikock
 * Spring的支持依赖注入的JUnit4 集成测试基类
 * @author kikock
 * @email kikock@qq.com
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseContextTestCase extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MonitorService monitorService;

    @Before
    public void setUp() {
        LogUtil.debug(ContextUtil.mockUser().toString());
    }

    @Test
    public void health(){
        ResponseData result = monitorService.health();
        System.out.println("monitor health:" + JsonUtils.toJson(result));
    }
}
