package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.core.ck.dto.SearchOrder;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.IBusinessModelService;
import com.kcmp.ck.flow.basic.vo.AppModule;
import com.kcmp.ck.flow.dao.BusinessModelDao;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.entity.FlowType;
import com.kcmp.ck.flow.util.ExpressionUtil;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.util.FlowTaskTool;
import com.kcmp.ck.flow.vo.ConditionVo;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.GenericType;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by kikock
 * 业务实体服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class BusinessModelService extends BaseEntityService<BusinessModel> implements IBusinessModelService {

    @Autowired
    private BusinessModelDao businessModelDao;
    @Autowired
    private FlowCommonUtil flowCommonUtil;
    @Autowired
    private FlowTaskService flowTaskService;
    @Autowired
    private FlowTypeService flowTypeService;
    @Autowired
    private FlowTaskPushService flowTaskPushService;
    @Autowired
    private FlowHistoryService flowHistoryService;
    @Autowired
    private FlowTaskTool flowTaskTool;

    @Override
    protected BaseEntityDao<BusinessModel> getDao() {
        return this.businessModelDao;
    }


    /**
     * 通过流程实例ID获取表单明细（长城移动端专用）
     */
    @Override
    public ResponseData getPropertiesByInstanceIdOfModile(String instanceId, String typeId, String id) {
        ResponseData responseData = new ResponseData();
        Boolean boo = true;  //是否为待办
        Boolean canMobile = true; //移动端是否可以审批
        Boolean canCancel = false; //如果是已办，是否可以撤回
        String historyId = "";//撤回需要的历史ID
        String flowTaskId = "";//待办ID
        String userId = ContextUtil.getUserId();
        Boolean isFlowtask = false;  //是否在待办中有数据
        if(StringUtils.isNotEmpty(instanceId) && StringUtils.isNotEmpty(typeId)){
            List<FlowTask>  flowTaskList =  flowTaskService.findByInstanceId(instanceId);
            if(flowTaskList!=null&&flowTaskList.size()>0){
               FlowTask flowTask = flowTaskList.stream().filter(a->userId.equals(a.getExecutorId())).findFirst().orElse(null);
               //如果待办中有当前用户的，默认就是查询的待办(实际可能是从已办过来的)
               if(flowTask != null){
                   isFlowtask = true;
                   flowTaskId = flowTask.getId();
                   canMobile = flowTask.getCanMobile() == null ? false : flowTask.getCanMobile();
               }
            }
            //待办里面没有，查询不到，默认取已办最后一条数据
           if(!isFlowtask){
               boo = false;
               Search search = new Search();
               search.addFilter(new SearchFilter("flowInstance.id", instanceId));
               search.addFilter(new SearchFilter("executorId", userId));
               search.addSortOrder(SearchOrder.desc("lastEditedDate"));
               List<FlowHistory> historylist = flowHistoryService.findByFilters(search);
               if(historylist != null && historylist.size() > 0){
                   FlowHistory a =historylist.get(0);
                   historyId = a.getId();
                   if (a != null && a.getCanCancel() != null && a.getCanCancel() == true &&
                           a.getTaskStatus() != null && "COMPLETED".equalsIgnoreCase(a.getTaskStatus()) &&
                           a.getFlowInstance() != null && a.getFlowInstance().isEnded() != null &&
                           a.getFlowInstance().isEnded() == false) {
                       Boolean canCel = flowTaskTool.checkoutTaskRollBack(a);
                       if (canCel) {
                           canCancel = true;
                       }
                   }
               }
           }

            FlowType flowType = flowTypeService.findOne(typeId);
            if (flowType == null) {
                responseData.setSuccess(false);
                responseData.setMessage("流程类型不存在！");
                return responseData;
            }
            String businessDetailServiceUrl = "";
            String apiBaseAddress = "";
            String businessModelCode = "";

            try {
                businessDetailServiceUrl = flowType.getBusinessDetailServiceUrl();
                if (StringUtils.isEmpty(businessDetailServiceUrl)) {
                    businessDetailServiceUrl = flowType.getBusinessModel().getBusinessDetailServiceUrl();
                }
                businessModelCode = flowType.getBusinessModel().getClassName();
            } catch (Exception e) {
                LogUtil.error(e.getMessage(), e);
                responseData.setSuccess(false);
                responseData.setMessage("获取业务实体数据失败！");
                return responseData;
            }

            try {
                String apiBaseAddressConfig = flowType.getBusinessModel().getAppModule().getApiBaseAddress();
                apiBaseAddress = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
            } catch (Exception e) {
                LogUtil.error(e.getMessage(), e);
                responseData.setSuccess(false);
                responseData.setMessage("获取模块Api基地址失败！");
                return responseData;
            }
            String url = apiBaseAddress + businessDetailServiceUrl;
            return this.getPropertiesByUrlOfModileByInstanceId(url, businessModelCode, id, boo, canMobile, canCancel, historyId ,flowTaskId);
        }else{
            responseData.setSuccess(false);
            responseData.setMessage("参数不能为空！");
        }
        return responseData;
    }

    public ResponseData getPropertiesByUrlOfModileByInstanceId(String url, String businessModelCode, String id, Boolean flowTaskIsInit, Boolean canMobile, Boolean canCancel, String historyId ,String flowTaskId) {
        ResponseData responseData = getPropertiesByUrlOfModile(url,businessModelCode,id,flowTaskIsInit,canMobile,canCancel,historyId);
        if(responseData.getSuccess()){
            Map<String, Object> properties = (Map<String, Object>)responseData.getData();
            properties.put("flowTaskId",flowTaskId);
            responseData.setData(properties);
        }
        return responseData;
    }

    /**
     * 通过任务ID获取表单明细（移动端专用）
     */
    @Override
    public ResponseData getPropertiesByTaskIdOfModile(String taskId, String typeId, String id) {
        ResponseData responseData = new ResponseData();
        Boolean boo = true;  //是否为待办
        Boolean canMobile = true; //移动端是否可以查看
        Boolean canCancel = false; //如果是已办，是否可以撤回
        String historyId = "";//撤回需要的历史ID
        if (StringUtils.isNotEmpty(taskId) && StringUtils.isNotEmpty(typeId)) {
            //能查询到就是待办，查不到就是已处理
            FlowTask flowTask = flowTaskService.findOne(taskId);
            if (flowTask == null) {
                boo = false;
                List<FlowHistory> historylist =flowHistoryService.findListByProperty("oldTaskId",taskId);
                if(historylist != null && historylist.size() > 0){
                    FlowHistory a =  historylist.get(0);
                    historyId = a.getId();
                    if (a != null && a.getCanCancel() != null && a.getCanCancel() == true &&
                            a.getTaskStatus() != null && "COMPLETED".equalsIgnoreCase(a.getTaskStatus()) &&
                            a.getFlowInstance() != null && a.getFlowInstance().isEnded() != null &&
                            a.getFlowInstance().isEnded() == false) {
                        Boolean canCel = flowTaskTool.checkoutTaskRollBack(a);
                        if (canCel) {
                            canCancel = true;
                        }
                    }
                }

            } else {
                canMobile = flowTask.getCanMobile() == null ? false : flowTask.getCanMobile();
            }
            FlowType flowType = flowTypeService.findOne(typeId);
            if (flowType == null) {
                responseData.setSuccess(false);
                responseData.setMessage("流程类型不存在！");
                return responseData;
            }


            String businessDetailServiceUrl = "";
            String apiBaseAddress = "";
            String businessModelCode = "";

            try {
                businessDetailServiceUrl = flowType.getBusinessDetailServiceUrl();
                if (StringUtils.isEmpty(businessDetailServiceUrl)) {
                    businessDetailServiceUrl = flowType.getBusinessModel().getBusinessDetailServiceUrl();
                }
                businessModelCode = flowType.getBusinessModel().getClassName();
            } catch (Exception e) {
                LogUtil.error(e.getMessage(), e);
                responseData.setSuccess(false);
                responseData.setMessage("获取业务实体数据失败！");
                return responseData;
            }

            try {
                String apiBaseAddressConfig = flowType.getBusinessModel().getAppModule().getApiBaseAddress();
                apiBaseAddress = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
            } catch (Exception e) {
                LogUtil.error(e.getMessage(), e);
                responseData.setSuccess(false);
                responseData.setMessage("获取模块Api基地址失败！");
                return responseData;
            }
            String url = apiBaseAddress + businessDetailServiceUrl;
            return this.getPropertiesByUrlOfModile(url, businessModelCode, id, boo, canMobile, canCancel, historyId);
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("参数不能为空！");
        }
        return responseData;
    }


    public ResponseData getPropertiesByUrlOfModile(String url, String businessModelCode, String id, Boolean flowTaskIsInit, Boolean canMobile, Boolean canCancel, String historyId) {
        ResponseData responseData = new ResponseData();
        if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(businessModelCode) && StringUtils.isNotEmpty(id)) {
            Map<String, Object> params = new HashMap();
            params.put("businessModelCode", businessModelCode);
            params.put("id", id);
            String messageLog = "开始调用‘表单明细’接口（移动端），接口url=" + url + ",参数值" + JsonUtils.toJson(params);
            try {
                Map<String, Object> properties = ApiClient.getEntityViaProxy(url, new GenericType<Map<String, Object>>() {
                }, params);
                //添加是否是待办参数，true为待办
                properties.put("flowTaskIsInit", flowTaskIsInit);
                //添加移动端是都可以查看
                properties.put("flowTaskCanMobile", canMobile);
                if (!flowTaskIsInit) {
                    //如果是已办，是否可以撤回，true为可以
                    properties.put("canCancel", canCancel);
                    //撤回需要的历史ID
                    properties.put("historyId", historyId);
                }
                responseData.setData(properties);
            } catch (Exception e) {
                messageLog += "表单明细接口调用异常：" + e.getMessage();
                LogUtil.error(messageLog, e);
                responseData.setSuccess(false);
                responseData.setMessage("接口调用异常，请查看日志！");
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("参数不能为空！");
        }
        return responseData;
    }

    /**
     * 查询条件属性说明
     * @param businessModelCode 业务实体代码
     * @return 实体对象
     * @throws ClassNotFoundException
     */
    @Override
    public ResponseData getProperties(String businessModelCode) throws ClassNotFoundException {
        ResponseData responseData = new ResponseData();
        BusinessModel businessModel = this.findByClassName(businessModelCode);
        if (businessModel != null) {
            Map<String, String> result = ExpressionUtil.getPropertiesDecMap(businessModel);
            if (result != null) {
                responseData.setData(result);
            } else {
                responseData.setSuccess(false);
                responseData.setMessage("调用接口异常，请查看日志！");
            }
        } else {
            responseData.setSuccess(false);
            responseData.setMessage("获取业务实体失败！");
        }
        return responseData;
    }

    /**
     * 查询条件属性说明
     * @param businessModelCode 业务实体代码
     * @return 实体对象
     * @throws ClassNotFoundException
     */
    @Override
    public List<ConditionVo> getPropertiesForConditionPojo(String businessModelCode) throws ClassNotFoundException {
        ResponseData responseData = this.getProperties(businessModelCode);
        List<ConditionVo> list = new ArrayList<ConditionVo>();
        if (responseData.getSuccess() && responseData.getData() != null) {
            Map<String, String> result = (Map<String, String>) responseData.getData();
            if (result.size() > 0) {
                result.forEach((key, value) -> {
                    ConditionVo bean = new ConditionVo();
                    bean.setCode(key);
                    bean.setName(value);
                    list.add(bean);
                });
            }
        }
        return list;
    }

    /**
     * 根据应用模块id查询业务实体
     * @param appModuleId 业务模块id
     * @return 实体清单
     */
    @Override
    public List<BusinessModel> findByAppModuleId(String appModuleId) {
        return businessModelDao.findByAppModuleId(appModuleId);
    }

    /**
     * 根据应用模块id查询业务实体
     * @param classNmae 业务模块代码
     * @return 实体对象
     */
    @Override
    public BusinessModel findByClassName(String classNmae) {
        return businessModelDao.findByClassName(classNmae);
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 返回操作结果对象
     */
    @Override
    public OperateResult delete(String id) {
        OperateResult operateResult = preDelete(id);
        if (Objects.isNull(operateResult) || operateResult.successful()) {
            BusinessModel entity = findOne(id);
            if (entity != null) {
                try {
                    getDao().delete(entity);
                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    e.printStackTrace();
                    SQLException sqlException = (SQLException) e.getCause().getCause();
                    if (sqlException != null && "23000".equals(sqlException.getSQLState())) {
                        return OperateResult.operationFailure("10027");
                    } else {
                        throw e;
                    }
                }
                // 业务实体删除成功！
                return OperateResult.operationSuccess("10057");
            } else {
                // 业务实体{0}不存在！
                return OperateResult.operationWarning("10058", id);
            }
        }
        clearFlowDefVersion();
        return operateResult;
    }

    /**
     * 保存业务
     * @param businessModel
     * @return
     */
    @Override
    public OperateResultWithData<BusinessModel> save(BusinessModel businessModel) {
        OperateResultWithData<BusinessModel> resultWithData = null;
        try {
            resultWithData = super.save(businessModel);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            cause = cause.getCause();
            SQLException sqlException = (SQLException) cause;
            if (sqlException != null && sqlException.getSQLState().equals("23000")) {
                resultWithData = OperateResultWithData.operationFailure("10037");//类全路径重复，请检查！
            } else {
                resultWithData = OperateResultWithData.operationFailure(e.getMessage());
            }
            LogUtil.error(e.getMessage(), e);
        }
        clearFlowDefVersion();
        return resultWithData;
    }

    /**
     * 清除流程定义版本缓存
     */
    private void clearFlowDefVersion() {
        String pattern = "FLowGetLastFlowDefVersion_*";
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    /**
     * 分页查询业务
     * @param searchConfig  查询参数
     * @return
     */
    @Override
    public PageResult<BusinessModel> findByPage(Search searchConfig) {
        List<AppModule> appModuleList = null;
        List<String> appModuleCodeList = null;
        appModuleList = flowCommonUtil.getBasicTenantAppModule();
        if (appModuleList != null && !appModuleList.isEmpty()) {
            appModuleCodeList = new ArrayList<String>();
            for (AppModule appModule : appModuleList) {
                appModuleCodeList.add(appModule.getCode());
            }
        }
        if (appModuleCodeList != null && !appModuleCodeList.isEmpty()) {
            SearchFilter searchFilter = new SearchFilter("appModule.code", appModuleCodeList, SearchFilter.Operator.IN);
            searchConfig.addFilter(searchFilter);
        }
        PageResult<BusinessModel> result = businessModelDao.findByPage(searchConfig);
        return result;
    }

    /**
     * 获取当前用户权限范围所有
     * @return 实体清单
     */
    @Override
    public List<BusinessModel> findAllByAuth() {
        List<BusinessModel> result = null;
        List<AppModule> appModuleList = null;
        List<String> appModuleCodeList = null;
        try {
            appModuleList = flowCommonUtil.getBasicTenantAppModule();
            if (appModuleList != null && !appModuleList.isEmpty()) {
                appModuleCodeList = new ArrayList<String>();
                for (AppModule appModule : appModuleList) {
                    appModuleCodeList.add(appModule.getCode());
                }
            }
            if (appModuleCodeList != null && !appModuleCodeList.isEmpty()) {
                result = businessModelDao.findByAppModuleCodes(appModuleCodeList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = businessModelDao.findAll();
        }
        return result;
    }
}
