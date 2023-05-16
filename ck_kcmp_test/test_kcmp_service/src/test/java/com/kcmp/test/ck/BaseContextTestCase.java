package com.kcmp.test.ck;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.test.ck.entity.MyTest;
import com.kcmp.test.ck.service.MyTestService;
import com.kcmp.ck.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseContextTestCase extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MyTestService service;
    @Before
    public void setUp() {
        LogUtil.debug(ContextUtil.mockUser().toString());
    }

    @Test
    public void MyTest(){
        MyTest t = service.findByCode("test");
        System.out.println(JsonUtils.toJson(t));
    }

}
