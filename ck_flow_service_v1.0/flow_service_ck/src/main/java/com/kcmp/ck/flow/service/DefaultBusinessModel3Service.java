package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.core.ck.service.Validation;
import com.kcmp.ck.flow.api.IDefaultBusinessModel3Service;
import com.kcmp.ck.flow.basic.vo.Employee;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.dao.DefaultBusinessModel3Dao;
import com.kcmp.ck.flow.entity.DefaultBusinessModel3;
import com.kcmp.ck.flow.util.CodeGenerator;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by kikock
 * 销售业务表单服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class DefaultBusinessModel3Service extends BaseEntityService<DefaultBusinessModel3> implements IDefaultBusinessModel3Service {

    @Autowired
    private DefaultBusinessModel3Dao defaultBusinessModel3Dao;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Override
    protected BaseEntityDao<DefaultBusinessModel3> getDao(){
        return this.defaultBusinessModel3Dao;
    }

    /**
     * 数据保存操作
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional( propagation= Propagation.REQUIRED)
    public OperateResultWithData<DefaultBusinessModel3> save(DefaultBusinessModel3 entity) {
        Validation.notNull(entity, "持久化对象不能为空");
//        String businessCode = NumberGenerator.getNumber(DefaultBusinessModel3.class);
        String businessCode = CodeGenerator.genCodes(6,1).get(0);
        if(StringUtils.isEmpty(entity.getBusinessCode())){
            entity.setBusinessCode(businessCode);
        }
        return super.save(entity);
    }

    /**
     * 测试自定义执行人选择
     * @param businessId 业务单据id
     * @param paramJson  json参数
     * @return 执行人列表
     */
    @Override
    @Transactional( propagation= Propagation.REQUIRES_NEW)
    public List<Executor> getPersonToExecutorConfig(String businessId, String paramJson){
        List<Executor> executors = new ArrayList<Executor>();
        if(StringUtils.isNotEmpty(businessId)){
            DefaultBusinessModel3 defaultBusinessModel = defaultBusinessModel3Dao.findOne(businessId);
            if(defaultBusinessModel!=null){
                String orgid = defaultBusinessModel.getOrgId();
                //根据组织机构ID获取员工集合
                List<Employee> employeeList =flowCommonUtil.getEmployeesByOrgId(orgid);
                List<String> idList = new ArrayList<String>();
                for(Employee e : employeeList){
                    idList.add(e.getId());
                }
                //根据用户的id列表获取执行人
                executors = flowCommonUtil.getBasicUserExecutors(idList);
            }
        }
        return executors;
    }
}
