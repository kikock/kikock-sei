package com.kcmp.ck.flow.listener;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.dao.FlowTaskDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.service.FlowTaskService;
import com.kcmp.ck.flow.util.ExpressionUtil;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.util.ServiceCallUtil;
import com.kcmp.ck.flow.util.TaskStatus;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.flow.vo.NodeInfo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;
import net.sf.json.JSONObject;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * Created by kikock
 * 用户池任务触发前监听事件
 * @author kikock
 * @email kikock@qq.com
 **/
public class PoolTaskBeforeListener implements org.activiti.engine.delegate.JavaDelegate {

    @Autowired
    private FlowTaskDao flowTaskDao;

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    FlowHistoryDao flowHistoryDao;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateTask) throws Exception {
            Date now = new Date();
            String actTaskDefKey = delegateTask.getCurrentActivityId();
            String actProcessDefinitionId = delegateTask.getProcessDefinitionId();
            String businessId =delegateTask.getProcessBusinessKey();
            FlowDefVersion flowDefVersion = flowDefVersionDao.findByActDefId(actProcessDefinitionId);
            String flowDefJson = flowDefVersion.getDefJson();
            JSONObject defObj = JSONObject.fromObject(flowDefJson);
            Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
            JSONObject currentNode = definition.getProcess().getNodes().getJSONObject(actTaskDefKey);
            JSONObject normal = currentNode.getJSONObject(Constants.NODE_CONFIG).getJSONObject(Constants.NORMAL);

            if (normal != null) {
                String serviceTaskId = (String) normal.get(Constants.SERVICE_TASK_ID);
                String poolTaskCode = (String) normal.get(Constants.POOL_TASK_CODE);
                if (!StringUtils.isEmpty(serviceTaskId)) {
                    Map<String,Object> tempV = delegateTask.getVariables();
                    tempV.put(Constants.POOL_TASK_ACT_DEF_ID,actTaskDefKey);
                    tempV.put(Constants.POOL_TASK_CODE,poolTaskCode);
                    String flowTaskName = (String) normal.get(Constants.NAME);
                    FlowTask flowTask = new FlowTask();
                    flowTask.setTaskJsonDef(currentNode.toString());
                    flowTask.setFlowName(definition.getProcess().getName());
                    //"用户池任务【等待执行】"
                    flowTask.setDepict( ContextUtil.getMessage("10052"));
                    flowTask.setTaskName(flowTaskName);
                    flowTask.setFlowDefinitionId(flowDefVersion.getFlowDefination().getId());
                    String actProcessInstanceId = delegateTask.getProcessInstanceId();
                    FlowInstance flowInstance = flowInstanceDao.findByActInstanceId(actProcessInstanceId);
                    flowTask.setFlowInstance(flowInstance);
                    String ownerName = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getName();
                    AppModule appModule = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule();
                    if(appModule!=null && StringUtils.isNotEmpty(appModule.getName())){
                        ownerName = appModule.getName();
                    }
                    flowTask.setOwnerAccount(Constants.ANONYMOUS);
                    flowTask.setOwnerName(ownerName);
                    flowTask.setExecutorAccount(Constants.ANONYMOUS);
                    flowTask.setExecutorId("");
                    flowTask.setExecutorName(ownerName);
                    flowTask.setCandidateAccount("");
                    flowTask.setActDueDate(now);

                    flowTask.setActTaskDefKey(actTaskDefKey);
                    flowTask.setPreId(null);
                    flowTask.setTaskStatus(TaskStatus.COMPLETED.toString());
                    flowTask.setTaskStatus(TaskStatus.INIT.toString());

                    //选择下一步可能的执行子流程路径
                    ApplicationContext applicationContext = ContextUtil.getApplicationContext();
                    FlowTaskService flowTaskService = (FlowTaskService)applicationContext.getBean("flowTaskService");
                    List<NodeInfo> nodeInfoList = flowTaskService.findNexNodesWithUserSet(flowTask);
                    List<String> paths = new ArrayList<String>();
                    if(nodeInfoList!=null && !nodeInfoList.isEmpty()){
                         for(NodeInfo nodeInfo :nodeInfoList){
                             if(StringUtils.isNotEmpty(nodeInfo.getCallActivityPath())){
                                 paths.add(nodeInfo.getCallActivityPath());
                             }
                         }
                    }
                    if(!paths.isEmpty()){
                        //提供给调用服务，子流程的绝对路径，用于存入单据id
                        tempV.put(Constants.CALL_ACTIVITY_SON_PATHS,paths);
                    }
                    String param = JsonUtils.toJson(tempV);
                    FlowOperateResult flowOperateResult = null;
                    String callMessage = null;
                    try{
                        flowOperateResult =  ServiceCallUtil.callService(serviceTaskId, businessId, param);
                        if(flowOperateResult!=null && flowOperateResult.isSuccess() && StringUtils.isNotEmpty(flowOperateResult.getUserId())){
                            runtimeService.setVariable(delegateTask.getProcessInstanceId(),Constants.POOL_TASK_CALLBACK_USER_ID+actTaskDefKey, flowOperateResult.getUserId());
                        }
                        callMessage = flowOperateResult.getMessage();
                    }catch (Exception e){
                        callMessage = e.getMessage();
                    }

                    if((flowOperateResult==null || !flowOperateResult.isSuccess())){
                        List<FlowTask> flowTaskList = flowTaskService.findByInstanceId(flowInstance.getId());
                        List<FlowHistory> flowHistoryList = flowHistoryDao.findByInstanceId(flowInstance.getId());

                        //如果是开始节点，手动回滚
                        if(flowTaskList.isEmpty()&&flowHistoryList.isEmpty()){
                            new Thread(){
                                @Override
                                public void run(){
                                    BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                                    Boolean result = false;
                                    int index = 5;
                                    while (!result && index>0){
                                        try {
                                            Thread.sleep(1000*(6-index));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            result =  ExpressionUtil.resetState(businessModel,flowInstance.getBusinessId(), FlowStatus.INIT);
                                        }catch (Exception e){
                                            LogUtil.error(e.getMessage(),e);
                                        }
                                        index--;
                                    }
                                }
                            }.start();
                        }
                        throw new FlowException(callMessage);//抛出异常
                    }

                }else{
                    throw new FlowException("工作池任务不能找到，可能已经被删除，serviceId=" + serviceTaskId);
                }
            }
    }
}
