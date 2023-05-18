package com.kcmp.ck.flow.service.client;

import com.kcmp.core.ck.dao.jpa.BaseDao;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.client.util.ExpressionUtil;
import com.kcmp.ck.flow.clientapi.ICommonConditionService;
import com.kcmp.ck.flow.common.util.BusinessUtil;
import com.kcmp.ck.flow.constant.BusinessEntityAnnotaion;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.entity.IBusinessFlowEntity;
import com.kcmp.ck.flow.entity.IConditionPojo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 通用客户端条件表达式服务
 * @author kikock
 * @email kikock@qq.com
 **/
public class CommonConditionService implements ICommonConditionService {

    public CommonConditionService() {
    }

    /**
     * 获取条件POJO属性说明
     * @param businessModelCode 业务实体代码
     * @param all               是否查询全部
     * @return  POJO属性说明Map
     * @throws ClassNotFoundException 类找不到异常
     */
    @Override
    public Map<String, String> properties(String businessModelCode,Boolean all) throws ClassNotFoundException {
        String conditonPojoClassName = null;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesForConditionPojo(conditonPojoClassName,all);
    }

    /**
     * 获取条件POJO属性初始化值键值对
     * @param businessModelCode 业务实体代码
     * @return  POJO属性说明Map
     * @throws ClassNotFoundException 类找不到异常
     * @throws InvocationTargetException 目标类解析异常
     * @throws InstantiationException 实例异常
     * @throws IllegalAccessException 访问异常
     * @throws NoSuchMethodException 没有方法异常
     */
    @Override
    public Map<String, Object> initPropertiesAndValues(String businessModelCode) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String conditonPojoClassName = null;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesAndValues(conditonPojoClassName,true);
    }

    /**
     * 获取条件POJO属性键值对
     * @param businessModelCode 业务实体代码
     * @param id                单据id
     * @return  POJO属性说明Map
     * @throws ClassNotFoundException 类找不到异常
     * @throws InvocationTargetException 目标类解析异常
     * @throws InstantiationException 实例异常
     * @throws IllegalAccessException 访问异常
     * @throws NoSuchMethodException 没有方法异常
     */
    @Override
    public Map<String, Object> propertiesAndValues(String businessModelCode, String id,Boolean all) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String conditonPojoClassName = null;
        String daoBeanName = null;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        daoBeanName = getDaoBeanName(businessModelCode);
        return this.getConditonPojoMap(conditonPojoClassName, daoBeanName, id,all);
    }

    /**
     * 重置单据状态
     * @param businessModelCode     业务实体代码
     * @param id                    单据id
     * @param status                状态
     * @return 返回结果
     * @throws ClassNotFoundException 类找不到异常
     * @throws InvocationTargetException 目标类解析异常
     * @throws InstantiationException 实例异常
     * @throws IllegalAccessException 访问异常
     * @throws NoSuchMethodException 没有方法异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean resetState(String businessModelCode, String id, FlowStatus status) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String daoBeanName = null;
        daoBeanName = getDaoBeanName(businessModelCode);
        ApplicationContext applicationContext = ContextUtil.getApplicationContext();
        BaseDao appModuleDao = (BaseDao) applicationContext.getBean(daoBeanName);
        IBusinessFlowEntity content = (IBusinessFlowEntity) appModuleDao.findOne(id);
        if(status==FlowStatus.INIT){//针对流程强制终止时，表单已经被删除的情况
            if(content!=null){
                content.setFlowStatus(status);
                appModuleDao.save(content);
            }
        }else{
            if(content==null){
                throw new RuntimeException("business.id do not exist, can not start or complete the process!");
            }
            content.setFlowStatus(status);
            appModuleDao.save(content);
        }
        return true;
    }

    /**
     * 获取条件POJO属性键值对
     * @param businessModelCode 业务实体代码
     * @param id                单据id
     * @return  POJO属性说明Map
     */
    @Override
    public Map<String,Object> businessPropertiesAndValues(String businessModelCode, String id) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,NoSuchMethodException{
        String daoBeanName = null;
        daoBeanName = getDaoBeanName(businessModelCode);
        ApplicationContext applicationContext = ContextUtil.getApplicationContext();
        BaseDao appModuleDao = (BaseDao) applicationContext.getBean(daoBeanName);
        IBusinessFlowEntity content = (IBusinessFlowEntity) appModuleDao.findOne(id);
        return   BusinessUtil.getPropertiesAndValues(content,null);
    }

    /**
     * 推送待办
     * @param  list 待办信息
     */
    @Override
    public String pushTasksToDo(List<FlowTask> list){
        List<String> megList = new ArrayList<String>();
        if(list!=null&&list.size()>0){
            list.forEach(a->megList.add("【是否已处理："+a.getTaskStatus()+"-id="+a.getId()+"】"));
        }
        return "推送成功！";
    }

    public Map<String, String> propertiesAll(String businessModelCode) throws ClassNotFoundException {
        String conditonPojoClassName = null;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesForConditionPojo(conditonPojoClassName,true);
    }

    private Map<String, String> getPropertiesForConditionPojo(String conditonPojoClassName,Boolean all) throws ClassNotFoundException {
        return ExpressionUtil.getProperties(conditonPojoClassName,all);
    }

    private Map<String, Object> getPropertiesAndValues(String conditonPojoClassName,Boolean all) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return ExpressionUtil.getPropertiesAndValues(conditonPojoClassName,all);
    }

    private Map<String, Object> getConditonPojoMap(String conditonPojoClassName, String daoBeanName, String id,Boolean all) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Class conditonPojoClass = Class.forName(conditonPojoClassName);
        ApplicationContext applicationContext = ContextUtil.getApplicationContext();
        BaseDao appModuleDao = (BaseDao) applicationContext.getBean(daoBeanName);
        IConditionPojo conditionPojo = (IConditionPojo) conditonPojoClass.newInstance();
        BaseEntity content = (BaseEntity) appModuleDao.findOne(id);
        BeanUtils.copyProperties(conditionPojo, content);
        if (conditionPojo != null) {
            return new ExpressionUtil<IConditionPojo>().getPropertiesAndValues(conditionPojo,all);
        } else {
            return null;
        }
    }

    private String getDaoBeanName(String className)throws ClassNotFoundException {
        BusinessEntityAnnotaion businessEntityAnnotaion = this.getBusinessEntityAnnotaion(className);
         return   businessEntityAnnotaion.daoBean();
    }
    private String getConditionBeanName(String className)throws ClassNotFoundException {
        BusinessEntityAnnotaion businessEntityAnnotaion = this.getBusinessEntityAnnotaion(className);
        return   businessEntityAnnotaion.conditionBean();
    }
    private BusinessEntityAnnotaion getBusinessEntityAnnotaion(String className)throws ClassNotFoundException {
        if (StringUtils.isNotEmpty(className)) {
            Class sourceClass = Class.forName(className);
            BusinessEntityAnnotaion businessEntityAnnotaion = (BusinessEntityAnnotaion) sourceClass.getAnnotation(BusinessEntityAnnotaion.class);
            return  businessEntityAnnotaion;
        }else {
            throw new RuntimeException("className is null!");
        }
    }
}
