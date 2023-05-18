package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.vo.SignalPoolTaskVO;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.ResponseData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowInstanceServiceTest extends BaseContextTestCase {

    @Autowired
    private FlowInstanceService service;

    @Test
    public void findTaskByBusinessIdAndActTaskKey() {
        String id = "BC808F8B-81BB-11E9-9D74-0242C0A84410";
        String flowTaskId = "PoolTask_6";
        FlowTask task = service.findTaskByBusinessIdAndActTaskKey(id, flowTaskId);
        Assert.assertNotNull(task);
        System.out.println(JsonUtils.toJson(task));
    }

    /**
     * 工作池任务确定执行人
     */
    @Test
    public void signalPoolTaskByBusinessId() {
        String businessId = "F293FCCB-59EC-11EA-AB67-0242C0A84421";
        String poolTaskActDefId = "PoolTask_22";
        String userId = "1592D012-A330-11E7-A967-02420B99179E";
        Map<String, Object> v = new HashMap<>();
        OperateResult operateResult = service.signalPoolTaskByBusinessId(businessId, poolTaskActDefId, userId, v);
        System.out.println(JsonUtils.toJson(operateResult));
    }

    /**
     * 工作池任务确定多执行人
     */
    @Test
    public void signalPoolTaskByBusinessIdAndUserList() {
        SignalPoolTaskVO signalPoolTaskVO = new SignalPoolTaskVO();
        signalPoolTaskVO.setBusinessId("F293FCCB-59EC-11EA-AB67-0242C0A84421");
        signalPoolTaskVO.setPoolTaskActDefId("PoolTask_22");
        List<String> list = new ArrayList<String>();
        list.add("1592D012-A330-11E7-A967-02420B99179E");
        list.add("1AE28F00-2FFC-11E9-AC2E-0242C0A84417");
        Map<String, Object> v = new HashMap<>();
        signalPoolTaskVO.setUserIds(list);
        signalPoolTaskVO.setMap(v);
        ResponseData responseData = service.signalPoolTaskByBusinessIdAndUserList(signalPoolTaskVO);
        System.out.println(JsonUtils.toJson(responseData));
    }
}
