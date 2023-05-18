package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.dao.AppModuleDao;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.util.ApiJsonUtils;
import com.kcmp.ck.vo.ResponseData;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class AppModuleServiceTest extends BaseContextTestCase{

    @Autowired
    private AppModuleService service;
    @Autowired
    private DefaultFlowBaseService defaultFlowBaseService;

    @Autowired
    private AppModuleDao appModuleDao;

    @Test
    public void findAll() {
        List<AppModule> appModules = service.findAll();
        Assert.assertNotNull(appModules);
        System.out.println(ApiJsonUtils.toJson(appModules));
    }

    @Test
    public void testFindByCodes(){
        List<String> lists = Lists.newArrayList();
        lists.add("FSOP_AMS_REACT");
        List<AppModule> byCodes = appModuleDao.findByCodes(lists);
        System.out.println(ApiJsonUtils.toJson(byCodes));
    }

    @Test
    public void startFlow() {
        String businessModelCode="com.kcmp.srm.pp.entity.PurchaseOrder";
        String businessKey="CAF50148-D961-11E9-8337-0242C0A8450A";
        String opinion ="";
        String typeId ="8F8F1030-4C6B-11E9-9485-0242C0A8450A";
        String flowDefKey="purchaseOrderCOPY";
        String taskList ="[{\"nodeId\":\"ServiceTask_19\",\"userVarName\":\"ServiceTask_19_ServiceTask\",\"option\":\"待审批\",\"flowTaskType\":\"serviceTask\",\"instancyStatus\":null,\"userIds\":\"7d345ed4-4443-4b71-aa00-29e23c191a9e\"}]";
        String anonymousNodeId =null;

        ResponseData responseData=null;
        try{
             responseData = defaultFlowBaseService.startFlow(businessModelCode,
                    businessKey,opinion,typeId,flowDefKey,taskList,anonymousNodeId);
        }catch (Exception e){
            e.printStackTrace();
        }
        Assert.assertNotNull(responseData);
        System.out.println(ApiJsonUtils.toJson(responseData));
    }

}
