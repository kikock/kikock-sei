package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowDefinationServiceTest extends BaseContextTestCase{
    @Autowired
    private FlowDefinationService service;

    @Test
    public void getFlowDefVersion(){
        String id = "1B5E5E2F-035A-11E9-A604-0242C0A84402";
        Integer versionCode = -1;
        FlowDefVersion defVersion = service.getFlowDefVersion(id, versionCode, null, null);
        Assert.assertNotNull(defVersion);
        System.out.println(JsonUtils.toJson(defVersion));
    }
}
