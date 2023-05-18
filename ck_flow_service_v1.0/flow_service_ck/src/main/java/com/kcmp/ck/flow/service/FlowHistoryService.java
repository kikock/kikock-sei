package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.*;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.IFlowHistoryService;
import com.kcmp.ck.flow.dao.BusinessModelDao;
import com.kcmp.ck.flow.dao.FlowDefinationDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowDefination;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.util.FlowTaskTool;
import com.kcmp.ck.flow.vo.TodoBusinessSummaryVO;
import com.kcmp.ck.flow.vo.phone.FlowHistoryPhoneVo;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.vo.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by kikock
 * 流程历史服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowHistoryService extends BaseEntityService<FlowHistory> implements IFlowHistoryService {

    @Autowired
    private FlowHistoryDao flowHistoryDao;

    @Autowired
    private FlowTaskTool flowTaskTool;

    @Autowired
    private FlowDefinationDao flowDefinationDao;

    @Autowired
    private BusinessModelDao businessModelDao;

    @Override
    protected BaseEntityDao<FlowHistory> getDao() {
        return this.flowHistoryDao;
    }

    /**
     * 根据流程实例ID查询流程历史
     * @param instanceId 实例id
     * @return 执行历史
     */
    @Override
    public List<FlowHistory> findByInstanceId(String instanceId) {
        return flowHistoryDao.findByInstanceId(instanceId);
    }

    /**
     * 获取分页数据（根据当前用户id）
     * @param searchConfig  查询参数
     * @return
     */
    @Override
    public PageResult<FlowHistory> findByPageAndUser(Search searchConfig) {
        String userId = ContextUtil.getUserId();
        return flowHistoryDao.findByPageByBusinessModelId(userId, searchConfig);
    }

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return
     */
    @Override
    public PageResult<FlowHistory> findByPage(Search searchConfig) {
        PageResult<FlowHistory> result = super.findByPage(searchConfig);
        if (result != null) {
            List<FlowHistory> flowHistoryList = result.getRows();
            this.initUrl(flowHistoryList);
        }
        return result;
    }

    public List<FlowHistory> findByAllTaskMakeOverPowerHistory(){
        return flowHistoryDao.findByAllTaskMakeOverPowerHistory();
    }

    private List<FlowHistory> initUrl(List<FlowHistory> result) {
        if (result != null && !result.isEmpty()) {
            for (FlowHistory flowHistory : result) {
                String apiBaseAddressConfig = flowHistory.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
                String apiBaseAddress = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                flowHistory.setApiBaseAddressAbsolute(apiBaseAddress);
                apiBaseAddress = apiBaseAddress.substring(apiBaseAddress.lastIndexOf(":"));
                apiBaseAddress = apiBaseAddress.substring(apiBaseAddress.indexOf("/"));
                String webBaseAddressConfig = flowHistory.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getWebBaseAddress();
                String webBaseAddress = ContextUtil.getGlobalProperty(webBaseAddressConfig);
                flowHistory.setWebBaseAddressAbsolute(webBaseAddress);
                webBaseAddress = webBaseAddress.substring(webBaseAddress.lastIndexOf(":"));
                webBaseAddress = webBaseAddress.substring(webBaseAddress.indexOf("/"));
                flowHistory.setApiBaseAddress(apiBaseAddress);
                flowHistory.setWebBaseAddress(webBaseAddress);
            }
        }
        return result;
    }


    /**
     * 查询当前用户已办单据汇总信息
     * @param appSign 应用标识
     * @return 汇总信息
     */
    public List<TodoBusinessSummaryVO> findHisTorySumHeader(String appSign, String dataType) {
        List<TodoBusinessSummaryVO> voList = new ArrayList<>();
        String userID = ContextUtil.getUserId();
        List groupResultList  = null;
        if(StringUtils.isNotEmpty(dataType)&&!"all".equals(dataType)){
            if ("record".equals(dataType)){//记录数据
                groupResultList  = flowHistoryDao.findHisByExecutorIdGroupRecord(userID);
            }else{ //有效数据
                groupResultList  = flowHistoryDao.findHisByExecutorIdGroupValid(userID);
            }
        }else{
            groupResultList  = flowHistoryDao.findHisByExecutorIdGroup(userID);
        }

        Map<BusinessModel, Integer> businessModelCountMap = new HashMap<BusinessModel, Integer>();
        if (groupResultList != null && !groupResultList.isEmpty()) {
            Iterator it = groupResultList.iterator();
            while (it.hasNext()) {
                Object[] res = (Object[]) it.next();
                int count = ((Number) res[0]).intValue();
                String flowDefinationId = res[1] + "";
                FlowDefination flowDefination = flowDefinationDao.findOne(flowDefinationId);
                if (flowDefination == null) {
                    continue;
                }

                // 获取业务类型
                BusinessModel businessModel = businessModelDao.findOne(flowDefination.getFlowType().getBusinessModel().getId());
                // 限制应用标识
                boolean canAdd = true;
                if (!StringUtils.isBlank(appSign)) {
                    // 判断应用模块代码是否以应用标识开头,不是就不添加
                    if (!businessModel.getAppModule().getCode().startsWith(appSign)) {
                        canAdd = false;
                    }
                }
                if (canAdd) {
                    Integer oldCount = businessModelCountMap.get(businessModel);
                    if (oldCount == null) {
                        oldCount = 0;
                    }
                    businessModelCountMap.put(businessModel, oldCount + count);
                }
            }
        }
        if (!businessModelCountMap.isEmpty()) {
            for (Map.Entry<BusinessModel, Integer> map : businessModelCountMap.entrySet()) {
                TodoBusinessSummaryVO todoBusinessSummaryVO = new TodoBusinessSummaryVO();
                todoBusinessSummaryVO.setBusinessModelCode(map.getKey().getClassName());
                todoBusinessSummaryVO.setBusinessModeId(map.getKey().getId());
                todoBusinessSummaryVO.setCount(map.getValue());
                todoBusinessSummaryVO.setBusinessModelName(map.getKey().getName() + "(" + map.getValue() + ")");
                voList.add(todoBusinessSummaryVO);
            }
        }
        return voList;
    }

    /**
     * 查询流程已办汇总列表
     * @return
     */
    @Override
    public ResponseData listFlowHistoryHeader(String dataType) {
        try {
            List<TodoBusinessSummaryVO> list = this.findHisTorySumHeader("",dataType);
            return ResponseData.operationSuccessWithData(list);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return ResponseData.operationFailure("操作失败！");
        }
    }

    /**
     * 获取已办信息
     * @param businessModelId   业务实体id
     * @param searchConfig      查询条件
     * @return 已办汇总信息
     */
    @Override
    public ResponseData listFlowHistory(String businessModelId, Search searchConfig) {
        ResponseData responseData = new ResponseData();
    try{
        PageResult<FlowHistory> pageList = this.findByBusinessModelId(businessModelId,searchConfig);
        responseData.setData(pageList);
    }catch (Exception e){
        responseData.setSuccess(false);
        responseData.setMessage(e.getMessage());
        LogUtil.error(e.getMessage());
    }
        return responseData;
    }

    /**
     * 获取待办汇总信息
     * @param businessModelId 业务实体id
     * @param searchConfig 查询条件
     * @return 待办汇总信息
     */
    @Override
    public PageResult<FlowHistory> findByBusinessModelId(String businessModelId, Search searchConfig) {
        String userId = ContextUtil.getUserId();
        PageResult<FlowHistory> result;
        if (StringUtils.isNotEmpty(businessModelId)) {
            result = flowHistoryDao.findByPageByBusinessModelId(businessModelId, userId, searchConfig);
        } else {
            result = flowHistoryDao.findByPage(userId, searchConfig);
        }
        if (result.getRows() != null && result.getRows().size() > 0) {
            result.getRows().forEach(a -> {
                //CompleteTaskView.js中撤回按钮显示的判断逻辑：
                // (items[j].canCancel == true && items[j].taskStatus == "COMPLETED" && items[j].flowInstance.ended == false)
                if(a.getCanCancel()!=null&&a.getCanCancel() == true
                        &&a.getTaskStatus()!=null&&"COMPLETED".equalsIgnoreCase(a.getTaskStatus())
                        &&a.getFlowInstance() != null&&a.getFlowInstance().isEnded()!=null&&a.getFlowInstance().isEnded() == false){
                    Boolean boo = flowTaskTool.checkoutTaskRollBack(a);
                    if (!boo) { //不可以显示
                        a.setCanCancel(false);
                    }
                }
            });
        }
        return result;
    }

    /**
     * 获取已办汇总信息（最新移动端专用）
     * @param businessModelId 业务实体id
     * @param property 需要排序的字段
     * @param direction 排序规则
     * @param page 当前页数
     * @param rows 每页条数
     * @param quickValue 模糊查询字段内容
     * @return 待办汇总信息
     */
    @Override
    public PageResult<FlowHistoryPhoneVo> findByBusinessModelIdOfMobile(String businessModelId, String property, String direction,
                                                                        int page, int rows, String quickValue) {
        Search searchConfig = new Search();
        String userId = ContextUtil.getUserId();
        searchConfig.addFilter(new SearchFilter("executorId", userId, SearchFilter.Operator.EQ));
        //根据业务单据名称、业务单据号、业务工作说明快速查询
        searchConfig.addQuickSearchProperty("flowName");
        searchConfig.addQuickSearchProperty("flowTaskName");
        searchConfig.addQuickSearchProperty("flowInstance.businessCode");
        searchConfig.addQuickSearchProperty("flowInstance.businessModelRemark");
        searchConfig.addQuickSearchProperty("creatorName");
        searchConfig.setQuickSearchValue(quickValue);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(page);
        pageInfo.setRows(rows);
        searchConfig.setPageInfo(pageInfo);

        SearchOrder searchOrder;
        if (StringUtils.isNotEmpty(property) && StringUtils.isNotEmpty(direction)) {
            if (SearchOrder.Direction.ASC.equals(direction)) {
                searchOrder = new SearchOrder(property, SearchOrder.Direction.ASC);
            } else {
                searchOrder = new SearchOrder(property, SearchOrder.Direction.DESC);
            }
        } else {
            searchOrder = new SearchOrder("createdDate", SearchOrder.Direction.ASC);
        }
        List<SearchOrder> list = new ArrayList<SearchOrder>();
        list.add(searchOrder);
        searchConfig.setSortOrders(list);

        PageResult<FlowHistory> historyPage = this.findByBusinessModelId(businessModelId, searchConfig);
        PageResult<FlowHistoryPhoneVo> historyVoPage = new PageResult<FlowHistoryPhoneVo>();
        if(historyPage.getRows()!=null&&historyPage.getRows().size()>0){
            List<FlowHistory>  historyList = historyPage.getRows();
            List<FlowHistoryPhoneVo> phoneVoList = new ArrayList<FlowHistoryPhoneVo>();
            historyList.forEach(bean->{
                FlowHistoryPhoneVo beanVo =new FlowHistoryPhoneVo();
                FlowInstance flowInstance = bean.getFlowInstance();
                BusinessModel businessModel =bean.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                beanVo.setId(bean.getId());
                beanVo.setFlowName(bean.getFlowName());
                beanVo.setFlowTaskName(bean.getFlowTaskName());
                beanVo.setCreatedDate(bean.getCreatedDate());
                beanVo.setTaskStatus(bean.getTaskStatus());
                beanVo.setCanCancel(bean.getCanCancel());
                beanVo.setFlowInstanceBusinessId(flowInstance.getBusinessId());
                beanVo.setFlowInstanceBusinessCode(flowInstance.getBusinessCode());
                beanVo.setFlowInstanceEnded(flowInstance.isEnded());

                String apiBaseAddress =  ContextUtil.getGlobalProperty(businessModel.getAppModule().getApiBaseAddress());
                String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddress);
                beanVo.setBusinessDetailServiceUrl(clientApiBaseUrl+businessModel.getBusinessDetailServiceUrl());
                phoneVoList.add(beanVo);
            });
            historyVoPage.setRows(phoneVoList);
        }else{
            historyVoPage.setRows(new ArrayList<FlowHistoryPhoneVo>());
        }
        historyVoPage.setPage(historyPage.getPage());
        historyVoPage.setRecords(historyPage.getRecords());
        historyVoPage.setTotal(historyPage.getTotal());
        return historyVoPage;
    }

    /**
     * 获取已办汇总信息（移动端专用）
     * @param businessModelId 业务实体id
     * @param property 需要排序的字段
     * @param direction 排序规则
     * @param page 当前页数
     * @param rows 每页条数
     * @param quickValue 模糊查询字段内容
     * @return 待办汇总信息
     */
    @Override
    public PageResult<FlowHistory> findByBusinessModelIdOfPhone(String businessModelId, String property, String direction,
                                                                int page, int rows, String quickValue) {
        Search searchConfig = new Search();
        String userId = ContextUtil.getUserId();
        searchConfig.addFilter(new SearchFilter("executorId", userId, SearchFilter.Operator.EQ));
        //根据业务单据名称、业务单据号、业务工作说明快速查询
        searchConfig.addQuickSearchProperty("flowName");
        searchConfig.addQuickSearchProperty("flowTaskName");
        searchConfig.addQuickSearchProperty("flowInstance.businessCode");
        searchConfig.addQuickSearchProperty("flowInstance.businessModelRemark");
        searchConfig.addQuickSearchProperty("creatorName");
        searchConfig.setQuickSearchValue(quickValue);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(page);
        pageInfo.setRows(rows);
        searchConfig.setPageInfo(pageInfo);

        SearchOrder searchOrder;
        if (StringUtils.isNotEmpty(property) && StringUtils.isNotEmpty(direction)) {
            if (SearchOrder.Direction.ASC.equals(direction)) {
                searchOrder = new SearchOrder(property, SearchOrder.Direction.ASC);
            } else {
                searchOrder = new SearchOrder(property, SearchOrder.Direction.DESC);
            }
        } else {
            searchOrder = new SearchOrder("createdDate", SearchOrder.Direction.ASC);
        }
        List<SearchOrder> list = new ArrayList<SearchOrder>();
        list.add(searchOrder);
        searchConfig.setSortOrders(list);

        return this.findByBusinessModelId(businessModelId, searchConfig);
    }
}
