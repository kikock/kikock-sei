package com.kcmp.ck.flow.util;

import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.service.BaseContextTestCase;
import com.kcmp.ck.flow.service.FlowTaskService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowTaskToolTest extends BaseContextTestCase {
    @Autowired
    private FlowTaskTool tool;
    @Autowired
    private FlowTaskService flowTaskService;

    @Test
    public void checkNextNodesCanAprool() {
        String taskId = "8D0D0466-34EB-11E9-BE62-0242C0A8441B";
        FlowTask flowTask = flowTaskService.findTaskById(taskId);
        Boolean canBatch = tool.checkNextNodesCanAprool(flowTask, null);
        Assert.assertTrue(canBatch);
    }
}
