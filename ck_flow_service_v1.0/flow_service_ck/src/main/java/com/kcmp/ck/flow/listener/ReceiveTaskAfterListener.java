package com.kcmp.ck.flow.listener;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.dao.FlowTaskDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.service.FlowDefinationService;
import com.kcmp.ck.flow.util.FlowListenerTool;
import com.kcmp.ck.flow.util.TaskStatus;
import com.kcmp.ck.flow.vo.NodeInfo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;
import net.sf.json.JSONObject;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by kikock
 * 接收任务触发后监听事件
 * @author kikock
 * @email kikock@qq.com
 **/
public class ReceiveTaskAfterListener implements org.activiti.engine.delegate.JavaDelegate {

    @Autowired
    private FlowTaskDao flowTaskDao;

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

    @Override
    public void execute(DelegateExecution delegateTask) throws Exception {
        String eventName = delegateTask.getEventName();
        String deleteReason = ((ExecutionEntity) delegateTask).getDeleteReason();
        if (Constants.END.equalsIgnoreCase(eventName) && StringUtils.isNotEmpty(deleteReason)) {
            return;
        }
        String actProcessInstanceId = delegateTask.getProcessInstanceId();
        ExecutionEntity taskEntity = (ExecutionEntity) delegateTask;
        String actTaskDefKey = delegateTask.getCurrentActivityId();
        String actProcessDefinitionId = delegateTask.getProcessDefinitionId();
        FlowDefVersion flowDefVersion = flowDefVersionDao.findByActDefId(actProcessDefinitionId);
        String flowDefJson = flowDefVersion.getDefJson();
        JSONObject defObj = JSONObject.fromObject(flowDefJson);
        Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
        JSONObject currentNode = definition.getProcess().getNodes().getJSONObject(actTaskDefKey);
        JSONObject normal = currentNode.getJSONObject(Constants.NODE_CONFIG).getJSONObject(Constants.NORMAL);
        if (normal != null) {
            String flowTaskName = (String) normal.get(Constants.NAME);
            FlowTask flowTask = null;
            FlowHistory flowHistory = new FlowHistory();
            List<FlowTask> flowTaskList = flowTaskDao.findByActTaskDefKeyAndActInstanceId(actTaskDefKey, actProcessInstanceId);
            if (flowTaskList != null && !flowTaskList.isEmpty()) {
                flowTask = flowTaskList.get(0);
                flowTaskDao.delete(flowTask);
            }
            if (flowTask != null) {
                BeanUtils.copyProperties(flowTask, flowHistory);
                flowHistory.setId(null);
                flowHistory.setOldTaskId(flowTask.getId());
                flowHistory.setActStartTime(flowTask.getActDueDate());
            } else {
                flowTask = new FlowTask();
                flowHistory.setTaskJsonDef(currentNode.toString());
                flowHistory.setFlowName(definition.getProcess().getName());
                flowHistory.setFlowTaskName(flowTaskName);
                FlowInstance flowInstance = flowInstanceDao.findByActInstanceId(actProcessInstanceId);
                flowHistory.setFlowInstance(flowInstance);
                String ownerName = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getName();
                AppModule appModule = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule();
                if (appModule != null && StringUtils.isNotEmpty(appModule.getName())) {
                    ownerName = appModule.getName();
                }
                flowHistory.setOwnerAccount(Constants.ADMIN);
                flowHistory.setOwnerName(ownerName);
                flowHistory.setExecutorAccount(Constants.ADMIN);
                flowHistory.setExecutorId("");
                flowHistory.setExecutorName(ownerName);
                flowHistory.setCandidateAccount("");
                flowHistory.setActStartTime(new Date());
                flowHistory.setActHistoryId(null);
                flowHistory.setActTaskDefKey(actTaskDefKey);
                flowHistory.setPreId(null);

                BeanUtils.copyProperties(flowHistory, flowTask);
                flowTask.setTaskStatus(TaskStatus.INIT.toString());
            }
            flowHistory.setFlowName(definition.getProcess().getName());
            flowHistory.setFlowTaskName(flowTaskName);
            //接收任务【执行完成】
            flowHistory.setDepict(ContextUtil.getMessage("10046"));
            flowHistory.setTaskStatus(TaskStatus.COMPLETED.toString());
            flowHistory.setActEndTime(new Date());
            flowHistory.setFlowDefId(flowDefVersion.getFlowDefination().getId());

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
        }
    }
}
