package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.vo.DefaultStartParam;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowIntegrateServiceTest extends BaseContextTestCase{

    @Autowired
    private FlowIntegrateService service;

    @Test
    public void startDefaultFlow() {
        String modelCode = "com.kcmp.fsop.soms.entity.ShareOrder";
        String entityId = "18CDBA22-76B3-11E9-A8D8-0242C0A84410";
        DefaultStartParam startParam = new DefaultStartParam(modelCode, entityId);
        OperateResult result =service.startDefaultFlow(startParam);
        System.out.println(JsonUtils.toJson(result));
        Assert.assertTrue(result.successful());
    }
}
