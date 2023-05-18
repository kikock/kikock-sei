package com.kcmp.ck.flow.util;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.dao.FlowServiceUrlDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.service.FlowTaskService;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.flow.vo.NodeInfo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import com.kcmp.ck.flow.entity.FlowTask;
import net.sf.json.JSONObject;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kikock
 * 流程监听工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowListenerTool {

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    FlowHistoryDao flowHistoryDao;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowTaskTool flowTaskTool;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private FlowServiceUrlDao flowServiceUrlDao;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    private final Logger logger = LoggerFactory.getLogger(FlowListenerTool.class);


    /**
     * 获取下一步节点信息
     * @param flowTask          流程任务
     * @param delegateTask      委托任务
     * @param isSolidifyFlow    是否是固化流程
     * @return
     * @throws NoSuchMethodException
     */
    public List<NodeInfo> nextNodeInfoList(FlowTask flowTask, DelegateExecution delegateTask, Boolean isSolidifyFlow) throws NoSuchMethodException {
        List<NodeInfo> nodeInfoList;
        //固化流程的执行人是从固化表中获取
        if (isSolidifyFlow) {
            nodeInfoList = flowTaskService.findNexNodesWithUserSetSolidifyFlow(flowTask);
        } else {
            nodeInfoList = flowTaskService.findNexNodesWithUserSet(flowTask);
        }
        List<NodeInfo> results = null;
        results = nodeInfoList;
        FlowInstance parentFlowInstance = flowTask.getFlowInstance().getParent();
        FlowTask flowTaskTempSrc = new FlowTask();
        org.springframework.beans.BeanUtils.copyProperties(flowTask, flowTaskTempSrc);
        //针对子流程结束，循环向上查找父任务下一步的节点执行人信息
        ProcessInstance instanceSon = ((ExecutionEntity) delegateTask).getProcessInstance();
        //针对子流程结束节点
        while (instanceSon != null && parentFlowInstance != null && nodeInfoList != null && !nodeInfoList.isEmpty() && nodeInfoList.size() == 1 && "EndEvent".equalsIgnoreCase(nodeInfoList.get(0).getType())) {
            FlowTask flowTaskTemp = new FlowTask();
            org.springframework.beans.BeanUtils.copyProperties(flowTaskTempSrc, flowTaskTemp);
            flowTaskTemp.setFlowInstance(parentFlowInstance);
            // 取得父流程实例
            ExecutionEntity superExecution = instanceSon.getSuperExecution();
            if (superExecution != null) {
                String activityId = superExecution.getActivityId();
                flowTaskTemp.setActTaskKey(activityId);
                flowTaskTemp.setActTaskDefKey(activityId);
                String flowDefJsonP = parentFlowInstance.getFlowDefVersion().getDefJson();
                JSONObject defObjP = JSONObject.fromObject(flowDefJsonP);
                Definition definitionP = (Definition) JSONObject.toBean(defObjP, Definition.class);
                JSONObject currentNodeP = definitionP.getProcess().getNodes().getJSONObject(activityId);
                flowTaskTemp.setTaskJsonDef(currentNodeP.toString());
                results = flowTaskService.findNexNodesWithUserSet(flowTaskTemp);
            }
            parentFlowInstance = parentFlowInstance.getParent();
            nodeInfoList = results;
            flowTaskTempSrc = flowTaskTemp;
            instanceSon = superExecution.getProcessInstance();
        }
        return results;
    }

    public List<String> getCallActivitySonPaths(FlowTask flowTask) throws NoSuchMethodException {
        List<NodeInfo> nodeInfoList = flowTaskService.findNexNodesWithUserSet(flowTask);
        List<String> paths = new ArrayList<String>();
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (NodeInfo nodeInfo : nodeInfoList) {
                if (StringUtils.isNotEmpty(nodeInfo.getCallActivityPath())) {
                    paths.add(nodeInfo.getCallActivityPath());
                }
            }
        }
        return paths;
    }

    public List<NodeInfo> initNodeUsers(List<NodeInfo> results, DelegateExecution delegateTask, String actTaskDefKey) {
        List<NodeInfo> nextNodes = new ArrayList<NodeInfo>();
        if (results != null && !results.isEmpty()) {
            Map<String, Object> userVarNameMap = new HashMap<>();
            List<String> userVarNameList = null;
            for (NodeInfo nodeInfo : results) {
                if ("EndEvent".equalsIgnoreCase(nodeInfo.getType())) {
                    nodeInfo.setType("EndEvent");
                    continue;
                } else if ("ServiceTask".equalsIgnoreCase(nodeInfo.getType())) {//服务任务也不做处理
                    continue;
                }
                nextNodes.add(nodeInfo);
                String taskType = nodeInfo.getFlowTaskType();
                String uiUserType = nodeInfo.getUiUserType();
                String callActivityPath = nodeInfo.getCallActivityPath();
                String varUserName = nodeInfo.getUserVarName();

                if (StringUtils.isNotEmpty(callActivityPath)) {
                    userVarNameList = (List<String>) userVarNameMap.get(callActivityPath + "_sonProcessSelectNodeUserV");
                    if (userVarNameList == null) {
                        userVarNameList = new ArrayList<String>();
                        //选择的变量名,子流程存在选择了多个的情况
                        userVarNameMap.put(callActivityPath + "_sonProcessSelectNodeUserV", userVarNameList);
                    }
                    userVarNameList.add(varUserName);
                }
                //任意执行人默认规则为当前执行人
                if ("AnyOne".equalsIgnoreCase(uiUserType)) {
                    String currentUserId = ContextUtil.getUserId();
                    Executor executor = flowCommonUtil.getBasicUserExecutor(currentUserId);
                    Set<Executor> employeeSet = new HashSet<Executor>();
                    employeeSet.add(executor);
                    nodeInfo.setExecutorSet(employeeSet);
                }
                if ("SingleSign".equalsIgnoreCase(taskType) || "CounterSign".equalsIgnoreCase(taskType) || "ParallelTask".equalsIgnoreCase(taskType) || "SerialTask".equalsIgnoreCase(taskType)) {
                    Set<Executor> executorSet = nodeInfo.getExecutorSet();
                    if (executorSet != null && !executorSet.isEmpty()) {
                        List<String> userIdArray = new ArrayList<String>();
                        for (Executor executor : executorSet) {
                            userIdArray.add(executor.getId());
                        }
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            userVarNameMap.put(callActivityPath + "/" + varUserName, userIdArray);
                        } else {
                            userVarNameMap.put(varUserName, userIdArray);
                        }
                    }
                } else {
                    Set<Executor> executorSet = nodeInfo.getExecutorSet();
                    if (executorSet != null && !executorSet.isEmpty()) {
                        String userId = ((Executor) executorSet.toArray()[0]).getId();
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            userVarNameMap.put(callActivityPath + "/" + varUserName, userId);
                        } else {
                            userVarNameMap.put(varUserName, userId);
                        }
                    }
                }
            }
//            runtimeService.setVariable(delegateTask.getProcessInstanceId(),actTaskDefKey+"_nextNodeIds",  nextNodes);
            runtimeService.setVariables(delegateTask.getProcessInstanceId(), userVarNameMap);
        }

        return nextNodes;
    }

    public void initNextAllTask(FlowInstance flowInstance, FlowHistory flowHistory) {
        Calendar startTreadTime = Calendar.getInstance();
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Calendar nowTime = Calendar.getInstance();
                nowTime.add(Calendar.MINUTE, -2);//不能超过2分钟
                if (nowTime.after(startTreadTime)) {
                    service.shutdown();
                }
                flowTaskTool.initTask(flowInstance, flowHistory, null, null);
            }
        };
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleWithFixedDelay(runnable, 1, 20, TimeUnit.SECONDS);
    }

    public void initNextAllTask(List<NodeInfo> nextNodes, ExecutionEntity taskEntity, FlowHistory flowHistory) {
        if (!nextNodes.isEmpty()) {
            ExecutionEntity parent = taskEntity.getSuperExecution();
            if (parent != null) {//针对作为子任务的情况
                ExecutionEntity parentTemp = parent;
                ProcessInstance parentProcessInstance = null;
                ExecutionEntity zhuzhongEntity = parentTemp;
                while (parentTemp != null) {
                    parentProcessInstance = parentTemp.getProcessInstance();
                    zhuzhongEntity = parentTemp;
                    parentTemp = ((ExecutionEntity) parentProcessInstance).getSuperExecution();
                }
                FlowInstance flowInstanceZhu = flowInstanceDao.findByActInstanceId(zhuzhongEntity.getProcessInstanceId());
                new Thread(new Runnable() {//异步
                    @Override
                    public void run() {
                        initNextAllTask(flowInstanceZhu, flowHistory);//初始化相关联的所有待办任务
                    }
                }).start();
            } else {
                new Thread(new Runnable() {//异步
                    @Override
                    public void run() {
                        FlowInstance flowInstance = flowHistory.getFlowInstance();
                        initNextAllTask(flowInstance, flowHistory);
                    }
                }).start();
            }
        }
    }

    /**
     * 执行事件后服务
     * @param businessKey
     * @param flowDefVersion
     * @param endSign
     * @param variables
     * @return
     */
    public FlowOperateResult callEndService(String businessKey, FlowDefVersion flowDefVersion, int endSign, Map<String, Object> variables) {
        FlowOperateResult flowOpreateResult = null;
        if (flowDefVersion != null && StringUtils.isNotEmpty(businessKey)) {
            String endCallServiceUrlId = flowDefVersion.getEndCallServiceUrlId();
            Boolean endCallServiceAync = flowDefVersion.getEndCallServiceAync();
            if (StringUtils.isNotEmpty(endCallServiceUrlId)) {
                FlowServiceUrl flowServiceUrl = flowServiceUrlDao.findOne(endCallServiceUrlId);
                String checkUrl = flowServiceUrl.getUrl();
                if (StringUtils.isNotEmpty(checkUrl)) {
                    String apiBaseAddressConfig = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
                    String baseUrl = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                    String endCallServiceUrlPath = baseUrl + checkUrl;
                    FlowInvokeParams flowInvokeParams = new FlowInvokeParams();
                    flowInvokeParams.setId(businessKey);
                    Map<String, String> params = new HashMap<String, String>();
                    if (variables != null) {
                        if (variables.get("approved") != null) {
                            String approved = variables.get("approved") + "";
                            flowInvokeParams.setAgree(Boolean.parseBoolean(approved));
                        }
                        if (variables.get("approveResult") != null) {
                            String approveResult = variables.get("approveResult") + "";
                            flowInvokeParams.setFinalAgree(Boolean.parseBoolean(approveResult));
                        }
                        if (variables.get("opinion") != null) {
                            params.put("opinion", variables.get("opinion") + "");
                        }
                    }
                    params.put("endSign", endSign + "");
                    flowInvokeParams.setParams(params);
                    String msg = "结束后事件【" + flowServiceUrl.getName() + "】";
                    String urlAndData = "-请求地址：" + endCallServiceUrlPath + "，参数：" + JsonUtils.toJson(flowInvokeParams);
                    if (endCallServiceAync != null && endCallServiceAync == true) {
                        //模拟异步
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FlowOperateResult flowOperateResult = ApiClient.postViaProxyReturnResult(endCallServiceUrlPath, new GenericType<FlowOperateResult>() {
                                    }, flowInvokeParams);
                                    if (flowOperateResult == null) {
                                        LogUtil.bizLog(msg + "异步调用返回信息为空!" + urlAndData);
                                    } else if (!flowOperateResult.isSuccess()) {
                                        LogUtil.bizLog(msg + "异步调用返回信息：【" + flowOperateResult.toString() + "】" + urlAndData);
                                    }
                                } catch (Exception e) {
                                    LogUtil.error(msg + "异步调用内部报错!" + urlAndData, e);
                                }
                            }
                        }).start();
                        flowOpreateResult = new FlowOperateResult(true, "事件已异步调用！");
                    } else {
                        try {
                            flowOpreateResult = ApiClient.postViaProxyReturnResult(endCallServiceUrlPath, new GenericType<FlowOperateResult>() {
                            }, flowInvokeParams);
                            if (flowOpreateResult == null) {
                                flowOpreateResult = new FlowOperateResult(false, msg + "返回信息为空！");
                                LogUtil.bizLog(msg + "返回信息为空！" + urlAndData);
                            } else if (!flowOpreateResult.isSuccess()) {
                                LogUtil.bizLog(msg + "返回信息：【" + flowOpreateResult.toString() + "】" + urlAndData);
                                flowOpreateResult.setMessage(msg + "返回信息：【" + flowOpreateResult.getMessage() + "】");
                            }
                        } catch (Exception e) {
                            LogUtil.error(msg + "内部报错!" + urlAndData, e);
                            throw new FlowException(msg + "内部报错，详情请查看日志！");
                        }
                    }

                }
            }
        }
        return flowOpreateResult;
    }

    /**
     * 流程即将结束时调用服务检查，如果失败流程结束失败，同步
     * @param businessKey
     * @param flowDefVersion
     * @return
     */
    public FlowOperateResult callBeforeEnd(String businessKey, FlowDefVersion flowDefVersion, int endSign, Map<String, Object> variables) {
        FlowOperateResult result = null;
        if (flowDefVersion != null && StringUtils.isNotEmpty(businessKey)) {
            String endBeforeCallServiceUrlId = flowDefVersion.getEndBeforeCallServiceUrlId();
            Boolean endBeforeCallServiceAync = flowDefVersion.getEndBeforeCallServiceAync();

            if (StringUtils.isNotEmpty(endBeforeCallServiceUrlId)) {
                FlowServiceUrl flowServiceUrl = flowServiceUrlDao.findOne(endBeforeCallServiceUrlId);
                String checkUrl = flowServiceUrl.getUrl();
                if (StringUtils.isNotEmpty(checkUrl)) {
                    String apiBaseAddressConfig = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
                    String baseUrl = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                    String checkUrlPath = baseUrl + checkUrl;
                    FlowInvokeParams flowInvokeParams = new FlowInvokeParams();
                    flowInvokeParams.setId(businessKey);
                    Map<String, String> params = new HashMap<String, String>();
                    if (variables != null) {
                        if (variables.get("approved") != null) {
                            String approved = variables.get("approved") + "";
                            flowInvokeParams.setAgree(Boolean.parseBoolean(approved));
                        }
                        if (variables.get("approveResult") != null) {
                            String approveResult = variables.get("approveResult") + "";
                            flowInvokeParams.setFinalAgree(Boolean.parseBoolean(approveResult));
                        }
                        if (variables.get("opinion") != null) {
                            params.put("opinion", variables.get("opinion") + "");
                        }

                    }
                    params.put("endSign", endSign + "");
                    flowInvokeParams.setParams(params);
                    String msg = "结束前事件【" + flowServiceUrl.getName() + "】";
                    String urlAndData = "-请求地址：" + checkUrlPath + "，参数：" + JsonUtils.toJson(params);
                    if (endBeforeCallServiceAync != null && endBeforeCallServiceAync == true) {
                        //模拟异步
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FlowOperateResult resultAync = ApiClient.postViaProxyReturnResult(checkUrlPath, new GenericType<FlowOperateResult>() {
                                    }, flowInvokeParams);
                                    if (resultAync == null) {
                                        LogUtil.bizLog(msg + "异步调用返回信息为空!" + urlAndData);
                                    } else if (!resultAync.isSuccess()) {
                                        LogUtil.bizLog(msg + "异步调用返回信息：【" + resultAync.toString() + "】" + urlAndData);
                                    }
                                } catch (Exception e) {
                                    LogUtil.error(msg + "异步调用内部报错!" + urlAndData, e);
                                }
                            }
                        }).start();
                        result = new FlowOperateResult(true, "事件已异步调用！");
                    } else {
                        try {
                            result = ApiClient.postViaProxyReturnResult(checkUrlPath, new GenericType<FlowOperateResult>() {
                            }, flowInvokeParams);
                            if (result == null) {
                                result = new FlowOperateResult(false, msg + "返回信息为空！");
                                LogUtil.bizLog(msg + "返回参数为空!" + urlAndData);
                            } else if (!result.isSuccess()) {
                                LogUtil.bizLog(msg + "返回信息：【" + result.toString() + "】" + urlAndData);
                                result.setMessage(msg + "返回信息：【" + result.getMessage() + "】");
                            }
                        } catch (Exception e) {
                            LogUtil.error(msg + "内部报错!" + urlAndData, e);
                            throw new FlowException(msg + "内部报错，详情请查看日志！");
                        }
                    }
                }
            }
        }
        return result;
    }

    public void taskEventServiceCall(DelegateExecution delegateTask, boolean async, String beforeExcuteServiceId, String businessId) {
        try {
            String multiInstance = (String) ((ExecutionEntity) delegateTask).getActivity().getProperty("multiInstance");
            Boolean isMmultiInstance = StringUtils.isNotEmpty(multiInstance);
            //控制会签任务、串行任务、并行任务 所有执行完成时只触发一次完成事件（可能后续需要扩展控制）
            if (isMmultiInstance) {
                TransitionImpl transiton = ((ExecutionEntity) delegateTask).getTransition();
                if (transiton == null) {
                    return;
                }
            }
            String actProcessDefinitionId = delegateTask.getProcessDefinitionId();
            FlowDefVersion flowDefVersion = flowDefVersionDao.findByActDefId(actProcessDefinitionId);
            String flowDefJson = flowDefVersion.getDefJson();
            JSONObject defObj = JSONObject.fromObject(flowDefJson);
            Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);

            Map<String, Object> tempV = delegateTask.getVariables();
            Map<String, List<String>> selectedNodesUserMap = (Map<String, List<String>>) tempV.get("selectedNodesUserMap");
            Map<String, List<String>> selectedNodesUserMapNew = new HashMap<>();
            //如果配置了自定义code，则进行替换
            if (selectedNodesUserMap != null && !selectedNodesUserMap.isEmpty()) {
                for (Map.Entry<String, List<String>> temp : selectedNodesUserMap.entrySet()) {
                    String actTaskDefKey = temp.getKey();
                    JSONObject currentNode = definition.getProcess().getNodes().getJSONObject(actTaskDefKey);
                    JSONObject normalInfo = currentNode.getJSONObject("nodeConfig").getJSONObject("normal");
                    if (normalInfo != null && !normalInfo.isEmpty()) {
                        String nodeCode = normalInfo.get("nodeCode") != null ? (String) normalInfo.get("nodeCode") : null;
                        if (StringUtils.isEmpty(nodeCode)) {
                            nodeCode = actTaskDefKey;
                        }
                        selectedNodesUserMapNew.put(nodeCode, temp.getValue());
                    }
                }
                tempV.put("selectedNodesUserMap", selectedNodesUserMapNew);
            }
            String param = JsonUtils.toJson(tempV);
            if (async) {
                //模拟异步
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ServiceCallUtil.callService(beforeExcuteServiceId, businessId, param);
                    }
                }).start();
            } else {
                FlowOperateResult flowOperateResult = null;
                String callMessage = null;
                try {
                    flowOperateResult = ServiceCallUtil.callService(beforeExcuteServiceId, businessId, param);
                    callMessage = flowOperateResult.getMessage();
                } catch (Exception e) {
                    callMessage = e.getMessage();
                }

                if ((flowOperateResult == null || !flowOperateResult.isSuccess())) {
                    String actProcessInstanceId = delegateTask.getProcessInstanceId();
                    FlowInstance flowInstance = flowInstanceDao.findByActInstanceId(actProcessInstanceId);
                    List<FlowTask> flowTaskList = flowTaskService.findByInstanceId(flowInstance.getId());
                    List<FlowHistory> flowHistoryList = flowHistoryDao.findByInstanceId(flowInstance.getId());

                    //如果是开始节点，手动回滚
                    if (flowTaskList.isEmpty() && flowHistoryList.isEmpty()) {
                        AppModule appModule = flowDefVersion.getFlowDefination().getFlowType().getBusinessModel().getAppModule();
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
                                        LogUtil.error(e.getMessage(), e);
                                    }
                                    index--;
                                }
                            }
                        }.start();
                    }
                    //抛出异常
                    throw new FlowException(callMessage);
                }
            }
        } catch (Exception e) {
            if (e.getClass() != FlowException.class) {
                LogUtil.error(e.getMessage(), e);
            }
            if (!async) {
                throw e;
            }
        }
    }
}
