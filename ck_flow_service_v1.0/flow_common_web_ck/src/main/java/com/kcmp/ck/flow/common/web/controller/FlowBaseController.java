package com.kcmp.ck.flow.common.web.controller;

import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.ck.annotation.IgnoreCheckAuth;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.*;
import com.kcmp.ck.flow.api.IDefaultBusinessModel2Service;
import com.kcmp.ck.flow.api.IDefaultBusinessModel3Service;
import com.kcmp.ck.flow.api.IDefaultBusinessModelService;
import com.kcmp.ck.flow.api.IFlowDefinationService;
import com.kcmp.ck.flow.api.IFlowSolidifyExecutorService;
import com.kcmp.ck.flow.api.IFlowTaskService;
import com.kcmp.ck.flow.common.web.vo.OperateStatus;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.vo.*;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import com.kcmp.ck.flow.entity.DefaultBusinessModel;
import com.kcmp.ck.flow.entity.DefaultBusinessModel2;
import com.kcmp.ck.flow.entity.DefaultBusinessModel3;
import com.kcmp.ck.flow.entity.FlowDefination;
import com.kcmp.ck.flow.entity.IBusinessFlowEntity;
import com.kcmp.ck.flow.vo.ApprovalHeaderVO;
import com.kcmp.ck.flow.vo.FlowStartResultVO;
import com.kcmp.ck.flow.vo.FlowStartVO;
import com.kcmp.ck.flow.vo.FlowTaskCompleteVO;
import com.kcmp.ck.flow.vo.FlowTaskCompleteWebVO;
import com.kcmp.ck.flow.vo.NodeInfo;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by kikock
 * 流程Controller抽象类
 * @author kikock
 * @email kikock@qq.com
 **/
@IgnoreCheckAuth
public abstract class FlowBaseController<V extends BaseEntity> extends BaseEntityController<V> {

    public FlowBaseController() {
    }

    /**
     * 通过流程定义key启动流程
     * @param businessModelCode
     * @return 操作结果
     */
    @RequestMapping(value = "startFlow")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus startFlow(String businessModelCode, String businessKey, String opinion,
                                   String typeId, String flowDefKey, String taskList, String anonymousNodeId) throws NoSuchMethodException, SecurityException {
        OperateStatus operateStatus = null;
        List<FlowTaskCompleteWebVO> flowTaskCompleteList = null;
        IFlowDefinationService proxy = ApiClient.createProxy(IFlowDefinationService.class);
        Map<String, Object> userMap = new HashMap<String, Object>();//UserTask_1_Normal
        FlowStartVO flowStartVO = new FlowStartVO();
        flowStartVO.setBusinessKey(businessKey);
        flowStartVO.setBusinessModelCode(businessModelCode);
        flowStartVO.setFlowTypeId(typeId);
        flowStartVO.setFlowDefKey(flowDefKey);
        Map<String, Object> variables = new HashMap<String, Object>();
        flowStartVO.setVariables(variables);
        if (StringUtils.isNotEmpty(taskList)) {
            variables.put("additionRemark",opinion);  //启动时的附加说明
            if ("anonymous".equalsIgnoreCase(taskList)) {
                flowStartVO.setPoolTask(true);
                userMap.put("anonymous", "anonymous");
                Map<String, List<String>> selectedNodesUserMap = new HashMap<>();//选择的用户信息
                List<String> userList = new ArrayList<String>();
                selectedNodesUserMap.put(anonymousNodeId, userList);
                variables.put("selectedNodesUserMap", selectedNodesUserMap);
            } else {
                JSONArray jsonArray = JSONArray.fromObject(taskList);//把String转换为json
                flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) JSONArray.toCollection(jsonArray, FlowTaskCompleteWebVO.class);
                if (flowTaskCompleteList != null && !flowTaskCompleteList.isEmpty()) {
                    //如果是固化流程的启动，设置参数里面的紧急状态和执行人列表
                    FlowTaskCompleteWebVO firstBean = flowTaskCompleteList.get(0);
                    if (firstBean.getSolidifyFlow() != null && firstBean.getSolidifyFlow() == true && StringUtils.isEmpty(firstBean.getUserIds())) {
                        IFlowSolidifyExecutorService solidifyProxy = ApiClient.createProxy(IFlowSolidifyExecutorService.class);
                        ResponseData solidifyData = solidifyProxy.setInstancyAndIdsByTaskList(flowTaskCompleteList, businessKey);
                        if (solidifyData.getSuccess() == false) {
                            operateStatus = new OperateStatus(false, solidifyData.getMessage());
                            return operateStatus;
                        }
                        flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) solidifyData.getData();
                        JSONArray jsonArray2 = JSONArray.fromObject(flowTaskCompleteList.toArray());
                        flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) JSONArray.toCollection(jsonArray2, FlowTaskCompleteWebVO.class);
                    }

                    Map<String, Boolean> allowChooseInstancyMap = new HashMap<>();//选择任务的紧急处理状态
                    Map<String, List<String>> selectedNodesUserMap = new HashMap<>();//选择的用户信息
                    for (FlowTaskCompleteWebVO f : flowTaskCompleteList) {
                        String flowTaskType = f.getFlowTaskType();
                        allowChooseInstancyMap.put(f.getNodeId(), f.getInstancyStatus());
                        String userIds = f.getUserIds()==null?"":f.getUserIds();
                        String[] idArray = userIds.split(",");
                        List<String> userList = Arrays.asList(idArray);
                        if ("common".equalsIgnoreCase(flowTaskType) || "approve".equalsIgnoreCase(flowTaskType)) {
                            userMap.put(f.getUserVarName(), userIds);
                        } else {
                            userMap.put(f.getUserVarName(), userList);
                        }
                        selectedNodesUserMap.put(f.getNodeId(), userList);
                    }
                    variables.put("selectedNodesUserMap", selectedNodesUserMap);
                    variables.put("allowChooseInstancyMap", allowChooseInstancyMap);
                }
            }
        }

        flowStartVO.setUserMap(userMap);
        OperateResultWithData<FlowStartResultVO> operateResultWithData = proxy.startByVO(flowStartVO);
        if (operateResultWithData.successful()) {
            FlowStartResultVO flowStartResultVO = operateResultWithData.getData();
            if (flowStartResultVO != null) {
                if (flowStartResultVO.getCheckStartResult()) {
                    new Thread(new Runnable() {//检测待办是否自动执行
                        @Override
                        public void run() {
                            IFlowSolidifyExecutorService SolidifyService = ApiClient.createProxy(IFlowSolidifyExecutorService.class);
                            SolidifyService.selfMotionExecuteTask(businessKey);
                        }
                    }).start();
                    operateStatus = new OperateStatus(true, "成功");
                    operateStatus.setData(flowStartResultVO);
                } else {
                    operateStatus = new OperateStatus(false, "启动流程失败,启动检查服务返回false!");
                }
            } else {
                operateStatus = new OperateStatus(false, "启动流程失败");
            }
        } else {
            operateStatus = new OperateStatus(false, operateResultWithData.getMessage());
        }
        return operateStatus;
    }


    /**
     * 签收任务
     * @param taskId 任务id
     * @return
     */
    @RequestMapping(value = "listFlowTask")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus claimTask(String taskId) {
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        String userId = ContextUtil.getUserId();
        OperateResult result = proxy.claim(taskId, userId);
        OperateStatus operateStatus = new OperateStatus(result.successful(), result.getMessage());
        return operateStatus;
    }

    /**
     * 完成任务
     * @param taskId        任务id
     * @param businessId    业务表单ID
     * @param opinion       审批意见
     * @param taskList      任务完成传输对象
     * @param
     * @return 操作结果
     */
    @RequestMapping(value = "completeTask")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus completeTask(String taskId, String businessId, String opinion, String taskList, String endEventId, boolean manualSelected, String approved, Long loadOverTime) throws Exception {
        OperateStatus operateStatus = null;
        List<FlowTaskCompleteWebVO> flowTaskCompleteList = null;
        if (StringUtils.isNotEmpty(taskList)) {
            JSONArray jsonArray = JSONArray.fromObject(taskList);//把String转换为json
            flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) JSONArray.toCollection(jsonArray, FlowTaskCompleteWebVO.class);
        }
        FlowTaskCompleteVO flowTaskCompleteVO = new FlowTaskCompleteVO();
        flowTaskCompleteVO.setTaskId(taskId);
        flowTaskCompleteVO.setOpinion(opinion);
        Map<String, String> selectedNodesMap = new HashMap<>();
        Map<String, Object> v = new HashMap<String, Object>();
        if (flowTaskCompleteList != null && !flowTaskCompleteList.isEmpty()) {
            //如果是固化流程提交，设置参数里面的紧急状态和执行人列表
            FlowTaskCompleteWebVO firstBean = flowTaskCompleteList.get(0);
            if (firstBean.getSolidifyFlow() != null && firstBean.getSolidifyFlow() == true
                    && StringUtils.isEmpty(firstBean.getUserIds())) {
                IFlowSolidifyExecutorService solidifyProxy = ApiClient.createProxy(IFlowSolidifyExecutorService.class);
                ResponseData solidifyData = solidifyProxy.setInstancyAndIdsByTaskList(flowTaskCompleteList, businessId);
                if (solidifyData.getSuccess() == false) {
                    operateStatus = new OperateStatus(false, solidifyData.getMessage());
                    return operateStatus;
                }
                flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) solidifyData.getData();
                JSONArray jsonArray2 = JSONArray.fromObject(flowTaskCompleteList.toArray());
                flowTaskCompleteList = (List<FlowTaskCompleteWebVO>) JSONArray.toCollection(jsonArray2, FlowTaskCompleteWebVO.class);
                v.put("manageSolidifyFlow", true); //需要维护固化表
            } else {
                v.put("manageSolidifyFlow", false);
            }

            Map<String, Boolean> allowChooseInstancyMap = new HashMap<>();//选择任务的紧急处理状态
            Map<String, List<String>> selectedNodesUserMap = new HashMap<>();//选择的用户信息
            for (FlowTaskCompleteWebVO f : flowTaskCompleteList) {
                allowChooseInstancyMap.put(f.getNodeId(), f.getInstancyStatus());
                String flowTaskType = f.getFlowTaskType();
                String callActivityPath = f.getCallActivityPath();
                List<String> userList = new ArrayList<String>();
                if (StringUtils.isNotEmpty(callActivityPath)) {
//                        Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath,true);
                    selectedNodesMap.put(callActivityPath, f.getNodeId());
                    List<String> userVarNameList = (List) v.get(callActivityPath + "_sonProcessSelectNodeUserV");
                    if (userVarNameList != null) {
                        userVarNameList.add(f.getUserVarName());
                    } else {
                        userVarNameList = new ArrayList<>();
                        userVarNameList.add(f.getUserVarName());
                        v.put(callActivityPath + "_sonProcessSelectNodeUserV", userVarNameList);//选择的变量名,子流程存在选择了多个的情况
                    }
                    String userIds = f.getUserIds()==null?"":f.getUserIds();
                    if ("common".equalsIgnoreCase(flowTaskType) || "approve".equalsIgnoreCase(flowTaskType)) {
                        v.put(callActivityPath + "/" + f.getUserVarName(), userIds);
                    } else {
                        String[] idArray = userIds.split(",");
                        v.put(callActivityPath + "/" + f.getUserVarName(), Arrays.asList(idArray));
                    }
                    //注意：针对子流程选择的用户信息-待后续进行扩展--------------------------
                } else {
                    String userIds = f.getUserIds()==null?"":f.getUserIds();
                    selectedNodesMap.put(f.getNodeId(), f.getNodeId());
                    String[] idArray = userIds.split(",");
                    userList = Arrays.asList(idArray);
                    if ("common".equalsIgnoreCase(flowTaskType) || "approve".equalsIgnoreCase(flowTaskType)) {
                        v.put(f.getUserVarName(), userIds);
                    } else if (!"poolTask".equalsIgnoreCase(flowTaskType)) {
                        v.put(f.getUserVarName(), userList);
                    }
                }
                selectedNodesUserMap.put(f.getNodeId(), userList);
            }
            v.put("allowChooseInstancyMap", allowChooseInstancyMap);
            v.put("selectedNodesUserMap", selectedNodesUserMap);
        } else {
            if (StringUtils.isNotEmpty(endEventId)) {
                selectedNodesMap.put(endEventId, endEventId);
            }
            Map<String, Boolean> allowChooseInstancyMap = new HashMap<>();//选择任务的紧急处理状态
            Map<String, List<String>> selectedNodesUserMap = new HashMap<>();//选择的用户信息
            v.put("selectedNodesUserMap", selectedNodesUserMap);
            v.put("allowChooseInstancyMap", allowChooseInstancyMap);
            v.put("manageSolidifyFlow", false);//会签未完成和结束节点不需要维护固化流程执行人列表
        }
        if (manualSelected) {
            flowTaskCompleteVO.setManualSelectedNode(selectedNodesMap);
        }
        if (loadOverTime != null) {
            v.put("loadOverTime", loadOverTime);
        }
        v.put("approved", approved);//针对会签时同意、不同意、弃权等操作
        flowTaskCompleteVO.setVariables(v);
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        OperateResultWithData<FlowStatus> operateResult = proxy.complete(flowTaskCompleteVO);
        if (operateResult.successful() && StringUtils.isEmpty(endEventId)) { //处理成功并且不是结束节点调用
            new Thread(new Runnable() {//检测待办是否自动执行
                @Override
                public void run() {
                    IFlowSolidifyExecutorService SolidifyService = ApiClient.createProxy(IFlowSolidifyExecutorService.class);
                    SolidifyService.selfMotionExecuteTask(businessId);
                }
            }).start();
        }
        operateStatus = new OperateStatus(operateResult.successful(), operateResult.getMessage());
        return operateStatus;
    }

    /**
     * 回退（撤销）任务
     * @param preTaskId 上一个任务ID
     * @param opinion   意见
     * @return 操作结果
     */
    @RequestMapping(value = "cancelTask")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus rollBackTo(String preTaskId, String opinion) throws CloneNotSupportedException {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        OperateResult result = proxy.rollBackTo(preTaskId, opinion);
        operateStatus = new OperateStatus(result.successful(), result.getMessage());
        return operateStatus;
    }

    /**
     * 任务驳回
     * @param taskId  任务ID
     * @param opinion 意见
     * @return 操作结果
     */
    @RequestMapping(value = "rejectTask")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus rejectTask(String taskId, String opinion) throws Exception {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        OperateResult result = proxy.taskReject(taskId, opinion, null);
        operateStatus = new OperateStatus(result.successful(), result.getMessage());
        return operateStatus;
    }


    /**
     * 获取当前审批任务的决策信息
     * @param taskId
     * @return 操作结果
     */
    @RequestMapping(value = "nextNodesInfo")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus nextNodesInfo(String taskId) throws NoSuchMethodException {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        List<NodeInfo> nodeInfoList = proxy.findNextNodes(taskId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            operateStatus = new OperateStatus(true, "成功");
            operateStatus.setData(nodeInfoList);
        } else {
            operateStatus = new OperateStatus(false, "任务不存在，可能已经被处理");
        }
        return operateStatus;
    }

    /**
     * 获取下一步的节点信息任务
     * @param taskId    任务id
     * @return 操作结果
     */
    @RequestMapping(value = "getSelectedNodesInfo")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus getSelectedNodesInfo(String taskId, String approved, String includeNodeIdsStr) throws NoSuchMethodException {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        List<String> includeNodeIds = null;
        if (StringUtils.isNotEmpty(includeNodeIdsStr)) {
            String[] includeNodeIdsStringArray = includeNodeIdsStr.split(",");
            includeNodeIds = Arrays.asList(includeNodeIdsStringArray);
        }
        if (StringUtils.isEmpty(approved)) {
            approved = "APPROVED";
        }
        List<NodeInfo> nodeInfoList = proxy.findNexNodesWithUserSet(taskId, approved, includeNodeIds);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            operateStatus = new OperateStatus(true, "成功");
            if (nodeInfoList.size() == 1 && "EndEvent".equalsIgnoreCase(nodeInfoList.get(0).getType())) {//只存在结束节点
                operateStatus.setData("EndEvent");
            } else if (nodeInfoList.size() == 1 && "CounterSignNotEnd".equalsIgnoreCase(nodeInfoList.get(0).getType())) {
                operateStatus.setData("CounterSignNotEnd");
            } else {
                operateStatus.setData(nodeInfoList);
            }
        } else {
            operateStatus = new OperateStatus(false, "任务不存在，可能已经被处理");
        }
        return operateStatus;
    }

    /**
     * 获取下一步的节点信息任务(带用户信息)
     * @param taskId
     * @return 操作结果
     */
    @RequestMapping(value = "nextNodesInfoWithUser")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus nextNodesInfoWithUser(String taskId) throws NoSuchMethodException {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        List<NodeInfo> nodeInfoList = proxy.findNexNodesWithUserSet(taskId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            operateStatus = new OperateStatus(true, "成功");
            operateStatus.setData(nodeInfoList);
        } else {
            operateStatus = new OperateStatus(false, "任务不存在，可能已经被处理");
        }
        return operateStatus;
    }

    /**
     * 获取任务抬头信息信息任务
     * @param taskId
     * @return 操作结果
     */
    @RequestMapping(value = "getApprovalHeaderInfo")
    @ResponseBody
    @IgnoreCheckAuth
    public OperateStatus getApprovalHeaderInfo(String taskId) {
        OperateStatus operateStatus = null;
        IFlowTaskService proxy = ApiClient.createProxy(IFlowTaskService.class);
        ApprovalHeaderVO approvalHeaderVO = proxy.getApprovalHeaderVO(taskId);
        if (approvalHeaderVO != null) {
            operateStatus = new OperateStatus(true, "成功");
            operateStatus.setData(approvalHeaderVO);
        } else {
            operateStatus = new OperateStatus(false, "任务不存在，可能已经被处理");
        }
        return operateStatus;
    }


    /**
     * 仅针对跨业务实体子任务的测试初始化方法
     * @param defaultBusinessModelList
     * @param defaultBusinessModel2List
     * @param defaultBusinessModel3List
     * @param flowStatus
     */
    protected void initCallActivityBusinessStatus(List<DefaultBusinessModel> defaultBusinessModelList, List<DefaultBusinessModel2> defaultBusinessModel2List, List<DefaultBusinessModel3> defaultBusinessModel3List, FlowStatus flowStatus) {
        IDefaultBusinessModelService defaultBusinessModelService = ApiClient.createProxy(IDefaultBusinessModelService.class);
        IDefaultBusinessModel2Service defaultBusinessModel2Service = ApiClient.createProxy(IDefaultBusinessModel2Service.class);
        IDefaultBusinessModel3Service defaultBusinessModel3Service = ApiClient.createProxy(IDefaultBusinessModel3Service.class);
        if (!defaultBusinessModelList.isEmpty()) {
            for (DefaultBusinessModel defaultBusinessModel : defaultBusinessModelList) {
                defaultBusinessModel.setFlowStatus(flowStatus);
                defaultBusinessModelService.save(defaultBusinessModel);
            }
        }
        if (!defaultBusinessModel2List.isEmpty()) {
            for (DefaultBusinessModel2 defaultBusinessModel2 : defaultBusinessModel2List) {
                defaultBusinessModel2.setFlowStatus(flowStatus);
                defaultBusinessModel2Service.save(defaultBusinessModel2);
            }
        }
        if (!defaultBusinessModel3List.isEmpty()) {
            for (DefaultBusinessModel3 defaultBusinessModel3 : defaultBusinessModel3List) {
                defaultBusinessModel3.setFlowStatus(flowStatus);
                defaultBusinessModel3Service.save(defaultBusinessModel3);
            }
        }
    }

    /**
     * 仅针对跨业务实体子任务的测试初始化方法
     * @param defaultBusinessModelList
     * @param defaultBusinessModel2List
     * @param defaultBusinessModel3List
     * @param callActivityPathMap
     * @param variables
     * @param parentBusinessModel
     */
    protected void initCallActivityBusiness(List<DefaultBusinessModel> defaultBusinessModelList, List<DefaultBusinessModel2> defaultBusinessModel2List, List<DefaultBusinessModel3> defaultBusinessModel3List, Map<String, String> callActivityPathMap, Map<String, Object> variables, IBusinessFlowEntity parentBusinessModel) {
        IDefaultBusinessModelService defaultBusinessModelService = ApiClient.createProxy(IDefaultBusinessModelService.class);
        IDefaultBusinessModel2Service defaultBusinessModel2Service = ApiClient.createProxy(IDefaultBusinessModel2Service.class);
        IDefaultBusinessModel3Service defaultBusinessModel3Service = ApiClient.createProxy(IDefaultBusinessModel3Service.class);

        IFlowDefinationService flowDefinationService = ApiClient.createProxy(IFlowDefinationService.class);

        for (Map.Entry<String, String> entry : callActivityPathMap.entrySet()) {
            String realDefiniationKey = entry.getValue();
            String realPathKey = entry.getKey();
            FlowDefination flowDefination = flowDefinationService.findByKey(realDefiniationKey);
            String sonBusinessModelCode = flowDefination.getFlowType().getBusinessModel().getClassName();
            if ("com.kcmp.ck.flow.entity.DefaultBusinessModel".equals(sonBusinessModelCode)) {
                DefaultBusinessModel defaultBusinessModel = new DefaultBusinessModel();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel);
                String name = "temp_测试跨业务实体子流程_默认业务实体" + System.currentTimeMillis();
                defaultBusinessModel.setName(name);
                defaultBusinessModel.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel.setId(null);
                defaultBusinessModel.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel> resultWithData = defaultBusinessModelService.save(defaultBusinessModel);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel = resultWithData.getData();
                defaultBusinessModelList.add(defaultBusinessModel);
            } else if ("com.kcmp.ck.flow.entity.DefaultBusinessModel2".equals(sonBusinessModelCode)) {
                DefaultBusinessModel2 defaultBusinessModel2Son = new DefaultBusinessModel2();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel2Son);
                String name = "temp_测试跨业务实体子流程_采购实体" + System.currentTimeMillis();
                defaultBusinessModel2Son.setName(name);
                defaultBusinessModel2Son.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel2Son.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel2Son.setId(null);
                defaultBusinessModel2Son.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel2> resultWithData = defaultBusinessModel2Service.save(defaultBusinessModel2Son);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel2Son = resultWithData.getData();
                defaultBusinessModel2List.add(defaultBusinessModel2Son);
            } else if ("com.kcmp.ck.flow.entity.DefaultBusinessModel3".equals(sonBusinessModelCode)) {
                DefaultBusinessModel3 defaultBusinessModel3Son = new DefaultBusinessModel3();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel3Son);
                String name = "temp_测试跨业务实体子流程_销售实体" + System.currentTimeMillis();
                defaultBusinessModel3Son.setName(name);
                defaultBusinessModel3Son.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel3Son.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel3Son.setId(null);
                defaultBusinessModel3Son.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel3> resultWithData = defaultBusinessModel3Service.save(defaultBusinessModel3Son);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel3Son = resultWithData.getData();
                defaultBusinessModel3List.add(defaultBusinessModel3Son);
            }
        }
    }

    /**
     * 解析子流程绝对路径
     * @param callActivityPath 路径值
     * @return 路径值为key，子流程id为value的MAP对象
     */
    protected Map<String, String> initCallActivtiy(String callActivityPath, boolean ifStart) {
        Map<String, String> resultMap = new HashMap<String, String>();
        //  String str ="/caigouTestZhu/CallActivity_3/yewushengqing2";
        String str = callActivityPath;
        String[] resultArray = str.split("/");
        if ((resultArray.length < 4) || (resultArray.length % 2 != 0)) {
            throw new RuntimeException("子流程路径解析错误");
        }
        List<String> resultList = new ArrayList<String>();
        for (int i = 1; i < resultArray.length; i++) {
            resultList.add(resultArray[i]);
        }
        int size = resultList.size();
        for (int j = 1; j < size; ) {
            String key = resultList.get(size - j);
            int endIndex = str.lastIndexOf(key) + key.length();
            String path = str.substring(0, endIndex);
            resultMap.put(path, key);
            j += 2;
            if (!ifStart) {
                break;//只生成一条测试数据
            }
        }
        return resultMap;
    }
}

