package com.kcmp.ck.flow.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowSolidifyExecutorServiceTest extends BaseContextTestCase{

    @Autowired
    private FlowSolidifyExecutorService service;


    @Test
    public void getExecuteInfoByBusinessId() {
        String businessId ="04483B02-1F18-11EA-91AC-0242C0A84503";
        service.selfMotionExecuteTask(businessId);
    }
}
