package com.kcmp.ck.flow.dao.impl;

import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;
import com.kcmp.core.ck.dto.PageInfo;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchOrder;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.dao.AppModuleDao;
import com.kcmp.ck.flow.dao.FlowTaskExtDao;
import com.kcmp.ck.flow.dao.util.PageUrlUtil;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.entity.WorkPageUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by kikock
 * 流程任务扩展dao实现
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowTaskDaoImpl extends BaseEntityDaoImpl<FlowTask> implements FlowTaskExtDao {

    public FlowTaskDaoImpl(EntityManager entityManager) {
        super(FlowTask.class, entityManager);
    }

    @Autowired
    private AppModuleDao appModuleDao;

    String hqlQueryOrder = "   order by ft.priority desc,ft.createdDate asc ";

    /**
     * 通过业务实体类型id,执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageByBusinessModelId(String businessModelId, String executorId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders = searchConfig.getSortOrders();
        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  = :executorId and (ft.trustState != 1  or ft.trustState is null ) and ft.flowDefinitionId in " +
                "( select fd.id from FlowDefination fd where fd.flowType.id in " +
                "( select fType.id from FlowType fType where fType.businessModel.id in " +
                "( select bm.id from BusinessModel bm where bm.id = :businessModelId)))";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorId", executorId);
        queryTotal.setParameter("businessModelId", businessModelId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorId", executorId);
        query.setParameter("businessModelId", businessModelId);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过业务实体类型id,执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageByBusinessModelIdOfPower(String businessModelId, List<String> executorIdList, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders = searchConfig.getSortOrders();
        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  in (:executorIdList) and (ft.trustState != 1  or ft.trustState is null ) and ft.flowDefinitionId in " +
                "( select fd.id from FlowDefination fd where fd.flowType.id in " +
                "( select fType.id from FlowType fType where fType.businessModel.id in " +
                "( select bm.id from BusinessModel bm where bm.id = :businessModelId)))";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorIdList", executorIdList);
        queryTotal.setParameter("businessModelId", businessModelId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorIdList", executorIdList);
        query.setParameter("businessModelId", businessModelId);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过执行人id,应用标识代码,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId        执行人id
     * @param appSign           应用标识代码
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPage(String executorId, String appSign, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders = searchConfig.getSortOrders();
        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  = :executorId and (ft.trustState !=1  or ft.trustState is null) ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);
        // 限制应用标识
        if (!StringUtils.isBlank(appSign)) {
            String appSignSql = "and ft.flowDefinitionId " +
                    "in( select fd.id from FlowDefination fd where fd.flowType.id " +
                    "in( select fType.id from FlowType fType where fType.businessModel.id " +
                    "in( select bm.id from BusinessModel bm where bm.appModule.code like :appSign)))";
            fromAndWhere += appSignSql;
        }

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorId", executorId);
        if (!StringUtils.isBlank(appSign)) {
            queryTotal.setParameter("appSign", appSign + "%");
        }
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorId", executorId);
        if (!StringUtils.isBlank(appSign)) {
            queryTotal.setParameter("appSign", appSign + "%");
        }
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过执行人id列表,应用标识代码,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorIdList    执行人id列表
     * @param appSign           应用标识代码
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageOfPower(List<String> executorIdList, String appSign, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders = searchConfig.getSortOrders();

        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  in (:executorIdList) and (ft.trustState !=1  or ft.trustState is null) ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);
        // 限制应用标识
        if (!StringUtils.isBlank(appSign)) {
            String appSignSql = "and ft.flowDefinitionId " +
                    "in (select fd.id from FlowDefination fd where fd.flowType.id " +
                    "in (select fType.id from FlowType fType where fType.businessModel.id " +
                    "in (select bm.id from BusinessModel bm where bm.appModule.code like :appSign)))";
            fromAndWhere += appSignSql;
        }

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorIdList", executorIdList);
        if (!StringUtils.isBlank(appSign)) {
            queryTotal.setParameter("appSign", appSign + "%");
        }
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorIdList", executorIdList);
        if (!StringUtils.isBlank(appSign)) {
            queryTotal.setParameter("appSign", appSign + "%");
        }
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId    执行人id
     * @param searchConfig  组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageCanBatchApproval(String executorId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();

        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  = :executorId and ft.canBatchApproval = true ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorId", executorId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += hqlQueryOrder;
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorId", executorId);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过执行人id,动态组合条件查询数据条数
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public Long findCountByExecutorId(String executorId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        String hqlCount = "select count(ft.id) from FlowTask ft where ft.executorId  = :executorId  and  (ft.trustState !=1  or ft.trustState is null ) ";

        hqlCount += handleQuickSearch(quickSearchValue,quickSearchProperties);
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorId", executorId);
        Long total = queryTotal.getSingleResult();
        return total;
    }

    /**
     * 通过执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageCanBatchApprovalOfPower(List<String> executorIdList, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();

        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where 1 = 1 and ft.executorId  in (:executorIdList) and ft.canBatchApproval = true and (ft.trustState !=1  or ft.trustState is null ) ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorIdList", executorIdList);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += hqlQueryOrder;
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorIdList", executorIdList);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过业务实体id,执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体id
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageCanBatchApprovalByBusinessModelId(String businessModelId, String executorId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();

        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where ft.executorId  = :executorId and ft.canBatchApproval = true and ft.flowDefinitionId in " +
                "( select fd.id from FlowDefination fd where fd.flowType.id in" +
                "( select fType.id from FlowType fType where fType.businessModel.id in" +
                "( select bm.id from .BusinessModel bm where bm.id = :businessModelId)) ) ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorId", executorId);
        queryTotal.setParameter("businessModelId", businessModelId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += hqlQueryOrder;
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorId", executorId);
        query.setParameter("businessModelId", businessModelId);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过业务实体id,执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体id
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageCanBatchApprovalByBusinessModelIdOfPower(String businessModelId, List<String> executorIdList, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where ft.executorId  in (:executorIdList) and ft.canBatchApproval = true and (ft.trustState !=1  or ft.trustState is null )  and ft.flowDefinitionId in " +
                "( select fd.id from FlowDefination fd where fd.flowType.id in " +
                "( select fType.id from FlowType fType where fType.businessModel.id in " +
                "( select bm.id from BusinessModel bm where bm.id = :businessModelId)) ) ";

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        queryTotal.setParameter("executorIdList", executorIdList);
        queryTotal.setParameter("businessModelId", businessModelId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += hqlQueryOrder;
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setParameter("executorIdList", executorIdList);
        query.setParameter("businessModelId", businessModelId);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过应用模块id,业务实体id,流程类型id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param appModuleId       应用模块id
     * @param businessModelId   业务实体id
     * @param flowTypeId        流程类型id
     * @param searchConfig      组合条件
     * @return
     */
    @Override
    public PageResult<FlowTask> findByPageByTenant(String appModuleId, String businessModelId, String flowTypeId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        if (Objects.isNull(pageInfo)) {
            pageInfo = new PageInfo();
        }
        Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
        String quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders = searchConfig.getSortOrders();

        String select = "select ft ";
        String selectCount = "select count(ft.id) ";
        String fromAndWhere = " from FlowTask ft where (ft.trustState !=1  or ft.trustState is null ) ";

        if (StringUtils.isNotEmpty(flowTypeId) && !"".equals(flowTypeId)) {
            fromAndWhere += " and ft.flowDefinitionId in (select fd.id from FlowDefination fd where fd.flowType.id in " +
                    "(select fType.id from FlowType fType where fType.id  = '" + flowTypeId + "' ))";
        } else if (StringUtils.isNotEmpty(businessModelId) && !"".equals(businessModelId)) {
            fromAndWhere += " and ft.flowDefinitionId in (select fd.id from FlowDefination fd where fd.flowType.id in " +
                    "(select fType.id from FlowType fType where fType.businessModel.id = '" + businessModelId + "' ) )";
        } else if (StringUtils.isNotEmpty(appModuleId) && !"".equals(appModuleId)) {
            fromAndWhere += " and ft.flowDefinitionId in (select fd.id from FlowDefination fd where fd.flowType.id in " +
                    "(select fType.id from FlowType fType where fType.businessModel.id in " +
                    "( select bm.id from BusinessModel bm where bm.appModule.id='" + appModuleId + "' )) )";
        }

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery(hqlCount, Long.class);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += hqlQueryOrder;
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowTask> query = entityManager.createQuery(hqlQuery, FlowTask.class);
        query.setFirstResult((pageInfo.getPage() - 1) * pageInfo.getRows());
        query.setMaxResults(pageInfo.getRows());
        List<FlowTask> result = query.getResultList();
        //处理结果
        PageResult<FlowTask> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 通过待办任务id获取一个待办任务(设置了办理任务URL)
     * @param taskId 待办任务id
     * @return 待办任务
     */
    @Override
    public FlowTask findTaskById(String taskId) {
        FlowTask flowTask = findOne(taskId);
        if (Objects.nonNull(flowTask)) {
            initFlowTask(flowTask);
        }
        return flowTask;
    }

    /**
     * 完成待办任务的URL设置
     * @param flowTasks 待办任务清单
     * @return 待办任务
     */
    @Override
    public void initFlowTasks(List<FlowTask> flowTasks) {
        if (CollectionUtils.isEmpty(flowTasks)) {
            return;
        }
        flowTasks.forEach(this::initFlowTask);
    }

    /**
     * 完成待办任务的URL设置
     * @param flowTask 待办任务
     * @return 待办任务
     */
    private void initFlowTask(FlowTask flowTask) {
        String apiBaseAddressConfig = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
        String apiBaseAddress = ContextUtil.getGlobalProperty(apiBaseAddressConfig);
        if (StringUtils.isNotEmpty(apiBaseAddress)) {
            flowTask.setApiBaseAddressAbsolute(apiBaseAddress);
            String[] tempApiBaseAddress = apiBaseAddress.split("/");
            if (tempApiBaseAddress != null && tempApiBaseAddress.length > 0) {
                apiBaseAddress = tempApiBaseAddress[tempApiBaseAddress.length - 1];
                flowTask.setApiBaseAddress("/" + apiBaseAddress + "/");
            }
        }
        String webBaseAddressConfig = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getWebBaseAddress();
        String webBaseAddress = ContextUtil.getGlobalProperty(webBaseAddressConfig);

        if (StringUtils.isNotEmpty(webBaseAddress)) {
            flowTask.setWebBaseAddressAbsolute(webBaseAddress);
            flowTask.setLookWebBaseAddressAbsolute(webBaseAddress);
            String[] tempWebBaseAddress = webBaseAddress.split("/");
            if (tempWebBaseAddress != null && tempWebBaseAddress.length > 0) {
                webBaseAddress = tempWebBaseAddress[tempWebBaseAddress.length - 1];
                flowTask.setWebBaseAddress("/" + webBaseAddress + "/");
                flowTask.setLookWebBaseAddress("/" + webBaseAddress + "/");
            }
        }
        WorkPageUrl workPageUrl = flowTask.getWorkPageUrl();
        String completeTaskServiceUrl = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getCompleteTaskServiceUrl();
        String businessDetailServiceUrl = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessDetailServiceUrl();
        if (StringUtils.isEmpty(completeTaskServiceUrl)) {
            completeTaskServiceUrl = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getCompleteTaskServiceUrl();
        }
        if (StringUtils.isEmpty(businessDetailServiceUrl)) {
            businessDetailServiceUrl = flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getBusinessDetailServiceUrl();
        }
        flowTask.setCompleteTaskServiceUrl(completeTaskServiceUrl);
        flowTask.setBusinessDetailServiceUrl(businessDetailServiceUrl);
        if (workPageUrl != null) {
            flowTask.setTaskFormUrl(PageUrlUtil.buildUrl(ContextUtil.getGlobalProperty(webBaseAddressConfig), workPageUrl.getUrl()));
            String taskFormUrlXiangDui = "/" + webBaseAddress + "/" + workPageUrl.getUrl();
            taskFormUrlXiangDui = taskFormUrlXiangDui.replaceAll("\\//", "/");
            flowTask.setTaskFormUrlXiangDui(taskFormUrlXiangDui);
            String appModuleId = workPageUrl.getAppModuleId();
            AppModule appModule = appModuleDao.findOne(appModuleId);
            if (appModule != null && !appModule.getId().equals(flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getId())) {
                webBaseAddressConfig = appModule.getWebBaseAddress();
                webBaseAddress = ContextUtil.getGlobalProperty(webBaseAddressConfig);
                flowTask.setTaskFormUrl(PageUrlUtil.buildUrl(webBaseAddress, workPageUrl.getUrl()));
                if (StringUtils.isNotEmpty(webBaseAddress)) {
                    flowTask.setWebBaseAddressAbsolute(webBaseAddress);
                    String[] tempWebBaseAddress = webBaseAddress.split("/");
                    if (tempWebBaseAddress != null && tempWebBaseAddress.length > 0) {
                        webBaseAddress = tempWebBaseAddress[tempWebBaseAddress.length - 1];
                        flowTask.setWebBaseAddress("/" + webBaseAddress + "/");
                    }
                }
                taskFormUrlXiangDui = "/" + webBaseAddress + "/" + workPageUrl.getUrl();
                taskFormUrlXiangDui = taskFormUrlXiangDui.replaceAll("\\//", "/");
                flowTask.setTaskFormUrlXiangDui(taskFormUrlXiangDui);
            }
        }
    }

    /**
     * 处理快速查询条件
     * @param quickSearchValue          快速查询值
     * @param quickSearchProperties     快速查询字段
     * @return
     */
    private String handleQuickSearch(String quickSearchValue,Collection<String> quickSearchProperties){
        String result = "";
        if (StringUtils.isNotEmpty(quickSearchValue) && quickSearchProperties != null && !quickSearchProperties.isEmpty()) {
            StringBuffer extraHql = new StringBuffer(" and (");
            boolean first = true;
            for (String s : quickSearchProperties) {
                if (first) {
                    extraHql.append("  ft." + s + " like '%" + quickSearchValue + "%'");
                    first = false;
                } else {
                    extraHql.append(" or  ft." + s + " like '%" + quickSearchValue + "%'");
                }
            }
            extraHql.append(" )");
            result = extraHql.toString();
        }
        return result;
    }

    /**
     * 处理排序列表
     * @param sortOrders    排序配置参数
     * @return
     */
    private String handleSortOrder(List<SearchOrder> sortOrders){
        String result = "";
        if (sortOrders != null && sortOrders.size() > 0) {
            result += " order by ft.priority desc ";
            for (int i = 0; i < sortOrders.size(); i++) {
                SearchOrder searchOrder = sortOrders.get(i);
                if (!"priority".equalsIgnoreCase(searchOrder.getProperty())) {
                    result += ", ft." + searchOrder.getProperty() + " " + searchOrder.getDirection();
                }
            }
        } else {
            result += hqlQueryOrder;
        }
        return result;
    }

    /**
     * 处理结果集
     * @param pageInfo      分页参数
     * @param flowTaskList  流程任务数据
     * @param total         流程任务条数
     * @return
     */
    private PageResult<FlowTask> handlePageResult(PageInfo pageInfo,List<FlowTask> flowTaskList,Long total){
        initFlowTasks(flowTaskList);
        PageResult<FlowTask> pageResult = new PageResult<>();
        pageResult.setPage(pageInfo.getPage());
        pageResult.setRows(flowTaskList);
        pageResult.setRecords(total.intValue());
        pageResult.setTotal((total.intValue() + pageInfo.getRows() - 1) / pageInfo.getRows());
        return pageResult;
    }
}
