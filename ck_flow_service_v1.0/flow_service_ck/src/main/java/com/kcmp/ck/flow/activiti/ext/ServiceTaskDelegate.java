package com.kcmp.ck.flow.activiti.ext;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.service.FlowDefinationService;
import com.kcmp.ck.flow.service.FlowTaskService;
import com.kcmp.ck.flow.util.*;
import com.kcmp.ck.flow.util.ExpressionUtil;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.util.FlowListenerTool;
import com.kcmp.ck.flow.util.ServiceCallUtil;
import com.kcmp.ck.flow.util.TaskStatus;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.flow.vo.NodeInfo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;
import net.sf.json.JSONObject;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by kikock
 * 委托任务服务
 * @author kikock
 * @email kikock@qq.com
 **/
public class ServiceTaskDelegate implements org.activiti.engine.delegate.JavaDelegate {

    public ServiceTaskDelegate() {
    }

    private final Logger logger = LoggerFactory.getLogger(ServiceTaskDelegate.class);

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    FlowHistoryDao flowHistoryDao;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    FlowDefinationService flowDefinationService;

    @Autowired
    private FlowListenerTool flowListenerTool;

    @Autowired
    private FlowTaskService flowTaskService;


    @Override
    public void execute(DelegateExecution delegateTask) throws Exception {

        try {
            ExecutionEntity taskEntity = (ExecutionEntity) delegateTask;
            String actTaskDefKey = delegateTask.getCurrentActivityId();
            String actProcessDefinitionId = delegateTask.getProcessDefinitionId();
            String businessId = delegateTask.getProcessBusinessKey();

            FlowDefVersion flowDefVersion = flowDefVersionDao.findByActDefId(actProcessDefinitionId);
            String flowDefJson = flowDefVersion.getDefJson();
            JSONObject defObj = JSONObject.fromObject(flowDefJson);
            Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
            net.sf.json.JSONObject currentNode = definition.getProcess().getNodes().getJSONObject(actTaskDefKey);
            net.sf.json.JSONObject normal = currentNode.getJSONObject(Constants.NODE_CONFIG).getJSONObject(Constants.NORMAL);
            if (normal != null) {
                String serviceTaskId = (String) normal.get(Constants.SERVICE_TASK_ID);
                String flowTaskName = (String) normal.get(Constants.NAME);
                if (!StringUtils.isEmpty(serviceTaskId)) {
                    Map<String, Object> tempV = delegateTask.getVariables();

                    FlowHistory flowHistory = new FlowHistory();
                    flowHistory.setTaskJsonDef(currentNode.toString());
                    flowHistory.setFlowName(definition.getProcess().getName());
                    flowHistory.setDepict(ContextUtil.getMessage("10047"));//服务任务【自动执行】
                    flowHistory.setFlowTaskName(flowTaskName);
                    flowHistory.setFlowDefId(flowDefVersion.getFlowDefination().getId());
                    String actProcessInstanceId = delegateTask.getProcessInstanceId();
                    List<TaskEntity> taskList = taskEntity.getTasks();
                    LogUtil.debug("委托任务列表【{}】！", JsonUtils.toJson(taskList));
                    FlowInstance flowInstance = flowInstanceDao.findByActInstanceId(actProcessInstanceId);
                    flowHistory.setFlowInstance(flowInstance);

                    flowHistory.setOwnerAccount(Constants.ADMIN);
                    flowHistory.setOwnerName(ContextUtil.getMessage("10048"));
                    flowHistory.setExecutorAccount(Constants.ADMIN);
                    flowHistory.setExecutorId("");
                    flowHistory.setExecutorName(ContextUtil.getMessage("10048"));
                    flowHistory.setCandidateAccount("");
                    flowHistory.setActStartTime(new Date());
                    flowHistory.setActHistoryId(null);
                    flowHistory.setActTaskDefKey(actTaskDefKey);
                    flowHistory.setPreId(null);

                    FlowTask flowTask = new FlowTask();
                    BeanUtils.copyProperties(flowHistory, flowTask);
                    flowTask.setTaskStatus(TaskStatus.INIT.toString());

                    List<String> paths = flowListenerTool.getCallActivitySonPaths(flowTask);
                    if (!paths.isEmpty()) {
                        tempV.put(Constants.CALL_ACTIVITY_SON_PATHS, paths);//提供给调用服务，子流程的绝对路径，用于存入单据id
                    }
                    String param = JsonUtils.toJson(tempV);

                    FlowOperateResult flowOperateResult = null;
                    String callMessage = null;
                    try {
                        flowOperateResult = ServiceCallUtil.callService(serviceTaskId, businessId, param);
                        callMessage = flowOperateResult.getMessage();
                    } catch (Exception e) {
                        callMessage = e.getMessage();
                    }
                    if ((flowOperateResult == null || !flowOperateResult.isSuccess())) {
                        List<FlowTask> flowTaskList = flowTaskService.findByInstanceId(flowInstance.getId());
                        List<FlowHistory> flowHistoryList = flowHistoryDao.findByInstanceId(flowInstance.getId());

                        if (flowTaskList.isEmpty() && flowHistoryList.isEmpty()) { //如果是开始节点，手动回滚
                            new Thread() {
                                @Override
                                public void run() {
                                    BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                                    Boolean result = false;
                                    int index = 5;
                                    while (!result && index > 0) {
                                        try {
                                            Thread.sleep(1000 * (6 - index));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            result = ExpressionUtil.resetState(businessModel, flowInstance.getBusinessId(), FlowStatus.INIT);
                                        } catch (Exception e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                        index--;
                                    }
                                }
                            }.start();
                        }
                        throw new FlowException(callMessage);//抛出异常
                    }

                    Calendar c = new GregorianCalendar();
                    c.setTime(new Date());
                    c.add(Calendar.SECOND, 10);
                    flowHistory.setActEndTime(c.getTime());//服务任务，默认延后10S
                    flowHistory.setTaskStatus(TaskStatus.COMPLETED.toString());
                    if (flowHistory.getActDurationInMillis() == null) {
                        Long actDurationInMillis = flowHistory.getActEndTime().getTime() - flowHistory.getActStartTime().getTime();
                        flowHistory.setActDurationInMillis(actDurationInMillis);
                    }
                    flowHistoryDao.save(flowHistory);

                    //选择下一步执行人，默认选择第一个(会签、单签、串、并行选择全部)
                    List<NodeInfo> results;
                    if (flowDefVersion != null && flowDefVersion.getSolidifyFlow() == true) { //固化流程
                        results = flowListenerTool.nextNodeInfoList(flowTask, delegateTask, true);
                    } else {
                        results = flowListenerTool.nextNodeInfoList(flowTask, delegateTask, false);
                    }
                    //初始化节点执行人
                    List<NodeInfo> nextNodes = flowListenerTool.initNodeUsers(results, delegateTask, actTaskDefKey);
                    //初始化下一步任务信息
                    flowListenerTool.initNextAllTask(nextNodes, taskEntity, flowHistory);
                } else {
                    throw new FlowException("服务事件不能找到，可能已经被删除，serviceId=" + serviceTaskId);
                }
            }
        } catch (Exception e) {
            if (e.getClass() != FlowException.class) {
                LogUtil.error(e.getMessage(), e);
            }
            throw e;
        }
    }
}
