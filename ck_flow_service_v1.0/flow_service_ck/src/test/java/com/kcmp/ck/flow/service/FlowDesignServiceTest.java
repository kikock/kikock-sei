package com.kcmp.ck.flow.service;

import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.ResponseData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowDesignServiceTest extends BaseContextTestCase {

    @Autowired
    private FlowDesignService service;

    @Test
    public void getEntity() {
        String id = "98307F87-5150-11EA-BBE4-0242C0A84421";
        Integer versionCode = -1;
        String businessModelCode = "com.kcmp.flow.entity.DefaultBusinessModel";
        String businessId = "2AA0E332-5A03-11EA-A372-0242C0A84421";
        ResponseData res = service.getEntity(id,versionCode,businessModelCode,businessId);
        System.out.println(JsonUtils.toJson(res));
    }
}
