package com.kcmp.ck.flow.util;

import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.service.BaseContextTestCase;
import com.kcmp.ck.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowCommonUtilTest extends BaseContextTestCase {
    @Autowired
    private FlowCommonUtil commonUtil;

    @Test
    public void getBasicUserExecutor() {
        String userIds = "[751444F9-BC78-11E8-8A20-0242C0A8440D,12356677]";
        userIds = XmlUtil.trimFirstAndLastChar(userIds, '[');
        userIds = XmlUtil.trimFirstAndLastChar(userIds, ']');
        List<String> ids = Arrays.asList(StringUtils.split(userIds, ','));
        List<Executor> executor = commonUtil.getBasicUserExecutors(ids);
        Assert.assertNotNull(executor);
        System.out.println(JsonUtils.toJson(executor));
    }
}
