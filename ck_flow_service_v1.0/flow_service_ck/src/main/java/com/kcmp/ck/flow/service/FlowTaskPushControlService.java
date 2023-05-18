package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.entity.RelationParam;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.IFlowTaskPushControlService;
import com.kcmp.ck.flow.dao.FlowTaskPushControlDao;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.entity.FlowTaskPush;
import com.kcmp.ck.flow.entity.FlowTaskPushControl;
import com.kcmp.ck.flow.util.TaskStatus;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.vo.ResponseData;
import com.kcmp.ck.flow.common.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kikock
 * 流程推送控制任务服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowTaskPushControlService extends BaseEntityService<FlowTaskPushControl> implements IFlowTaskPushControlService {

    @Autowired
    private FlowTaskPushControlDao flowTaskPushControlDao;

    @Autowired
    private FlowTaskPushService flowTaskPushService;

    @Autowired
    private FlowTaskControlAndPushService flowTaskControlAndPushService;

    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    protected BaseEntityDao<FlowTaskPushControl> getDao() {
        return this.flowTaskPushControlDao;
    }


    /**
     * 通过推送控制表id重新推送当前任务
     * @param pushControlId 推送控制表id
     * @return
     */
    @Override
    public ResponseData pushAgainByControlId(String pushControlId) {
        FlowTaskPushControl flowTaskPushControl = flowTaskPushControlDao.findOne(pushControlId);
        ResponseData responseData;
        if (flowTaskPushControl != null) {
            responseData = this.pushAgainByControl(flowTaskPushControl);
        } else {
            responseData = ResponseData.operationFailure("推送控制表不存在！");
        }
        return responseData;
    }

    /**
     * 通过控制表重新推送任务
     * @param flowTaskPushControl 推送控制表
     * @return
     */
    public ResponseData pushAgainByControl(FlowTaskPushControl flowTaskPushControl) {
        ResponseData responseData = new ResponseData();
        String pushType = flowTaskPushControl.getPushType();
        if (StringUtils.isEmpty(pushType)) {
            responseData = ResponseData.operationFailure("推送类型不能为空！");
            return responseData;
        }
        String PushStatus = flowTaskPushControl.getPushStatus();
        if (StringUtils.isEmpty(PushStatus)) {
            responseData = ResponseData.operationFailure("推送类状态不能为空！");
            return responseData;
        }
        try {
            //得到需要推送的任务集合
            List<FlowTaskPush> pushList = flowTaskControlAndPushService.getChildrenFromParentId(flowTaskPushControl.getId());
            List<FlowTask> taskList = this.copyPushTaskToFlowTask(pushList);
            if (taskList == null || taskList.size() == 0) {
                responseData = ResponseData.operationFailure("推送任务不存在！");
                return responseData;
            }
            responseData = this.pushAgainByTypeAndStatus(pushType, PushStatus, taskList);
        } catch (Exception e) {
            LogUtil.error("重新推送任务获取参数失败！", e);
            responseData = ResponseData.operationFailure("重新推送获取参数失败，详情请查看日志！");
        } finally {
            return responseData;
        }
    }

    /**
     * 通过类型、状态和流程任务，重新推送
     * @param pushType     推送类型
     * @param pushStatus   推送状态
     * @param flowTaskList 流程任务集合
     * @return
     */
    public ResponseData pushAgainByTypeAndStatus(String pushType, String pushStatus, List<FlowTask> flowTaskList) {
        ResponseData responseData;
        if (Constants.TYPE_BASIC.equals(pushType)) { //推送到basic
            responseData = this.pushAgainToBasic(pushStatus, flowTaskList);
        } else if (Constants.TYPE_BUSINESS.equalsIgnoreCase(pushType)) { //推送到业务模块
            responseData = this.pushAgainToBusiness(pushStatus, flowTaskList);
        } else {
            responseData = ResponseData.operationFailure("推送类型不能识别!");
        }
        return responseData;
    }

    /**
     * 重新推送到业务模块
     * @param pushStatus
     * @param flowTaskList
     * @return
     */
    public ResponseData pushAgainToBusiness(String pushStatus, List<FlowTask> flowTaskList) {
        ResponseData responseData = ResponseData.operationSuccess("推送成功！");
        FlowInstance flowInstance = flowTaskList.get(0).getFlowInstance();
        if (flowInstance == null) {
            responseData = ResponseData.operationFailure("得不到流程实例！");
            return responseData;
        }
        if (Constants.STATUS_BUSINESS_INIT.equals(pushStatus)) {
            flowTaskService.pushTaskToModelOrUrl(flowInstance, flowTaskList, TaskStatus.INIT);
        } else if (Constants.STATUS_BUSINESS_COMPLETED.equals(pushStatus)) {
            flowTaskService.pushTaskToModelOrUrl(flowInstance, flowTaskList, TaskStatus.COMPLETED);
        } else if (Constants.STATUS_BUSINESS_DEDLETE.equals(pushStatus)) {
            flowTaskService.pushTaskToModelOrUrl(flowInstance, flowTaskList, TaskStatus.DELETE);
        } else {
            responseData = ResponseData.operationFailure("推送状态不能识别！");
        }
        return responseData;
    }

    /**
     * 重新推送到basic
     * @return
     */
    public ResponseData pushAgainToBasic(String pushStatus, List<FlowTask> flowTaskList) {
        ResponseData responseData = ResponseData.operationSuccess("推送成功！");
        if (Constants.STATUS_BASIC_NEW.equals(pushStatus)) {//新增待办
            flowTaskService.pushToBasic(flowTaskList, null, null, null);
        } else if (Constants.STATUS_BASIC_OLD.equals(pushStatus)) { //待办转已办
            flowTaskService.pushToBasic(null, flowTaskList, null, null);
        } else if (Constants.STATUS_BASIC_DEL.equals(pushStatus)) { //删除待办
            flowTaskService.pushToBasic(null, null, flowTaskList, null);
        } else if (Constants.STATUS_BASIC_END.equals(pushStatus)) { //归档（终止）
            flowTaskService.pushToBasic(null, null, null, flowTaskList.get(0));
        } else {
            responseData = ResponseData.operationFailure("推送状态不能识别！");
        }
        return responseData;
    }

    /**
     * 将推送任务集合转换成流程任务集合
     * @param pushList 推送任务集合
     * @return
     */
    public List<FlowTask> copyPushTaskToFlowTask(List<FlowTaskPush> pushList) throws Exception {
        List<FlowTask> taskList = new ArrayList<FlowTask>();
        pushList.forEach(push -> {
            FlowTask flowTask = new FlowTask();
            BeanUtils.copyProperties(push, flowTask);
            flowTask.setId(push.getFlowTaskId());
            taskList.add(flowTask);
        });
        return taskList;
    }

    /**
     * 通过流程实例ID、节点ID、推送类型、推送状态得到推送信息表集合
     * @param flowInstanceId 流程实例ID
     * @param nodeId         节点ID
     * @param type           推送类型
     * @param status         推送状态
     * @return
     */
    public List<FlowTaskPushControl> getByInstanceAndNodeAndTypeAndStatus(String flowInstanceId, String nodeId, String type, String status) {
        Search search = new Search();
        search.addFilter(new SearchFilter("flowInstanceId", flowInstanceId));
        search.addFilter(new SearchFilter("flowActTaskDefKey", nodeId));
        search.addFilter(new SearchFilter("pushType", type));
        search.addFilter(new SearchFilter("pushStatus", status));
        return flowTaskPushControlDao.findByFilters(search);
    }

    /**
     * 建立新的推送信息表和任务表（以及关联关系）
     * @param type     推送类型
     * @param status   推送状态
     * @param url      推送url
     * @param success  是否推送成功
     * @param taskList 推送的任务集合
     * @return
     */
    public void saveNewControlInfo(String type, String status, String url, Boolean success, List<FlowTask> taskList) throws Exception {
        //保存推送信息父表
        FlowTaskPushControl control = this.saveBeanByFlowTask(type, status, url, success, taskList);
        //保存推送任务集合
        List<FlowTaskPush> pushTaskList = flowTaskPushService.saveListByFlowTaskList(taskList);
        //推送任务ID集合
        List<String> pushIdList = new ArrayList<String>();
        pushTaskList.forEach(a -> {
            pushIdList.add(a.getId());
        });
        //创建关联关系
        RelationParam relationParam = new RelationParam();
        relationParam.setParentId(control.getId());
        relationParam.setChildIds(pushIdList);
        flowTaskControlAndPushService.insertRelationsByParam(relationParam);
    }

    /**
     * 修改的推送信息表和任务表（以及关联关系）
     * @param url                 推送url
     * @param success             是否推送成功
     * @param taskList            推送的任务集合
     * @param flowTaskPushControl 需要更新的推送父表
     * @return
     */
    public void updateControlAndPush(String url, Boolean success, List<FlowTask> taskList, FlowTaskPushControl flowTaskPushControl) throws Exception {
        //更新推送信息父表
        FlowTaskPushControl control = this.updateBeanByFlowTask(flowTaskPushControl, url, success, taskList);
        List<FlowTaskPush> pushList = flowTaskControlAndPushService.getChildrenFromParentId(control.getId());
        //更新推送任务集合
        pushList = flowTaskPushService.updateListByFlowTaskList(taskList, pushList);
        //推送任务ID集合
        List<String> pushIdList = new ArrayList<String>();
        pushList.forEach(a -> {
            pushIdList.add(a.getId());
        });
        //创建关联关系
        RelationParam relationParam = new RelationParam();
        relationParam.setParentId(control.getId());
        relationParam.setChildIds(pushIdList);
        flowTaskControlAndPushService.insertRelationsByParam(relationParam);
    }

    /**
     * 更新或新建推送信息表和任务表（以及关联关系）
     * @param type        推送类型
     * @param status      推送状态
     * @param url         推送url
     * @param success     是否推送成功
     * @param taskList    推送的任务集合
     * @param controlList 已有的推送父表
     * @throws Exception
     */
    public void updateOldControlInfo(String type, String status, String url, Boolean success,
                                     List<FlowTask> taskList, List<FlowTaskPushControl> controlList) throws Exception {
        //是否更新了数据
        Boolean updateInfo = false;
        for (int i = 0; i < controlList.size(); i++) {
            List<FlowTaskPush> pushList = flowTaskControlAndPushService.getChildrenFromParentId(controlList.get(i).getId());
            int sameNumber = 0;
            if (pushList.size() == taskList.size()) {
                for (int k = 0; k < taskList.size(); k++) {
                    Boolean boo = this.ifInclude(pushList, taskList.get(k));
                    if (!boo) {
                        break;
                    }
                    sameNumber++;
                }

                //说明已经推送过
                if (sameNumber == taskList.size()) {
                    //更新推送信息
                    this.updateControlAndPush(url, success, taskList, controlList.get(i));
                    updateInfo = true;
                    break;
                }
            } else {
                continue;
            }
        }

        //表示当前推送信息需要新建
        if (updateInfo == false) {
            //新建推送任务父表和任务列表
            this.saveNewControlInfo(type, status, url, success, taskList);
        }
    }

    /**
     * 判断推送任务列表中是否存在该任务
     * @param list
     * @param flowTask
     * @return
     */
    public Boolean ifInclude(List<FlowTaskPush> list, FlowTask flowTask) {
        for (int i = 0; i < list.size(); i++) {
            //要判断执行人相同是因为（工作池任务最开始是匿名用户，指定用户后直接改的执行人）
            if (list.get(i).getFlowTaskId().equals(flowTask.getId()) && list.get(i).getExecutorId().equals(flowTask.getExecutorId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过流程任务构建推送信息父表
     * @param taskList 推送任务集合
     * @return
     */
    public FlowTaskPushControl saveBeanByFlowTask(String type, String status, String url, Boolean success, List<FlowTask> taskList) throws Exception {
        FlowTask flowTask = taskList.get(0); //因为任务都是同类型节点推送，所以基本信息一致，默认取第一个任务进行基本信息确认
        FlowTaskPushControl control = new FlowTaskPushControl();
        control.setAppModuleId(flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getId());
        control.setBusinessModelId(flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getId());
        control.setFlowTypeId(flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getId());
        control.setFlowInstanceId(flowTask.getFlowInstance().getId());
        control.setFlowInstanceName(flowTask.getFlowInstance().getFlowName());
        control.setFlowActTaskDefKey(flowTask.getActTaskDefKey());
        control.setFlowTaskName(flowTask.getTaskName());
        control.setBusinessId(flowTask.getFlowInstance().getBusinessId());
        control.setBusinessCode(flowTask.getFlowInstance().getBusinessCode());
        //执行人名称字段
        List<String> nameList = new ArrayList<String>();
        taskList.forEach(a -> nameList.add(a.getExecutorName()));
        String nameListString = nameList.toString();
        nameListString = nameListString.substring(1, nameListString.length() - 1);
        control.setExecutorNameList(nameListString);
        control.setPushType(type);
        control.setPushStatus(status);
        control.setPushUrl(url);
        control.setPushNumber(1);
        if (success) {
            control.setPushSuccess(1);
            control.setPushFalse(0);
        } else {
            control.setPushSuccess(0);
            control.setPushFalse(1);
        }
        Date date = new Date();
        control.setPushStartDate(date);
        control.setPushEndDate(date);
        control.setTenantCode(ContextUtil.getTenantCode());
        flowTaskPushControlDao.save(control);
        return control;
    }

    /**
     * 修改图送信息父表
     * @param control
     * @param url
     * @param success
     * @param taskList
     * @return
     * @throws Exception
     */
    public FlowTaskPushControl updateBeanByFlowTask(FlowTaskPushControl control, String url, Boolean success, List<FlowTask> taskList) throws Exception {
        //执行人名称字段
        List<String> nameList = new ArrayList<String>();
        taskList.forEach(a -> nameList.add(a.getExecutorName()));
        String nameListString = nameList.toString();
        nameListString = nameListString.substring(1, nameListString.length() - 1);
        control.setExecutorNameList(nameListString);
        control.setPushUrl(url);
        control.setPushNumber(control.getPushNumber() + 1);
        if (success) {
            control.setPushSuccess(control.getPushSuccess() + 1);
        } else {
            control.setPushFalse(control.getPushFalse() + 1);
        }
        Date date = new Date();
        control.setPushEndDate(date);
        control.setTenantCode(ContextUtil.getTenantCode());
        flowTaskPushControlDao.save(control);
        return control;
    }
}
