package com.kcmp.ck.flow.listener;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowDefinationDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.dao.FlowServiceUrlDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.util.ExpressionUtil;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowDefination;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.GenericType;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 启动完成监听器
 * @author kikock
 * @email kikock@qq.com
 **/
public class StartEventCompleteListener implements ExecutionListener {

	public StartEventCompleteListener(){
	}
    private static final long serialVersionUID = 1L;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private FlowDefinationDao flowDefinationDao;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private FlowServiceUrlDao flowServiceUrlDao;

    @Override
    @Transactional( propagation= Propagation.REQUIRED)
    public void notify(DelegateExecution delegateTask) {
        ExecutionEntity taskEntity = (ExecutionEntity) delegateTask;
        Map<String,Object> variables = delegateTask.getVariables();
        ProcessInstance processInstance  = taskEntity.getProcessInstance();
        ProcessInstance parentProcessInstance = null;
//        ExecutionEntity son = taskEntity.getSubProcessInstance();
        ExecutionEntity parent = taskEntity.getSuperExecution();
        String currentBusinessId = null;
        String callActivityPath = null;
        if(parent != null){
            StringBuffer sonBusinessVNameBuff = new StringBuffer();
            ExecutionEntity parentTemp = parent;
            while (parentTemp!=null){
                parentProcessInstance = parentTemp.getProcessInstance();
                Map<String,Object> variablesParent = runtimeService.getVariables(parentTemp.getId());
                variables.putAll(variablesParent);
                delegateTask.setVariables(variables);
                String parentDefinitionKey =  parentProcessInstance.getProcessDefinitionKey();
                if(StringUtils.isEmpty(parentDefinitionKey)){
                    // 取得流程定义
                    ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                            .getDeployedProcessDefinition( parentProcessInstance.getProcessDefinitionId());                   ;
                    parentDefinitionKey = definition.getKey();
                }
                sonBusinessVNameBuff.insert(0,"/"+parentDefinitionKey+"/"+parentTemp.getActivityId());
                parentTemp = ((ExecutionEntity)parentProcessInstance).getSuperExecution();
            }
            callActivityPath = sonBusinessVNameBuff.toString();
            parentProcessInstance = parent.getProcessInstance();
            Map<String,Object> variablesParent = runtimeService.getVariables(parent.getId());
            variables.putAll(variablesParent);
            delegateTask.setVariables(variables);
            String parentDefinitionKey =  parentProcessInstance.getProcessDefinitionKey();
            if(StringUtils.isEmpty(parentDefinitionKey)){
                // 取得流程定义
                ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition( parentProcessInstance.getProcessDefinitionId());                   ;
                parentDefinitionKey = definition.getKey();
            }
            FlowDefination flowDefinationParent = flowDefinationDao.findByDefKey(parentDefinitionKey);
            String definitionKey =  processInstance.getProcessDefinitionKey();
            FlowDefination flowDefination = flowDefinationDao.findByDefKey(definitionKey);
            String parentBusinessModelCode = flowDefinationParent.getFlowType().getBusinessModel().getClassName();
            String sonBusinessModelCode = flowDefination.getFlowType().getBusinessModel().getClassName();
//            //父流程业务实体代码+callActivtiy的key+子流程key+'_sonBusinessId'
//            String sonBusinessVName = parentBusinessModelCode+"_"+parent.getActivityId()+"_"+definitionKey+"_sonBusinessId";
            sonBusinessVNameBuff.append("/"+definitionKey);
            currentBusinessId =(String )delegateTask.getVariable(sonBusinessVNameBuff.toString());
            List<String> userVarNameList = (List)delegateTask.getVariable(sonBusinessVNameBuff + Constants.SON_PROCESS_SELECT_NODE_USER);
            if(userVarNameList!=null && !userVarNameList.isEmpty()){
                for(String userVarName :userVarNameList){
                   Object userValue = delegateTask.getVariable(sonBusinessVNameBuff+"/"+userVarName);
                    delegateTask.setVariable(userVarName,userValue);
                }
            }
            if(StringUtils.isEmpty(currentBusinessId)){
                if(parentBusinessModelCode.equals(sonBusinessModelCode)){//非跨业务实体子流程
                    //设置子流程businessKey
                    String  parentBusinessKey = parentProcessInstance.getBusinessKey();
                    runtimeService.updateBusinessKey(processInstance.getId(),parentBusinessKey);
                    currentBusinessId = parentBusinessKey;
                }else{//跨业务实体子流程,必须指定子流程关联单据id
                    String message = ContextUtil.getMessage("10039");
                    throw new FlowException(message);//子流程关联的单据找不到！
                }
            }else {
                    runtimeService.updateBusinessKey(processInstance.getId(),currentBusinessId);
            }

        }
//        FlowInstance  flowInstance = flowInstanceDao.findByActInstanceId(processInstance.getId());
//        if(flowInstance==null){
            FlowInstance flowInstance = new FlowInstance();
            flowInstance.setTenantCode(ContextUtil.getTenantCode());
            flowInstance.setBusinessId(processInstance.getBusinessKey());
            String workCaption = null;
            if(variables.containsKey(Constants.WORK_CAPTION)){
                workCaption = (String)variables.get(Constants.WORK_CAPTION);//工作说明
            }
            flowInstance.setBusinessModelRemark(workCaption);

            String businessCode = null;
            if(variables.containsKey(Constants.BUSINESS_CODE)){
                businessCode = (String)variables.get(Constants.BUSINESS_CODE);//工作说明
            }
            flowInstance.setBusinessCode(businessCode);

            String businessName = null;
            if(variables.containsKey(Constants.NAME)){
                businessName = (String)variables.get(Constants.NAME);//业务单据名称
            }
            flowInstance.setBusinessName(businessName);

            String flowDefVersionId = null;
            if(variables.containsKey(Constants.FLOW_DEF_VERSION_ID)){
                flowDefVersionId = (String) variables.get(Constants.FLOW_DEF_VERSION_ID);//流程定义版本id
            }
            FlowDefVersion flowDefVersion = flowCommonUtil.getLastFlowDefVersion(flowDefVersionId);flowDefVersionDao.findOne(flowDefVersionId);
            if(flowDefVersion==null){
                String message = ContextUtil.getMessage("10040");
                throw new FlowException(message);//"流程版本找不到！
            }
            flowInstance.setFlowDefVersion(flowDefVersion);
            Date now = new Date();
            flowInstance.setStartDate(now);
            flowInstance.setEndDate(now);
            flowInstance.setFlowName(flowDefVersion.getName());
            flowInstance.setActInstanceId(processInstance.getId());
            if(parentProcessInstance !=null){
                FlowInstance flowInstanceP = flowInstanceDao.findByActInstanceId(parentProcessInstance.getId());
                String actDefinitionKey = processInstance.getProcessDefinitionKey();
               List<FlowDefVersion> flowDefVersionList = flowDefVersionDao.findByKeyActivate(actDefinitionKey);
                if(flowDefVersionList == null || flowDefVersionList.isEmpty()){
                    String message = ContextUtil.getMessage("10041");
                    throw new FlowException(message);//子流程的流程版本找不到！
                }
                flowDefVersion = flowDefVersionList.get(0);
                flowInstance.setFlowDefVersion(flowDefVersion);
                flowInstance.setBusinessId(currentBusinessId);
                flowInstance.setCallActivityPath(callActivityPath);
                flowInstance.setParent(flowInstanceP);
            }
            flowInstanceDao.save(flowInstance);
//        }

        BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
        AppModule appModule = businessModel.getAppModule();

        FlowOperateResult callAfterStartResult = callAfterStart(flowInstance.getBusinessId(),flowInstance.getFlowDefVersion());
        if(callAfterStartResult!=null && callAfterStartResult.isSuccess()!=true){
//            String message = ContextUtil.getMessage("10043",callAfterStartResult.getMessage());
//            String messageLogger =message+
//                    ";businessId="+flowInstance.getBusinessId()
//                    +",FlowDefVersion.id="+flowInstance.getFlowDefVersion().getId()
//                    +",appModule.code="+appModule.getCode();
//            logger.info(messageLogger);
            throw new FlowException(callAfterStartResult.getMessage());
        }

        Boolean result = ExpressionUtil.resetState(businessModel,flowInstance.getBusinessId(), FlowStatus.INPROCESS);
        if(!result){
            String message = ContextUtil.getMessage("10042");
            throw new FlowException(message);//流程启动-调用重置表单服务失败！
        }
    }

    /**
     * 流程启动结束后调用调用服务
     * @param businessKey
     * @param flowDefVersion
     * @return
     */
    private FlowOperateResult callAfterStart( String businessKey,FlowDefVersion flowDefVersion){
        FlowOperateResult result = null;
        if(flowDefVersion!=null && StringUtils.isNotEmpty(businessKey)){
            String afterStartServiceId = flowDefVersion.getAfterStartServiceId();
            Boolean afterStartServiceAync = flowDefVersion.getAfterStartServiceAync();

            if(StringUtils.isNotEmpty(afterStartServiceId)){
                FlowServiceUrl flowServiceUrl = flowServiceUrlDao.findOne(afterStartServiceId);
                if(flowServiceUrl == null){
                    throw new FlowException("获取启动后事件失败，可能已经被删除！");
                }
                String checkUrl = flowServiceUrl.getUrl();
                if(StringUtils.isNotEmpty(checkUrl)){
                    String apiBaseAddressConfig = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
                    String baseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                    String checkUrlPath = baseUrl+checkUrl;
                    FlowInvokeParams flowInvokeParams = new FlowInvokeParams();
                    flowInvokeParams.setId(businessKey);
                    String msg = "启动后事件【" + flowServiceUrl.getName() + "】";
                    String urlAndData = "-请求地址："+checkUrlPath+"，参数："+ JsonUtils.toJson(flowInvokeParams);
                    if(afterStartServiceAync == true){
                        //模拟异步
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    FlowOperateResult resultAync =  ApiClient.postViaProxyReturnResult(checkUrlPath,new GenericType<FlowOperateResult>() {},flowInvokeParams);
                                    if(resultAync==null){
                                        LogUtil.bizLog(msg + "异步调用返回信息为空!" + urlAndData);
                                    }else if(!resultAync.isSuccess()){
                                        LogUtil.bizLog(msg + "异步调用返回信息：【" + resultAync.toString() + "】" + urlAndData);
                                    }
                                }catch (Exception e){
                                    LogUtil.error(msg + "异步调用内部报错!" + urlAndData,e);
                                }
                            }
                        }).start();
                    }else {
                       try{
                           result = ApiClient.postViaProxyReturnResult(checkUrlPath,new GenericType<FlowOperateResult>() {},flowInvokeParams);
                           if(result==null){
                               result = new FlowOperateResult(false,msg + "返回信息为空！");
                               LogUtil.bizLog(msg + "返回参数为空!" + urlAndData);
                           }else if(!result.isSuccess()){
                               LogUtil.bizLog(msg + "返回信息：【"+result.toString() + "】" + urlAndData);
                               result.setMessage(msg + "返回信息：【" + result.getMessage() + "】");
                           }
                       }catch (Exception e){
                           LogUtil.error(msg + "内部报错!" + urlAndData,e);
                           throw new FlowException(msg + "内部报错，详情请查看日志！");
                       }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 模拟异步,上传调用日志
     * @param message
     */
    static void asyncUploadLog(String message){
        //模拟异步,上传调用日志
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.bizLog(message);
            }
        }).start();
    }
}
