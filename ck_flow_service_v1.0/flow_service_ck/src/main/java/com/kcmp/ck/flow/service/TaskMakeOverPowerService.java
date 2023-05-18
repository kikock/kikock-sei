package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.core.ck.dto.SearchOrder;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.enums.UserAuthorityPolicy;
import com.kcmp.ck.flow.api.ITaskMakeOverPowerService;
import com.kcmp.ck.flow.dao.FlowTaskDao;
import com.kcmp.ck.flow.dao.TaskMakeOverPowerDao;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.entity.TaskMakeOverPower;
import com.kcmp.ck.flow.util.TaskStatus;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import com.kcmp.ck.vo.SessionUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kikock
 * 流程任务转授权服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class TaskMakeOverPowerService extends BaseEntityService<TaskMakeOverPower> implements ITaskMakeOverPowerService {

    @Autowired
    private TaskMakeOverPowerDao taskMakeOverPowerDao;

    @Autowired
    private FlowHistoryService flowHistoryService;

    @Autowired
    private FlowTaskDao flowTaskDao;

    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    protected BaseEntityDao<TaskMakeOverPower> getDao() {
        return this.taskMakeOverPowerDao;
    }

    /**
     * 查询自己的转授权单据
     */
    @Override
    public ResponseData findAllByUser() {
        String userId = ContextUtil.getUserId();
        List<TaskMakeOverPower> list;
        SessionUser sessionUser = ContextUtil.getSessionUser();
        UserAuthorityPolicy authorityPolicy = sessionUser.getAuthorityPolicy();
        if (authorityPolicy.equals(UserAuthorityPolicy.TenantAdmin)) {
            list = taskMakeOverPowerDao.findAll();
        } else {
            list = taskMakeOverPowerDao.findListByProperty("userId", userId);
        }
        return ResponseData.operationSuccessWithData(list);
    }

    /**
     * 查询转授权处理历史（自己待办转授权人处理的）
     */
    @Override
    public ResponseData findAllHistoryByUser() {
        String userId = ContextUtil.getUserId();
        SessionUser sessionUser = ContextUtil.getSessionUser();
        UserAuthorityPolicy authorityPolicy = sessionUser.getAuthorityPolicy();
        List<FlowHistory> historylist;
        if (authorityPolicy.equals(UserAuthorityPolicy.TenantAdmin)) {
            historylist = flowHistoryService.findByAllTaskMakeOverPowerHistory();
        } else {
            Search search = new Search();
            search.addFilter(new SearchFilter("ownerId", userId, SearchFilter.Operator.EQ));
            search.addFilter(new SearchFilter("executorId", userId, SearchFilter.Operator.NE));
            search.addSortOrder(SearchOrder.desc("lastEditedDate"));
            historylist = flowHistoryService.findByFilters(search);
        }
        this.initWebUrl(historylist);
        return ResponseData.operationSuccessWithData(historylist);
    }

    private List<FlowHistory> initWebUrl(List<FlowHistory> result) {
        if (result != null && !result.isEmpty()) {
            for (FlowHistory flowHistory : result) {
                FlowInstance flowInstance = flowHistory.getFlowInstance();
                String webBaseAddressConfig = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getWebBaseAddress();
                String webBaseAddress = ContextUtil.getGlobalProperty(webBaseAddressConfig);
                if (StringUtils.isNotEmpty(webBaseAddress)) {
                    flowInstance.setWebBaseAddressAbsolute(webBaseAddress);
                    String[] tempWebBaseAddress = webBaseAddress.split("/");
                    if (tempWebBaseAddress != null && tempWebBaseAddress.length > 0) {
                        webBaseAddress = tempWebBaseAddress[tempWebBaseAddress.length - 1];
                        flowInstance.setWebBaseAddress("/" + webBaseAddress);
                    }
                }

            }
        }
        return result;
    }

    /**
     * 保存一个实体
     * @param entity 实体
     * @return 保存后的实体
     */
    @Override
    @Transactional
    public OperateResultWithData<TaskMakeOverPower> setUserAndsave(TaskMakeOverPower entity) {
        OperateResultWithData<TaskMakeOverPower> resultWithData;

        entity.setUserId(ContextUtil.getUserId());
        entity.setUserAccount(ContextUtil.getUserAccount());
        entity.setUserName(ContextUtil.getUserName());
        //规则检查
        ResponseData responseData = checkOk(entity);
        if (!responseData.getSuccess()) {
            return OperateResultWithData.operationFailure(responseData.getMessage());
        }
        resultWithData = super.save(entity);
        if (resultWithData.getSuccess()) {
            //根据转授权模式处理不同的逻辑
            makeOverPowerTypeToDo(entity);
        }
        return resultWithData;
    }

    /**
     * 通过ID修改启用状态
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResponseData updateOpenStatusById(String id) {
        TaskMakeOverPower bean = taskMakeOverPowerDao.findOne(id);
        if (bean.getOpenStatus() == true) {
            bean.setOpenStatus(false);
            taskMakeOverPowerDao.save(bean);
        } else {
            //生效规则检查
            ResponseData responseData = checkOk(bean);
            if (!responseData.getSuccess()) {
                return OperateResultWithData.operationFailure(responseData.getMessage());
            }
            bean.setOpenStatus(true);
            taskMakeOverPowerDao.save(bean);
            //根据不同转授权模式处理逻辑
            makeOverPowerTypeToDo(bean);
        }
        return ResponseData.operationSuccessWithData(bean);
    }


    /**
     * 检查逻辑是否符合
     * @return
     */
    public ResponseData checkOk(TaskMakeOverPower bean) {
        //统一开始时间和结束时间为日期
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            bean.setPowerStartDate(dateFormat.parse(dateFormat.format(bean.getPowerStartDate())));
            bean.setPowerEndDate(dateFormat.parse(dateFormat.format(bean.getPowerEndDate())));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

        //检查该用户建立的授权信息是否重复
        ResponseData responseData = checkUserRepetition(bean);
        if (!responseData.getSuccess()) {
            return responseData;
        }
        //检查该用户是否在该时间段已经被转授权
        responseData = checkPowerUserRepetition(bean);
        return responseData;
    }


    /**
     * 检查该用户是否被授权
     * @param bean
     * @return
     */
    public ResponseData checkPowerUserRepetition(TaskMakeOverPower bean) {
        //查询该用户所有授权信息（不包含当前数据）
        Search search = new Search();
        search.addFilter(new SearchFilter("powerUserId", bean.getUserId()));
        search.addFilter(new SearchFilter("openStatus", true));
        if (StringUtils.isNotEmpty(bean.getId())) {
            search.addFilter(new SearchFilter("id", bean.getId(), SearchFilter.Operator.NE));
        }
        List<TaskMakeOverPower> list = taskMakeOverPowerDao.findByFilters(search);
        Date startDate = bean.getPowerStartDate();
        Date endDate = bean.getPowerEndDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TaskMakeOverPower a = list.get(i);
                Date start = a.getPowerStartDate();
                Date end = a.getPowerEndDate();
                if (((startDate.after(start) || startDate.equals(start)) && (startDate.before(end) || startDate.equals(end)))  //开始时间重叠
                        || ((endDate.after(start) || endDate.equals(start)) && (endDate.before(end) || endDate.equals(end)))  //结束事件重叠
                        || (startDate.before(start) && endDate.after(end))//包含
                        ) {
                    return ResponseData.operationFailure("[" + dateFormat.format(start) + "]至[" + dateFormat.format(end) + "],[" + a.getUserName() + "]已经转授权给您，所以该时间段您不能再转授权！");
                }
            }
        }
        return ResponseData.operationSuccess();
    }


    /**
     * 检查该用户建立的授权信息是否重复
     * @param bean
     * @return
     */
    public ResponseData checkUserRepetition(TaskMakeOverPower bean) {
        //查询该用户所有授权信息（不包含当前数据）
        Search search = new Search();
        search.addFilter(new SearchFilter("userId", bean.getUserId()));
        search.addFilter(new SearchFilter("openStatus", true));
        if (StringUtils.isNotEmpty(bean.getId())) {
            search.addFilter(new SearchFilter("id", bean.getId(), SearchFilter.Operator.NE));
        }
        List<TaskMakeOverPower> list = taskMakeOverPowerDao.findByFilters(search);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = bean.getPowerStartDate();
        Date endDate = bean.getPowerEndDate();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TaskMakeOverPower a = list.get(i);
                Date start = a.getPowerStartDate();
                Date end = a.getPowerEndDate();
                if (((startDate.after(start) || startDate.equals(start)) && (startDate.before(end) || startDate.equals(end)))  //开始时间重叠
                        || ((endDate.after(start) || endDate.equals(start)) && (endDate.before(end) || endDate.equals(end)))  //结束事件重叠
                        || (startDate.before(start) && endDate.after(end))//包含
                        ) {
                    return ResponseData.operationFailure("[" + dateFormat.format(start) + "]至[" + dateFormat.format(end) + "],您已经建立了一份[" + a.getPowerUserName() + "]的转授权，不能重复创建！");
                }
            }
        }
        return ResponseData.operationSuccess();
    }


    /**
     * 根据被授权人ID查询满足要求的授权信息
     * @param powerUserId 被授权人ID
     * @return
     */
    public List<TaskMakeOverPower> findMeetUserByPowerId(String powerUserId) {
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simp.parse(simp.format(date));
        } catch (Exception e) {
        }
        Search search = new Search();
        search.addFilter(new SearchFilter("powerUserId", powerUserId));
        search.addFilter(new SearchFilter("openStatus", true));
        search.addFilter(new SearchFilter("powerStartDate", date, SearchFilter.Operator.LE, "Date"));
        search.addFilter(new SearchFilter("powerEndDate", date, SearchFilter.Operator.GE, "Date"));
        return taskMakeOverPowerDao.findByFilters(search);
    }


    /**
     * 通过用户ID获取授权人的集合（包含自己）
     * 待办转授权（同时查看模式）
     * @param userId
     * @return
     */
    public List<String> getAllPowerUserList(String userId) {
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        //查看转授权模式
        String makeOverPowerType = checkMakeOverPowerType();
        //同时查看模式（添加转授权执行人信息）
        if ("sameToSee".equalsIgnoreCase(makeOverPowerType)) {
            List<TaskMakeOverPower> powerUserlist = this.findMeetUserByPowerId(userId);
            if (powerUserlist != null && powerUserlist.size() > 0) {
                powerUserlist.forEach(a -> {
                    userIdList.add(a.getUserId());
                });
            }
        }
        return userIdList;
    }


    /**
     * 检查待办转授权模式（同时查看模式/转办模式）
     * @return
     */
    public String checkMakeOverPowerType() {
        //是否允许转授权(如果没设置或者不为true，视为不允许进行转授权操作)
        String allowMakeOverPower = ContextUtil.getGlobalProperty("ALLOW_MAKE_OVER_POWER");
        if (StringUtils.isEmpty(allowMakeOverPower) || !"true".equalsIgnoreCase(allowMakeOverPower)) {
            return "noType";
        }
        //转授权类型（如果没设置或者不为turnToDo，视为转授权类型为同时查看）
        String allowMakeOverPowerType = ContextUtil.getGlobalProperty("ALLOW_MAKE_OVER_POWER_TYPE");
        if (StringUtils.isEmpty(allowMakeOverPowerType) || !"turnToDo".equalsIgnoreCase(allowMakeOverPowerType)) {
            return "sameToSee";
        }
        return "turnToDo";
    }

    /**
     * 通过授权用户ID返回转办模式转授权信息
     * @param executorId 用户ID
     * @return
     */
    public TaskMakeOverPower getMakeOverPowerByTypeAndUserId(String executorId) {
        if (StringUtils.isNotEmpty(executorId)) {
            String makeOverPowerType = this.checkMakeOverPowerType();
            if ("turnToDo".equalsIgnoreCase(makeOverPowerType)) {    //如果是转办模式（需要替换执行人，保留所属人）
                List<TaskMakeOverPower> listPower = this.findPowerIdByUser(executorId);
                if (listPower != null && listPower.size() > 0) {
                    return listPower.get(0);  //因为有限制，逻辑上满足的只会有一个
                }
            }
        }
        return null;
    }


    /**
     * 根据转授权模式处理不同的逻辑
     * 注：目前主要针对转办模式（共同查看模式时是在查看待办的时候动态实现）
     * @param entity
     */
    public void makeOverPowerTypeToDo(TaskMakeOverPower entity) {
        if (entity != null && entity.getOpenStatus() == true) { //规则生效
            String makeOverPowerType = checkMakeOverPowerType();  //得到转授权模式
            if ("turnToDo".equalsIgnoreCase(makeOverPowerType)) { //转办模式
                List<FlowTask> taskList = getNeedTurnTodoList(entity);
                if (taskList != null && taskList.size() > 0) {
                    List<FlowTask> needDelList = new ArrayList<>();
                    List<FlowTask> addList = new ArrayList<>();
                    //是否推送信息到baisc
                    Boolean pushBasic = flowTaskService.getBooleanPushTaskToBasic();
                    Boolean pushModelOrUrl = null;
                    for (FlowTask flowTask : taskList) {
                        //是否推送信息到业务模块或者直接配置的url
                        if (pushModelOrUrl == null) {
                            pushModelOrUrl = flowTaskService.getBooleanPushModelOrUrl(flowTask.getFlowInstance());
                        }
                        //暂时先不变所属人，方便查看哪些是转授权出去的
                        FlowTask taskBean = new FlowTask();
                        BeanUtils.copyProperties(flowTask, taskBean);
                        taskBean.setId(null);
                        taskBean.setExecutorId(entity.getPowerUserId());
                        taskBean.setExecutorName(entity.getPowerUserName());
                        taskBean.setExecutorAccount(entity.getPowerUserAccount());
                        if (StringUtils.isNotEmpty(taskBean.getDepict()) && !"null".equalsIgnoreCase(taskBean.getDepict())) {
                            taskBean.setDepict("【转授权-" + entity.getUserName() + "授权】" + taskBean.getDepict());
                        } else {
                            taskBean.setDepict("【转授权-" + entity.getUserName() + "授权】");
                        }
                        if (pushBasic || pushModelOrUrl) {
                            needDelList.add(flowTask);
                            addList.add(taskBean);
                        }
                        flowTaskService.delete(flowTask.getId());
                        flowTaskService.save(taskBean);
                    }


                    if (pushBasic || pushModelOrUrl) {
                        if (pushBasic) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    flowTaskService.pushToBasic(addList, null, needDelList, null);
                                }
                            }).start();
                        }
                        if (pushModelOrUrl) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    flowTaskService.pushTaskToModelOrUrl(taskList.get(0).getFlowInstance(), needDelList, TaskStatus.DELETE);
                                    flowTaskService.pushTaskToModelOrUrl(taskList.get(0).getFlowInstance(), addList, TaskStatus.INIT);
                                }
                            }).start();
                        }
                    }
                }
            }
        }
    }

    /**
     * 通过转授权信息得到需要转办的任务
     * @param entity 转授权信息
     * @return
     */
    public List<FlowTask> getNeedTurnTodoList(TaskMakeOverPower entity) {
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = entity.getPowerStartDate();
        Date endDate = entity.getPowerEndDate();
        try {
            startDate = simp.parse(simp.format(startDate));
            endDate = simp.parse(simp.format(endDate));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
        Search search = new Search();
        search.addFilter(new SearchFilter("executorId", entity.getUserId()));
        search.addFilter(new SearchFilter("lastEditedDate", startDate, SearchFilter.Operator.GE, "Date"));
        search.addFilter(new SearchFilter("lastEditedDate", endDate, SearchFilter.Operator.LE, "Date"));
        return flowTaskDao.findByFilters(search);
    }

    /**
     * 根据授权人ID查询满足要求的授权信息
     * @param userId 授权人ID
     * @return
     */
    public List<TaskMakeOverPower> findPowerIdByUser(String userId) {
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simp.parse(simp.format(date));
        } catch (Exception e) {
        }
        Search search = new Search();
        search.addFilter(new SearchFilter("userId", userId));
        search.addFilter(new SearchFilter("openStatus", true));
        search.addFilter(new SearchFilter("powerStartDate", date, SearchFilter.Operator.LE, "Date"));
        search.addFilter(new SearchFilter("powerEndDate", date, SearchFilter.Operator.GE, "Date"));
        return taskMakeOverPowerDao.findByFilters(search);
    }

    /**
     * 得到转授权的基本记录信息
     * 注：如果是转授权的转办模式，task里面记录了【转授权-***授权】的信息，此方法主要用于提出这些信息，方便历史记录
     * @param depict
     * @return
     */
    public String getOverPowerStrByDepict(String depict) {
        if (StringUtils.isNotEmpty(depict)) {
            int startInt = depict.indexOf("【转授权-");
            int endInt = depict.indexOf("授权】");
            if (startInt != -1 && endInt != -1 && endInt >= startInt) {
                return depict.substring(startInt, endInt + 3);
            }
        }
        return "";
    }
}
