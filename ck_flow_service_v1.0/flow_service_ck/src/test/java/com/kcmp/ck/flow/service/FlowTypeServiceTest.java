package com.kcmp.ck.flow.service;

import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowType;
import com.kcmp.ck.vo.OperateResultWithData;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by kikock
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowTypeServiceTest extends BaseContextTestCase {

    @Autowired
    private BusinessModelService businessModelService;

    @Autowired
    private FlowTypeService flowTypeService;

    @Test
    public void save() {
        List<BusinessModel> businessModels = businessModelService.findAll();
        if (businessModels != null && businessModels.size() > 0) {
            FlowType flowType = new FlowType();
            flowType.setBusinessModel(businessModels.get(0));
            flowType.setCode("test_mac");
            flowType.setName("测试");
            flowType.setDepict("测试");
            OperateResultWithData<FlowType> op = flowTypeService.save(flowType);
            System.out.println(op);
        }
    }
}
