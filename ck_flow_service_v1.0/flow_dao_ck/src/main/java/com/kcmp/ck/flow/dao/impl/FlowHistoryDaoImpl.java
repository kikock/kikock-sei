package com.kcmp.ck.flow.dao.impl;

import com.kcmp.core.ck.dao.impl.BaseEntityDaoImpl;
import com.kcmp.core.ck.dto.*;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.dao.FlowHistoryExtDao;
import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

/**
 * Created by kikock
 * 流程历史扩展dao实现
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowHistoryDaoImpl extends BaseEntityDaoImpl<FlowHistory> implements FlowHistoryExtDao {

    public FlowHistoryDaoImpl(EntityManager entityManager) {
        super(FlowHistory.class, entityManager);
    }

    /**
     * 通过业务实体类型id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorId        执行人账号
     * @param searchConfig      组合条件对象
     * @return
     */
    @Override
    public PageResult<FlowHistory> findByPageByBusinessModelId(String businessModelId, String executorId, Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties= searchConfig.getQuickSearchProperties();
        String  quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders =   searchConfig.getSortOrders();
        List<SearchFilter>  searchFilters = searchConfig.getFilters();
        String select = "select fh ";
        String selectCount = "select count(fh.id) ";
        String fromAndWhere = " from FlowHistory fh where 1 = 1 fh.executorId  = :executorId and fh.flowInstance.id in  " +
                "( select fi.id from  FlowInstance fi where  fi.flowDefVersion.id in  " +
                "( select fdv.id from FlowDefVersion fdv  where  fdv.flowDefination.id in " +
                "( select fd.id from FlowDefination fd where fd.flowType.id in  " +
                "( select ft.id from FlowType ft where ft.businessModel.id = :businessModelId )))) ";

        if (searchFilters != null && searchFilters.size() > 0 && searchFilters.get(0).getValue()!=null) {
            SearchFilter filters =  searchFilters.get(0);
            if("valid".equals(filters.getValue())){
                //有效数据
                fromAndWhere += " and ( fh.flowExecuteStatus in ('submit','agree','disagree','turntodo','entrust','recall','reject')  or fh.flowExecuteStatus is null)";
            } else if("record".equals(filters.getValue())){
                //记录数据
                fromAndWhere += " and fh.flowExecuteStatus in ('end','auto') ";
            }
        }

        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery( hqlCount, Long.class);
        queryTotal.setParameter("executorId",executorId);
        queryTotal.setParameter("businessModelId",businessModelId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowHistory> query = entityManager.createQuery(hqlQuery, FlowHistory.class);
        query.setParameter("executorId",executorId);
        query.setParameter("businessModelId",businessModelId);

        query.setFirstResult( (pageInfo.getPage()-1) * pageInfo.getRows() );
        query.setMaxResults( pageInfo.getRows() );
        List<FlowHistory> result = query.getResultList();
        //处理结果
        PageResult<FlowHistory> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorAccount   执行人账号
     * @param searchConfig      组合条件对象
     * @return
     */
    @Override
    public PageResult<FlowHistory> findByPageByBusinessModelId(String executorAccount, Search searchConfig){
        PageInfo pageInfo = searchConfig.getPageInfo();

        TypedQuery<Integer> queryTotal = entityManager.createQuery("select count(fh.id) from FlowHistory fh " +
                "where fh.executorAccount  = :executorAccount  ", Integer.class);
        queryTotal.setParameter("executorAccount",executorAccount);
        Integer total = queryTotal.getSingleResult();

        TypedQuery<FlowHistory> query = entityManager.createQuery("select fh from FlowHistory fh " +
                "where fh.executorAccount  = :executorAccount order by fh.lastEditedDate desc", FlowHistory.class);
        query.setParameter("executorAccount",executorAccount);
        query.setFirstResult( (pageInfo.getPage()-1) * pageInfo.getRows() );
        query.setMaxResults( pageInfo.getRows() );
        List<FlowHistory>  result = query.getResultList();
        initFlowTaskAppModule(result);
        PageResult<FlowHistory> pageResult = new PageResult<>();
        pageResult.setPage(pageInfo.getPage());
        pageResult.setRows(result);
        pageResult.setRecords(result.size());
        pageResult.setTotal(total);
        return pageResult;
    }

    /**
     * 根据业务实体类型id，业务单据id，获取最新流程实体执行的待办，不包括撤销之前的历史任务
     * @param businessId    业务实体类型id
     * @return
     */
    @Override
    public List<FlowHistory> findLastByBusinessId(String businessId){
        TypedQuery<FlowInstance> instanceQuery = entityManager.createQuery("select fh from FlowInstance fh " +
                "where fh.businessId = :businessId  order by fh.lastEditedDate desc", FlowInstance.class);
        instanceQuery.setParameter("businessId",businessId);
        FlowInstance flowInstance = instanceQuery.getSingleResult();

        TypedQuery<FlowHistory> flowHistoryQuery = entityManager.createQuery("select fh.id from FlowHistory fh " +
                "where fh.flowInstance.id = :instanceId  order by fh.lastEditedDate desc", FlowHistory.class);
        flowHistoryQuery.setParameter("instanceId",flowInstance.getId());
        List<FlowHistory> flowHistoryList = flowHistoryQuery.getResultList();
        initFlowTaskAppModule(flowHistoryList);
        return flowHistoryList;
    }

    /**
     * 查询所有转授权历史
     * @return
     */
    @Override
    public List<FlowHistory>  findByAllTaskMakeOverPowerHistory(){
        TypedQuery<FlowHistory> flowHistoryQuery = entityManager.createQuery("select fh from FlowHistory fh " +
                "where fh.executorId <> fh.ownerId  and  fh.executorId is not null and  fh.ownerId is not null  " +
                "order by fh.lastEditedDate desc", FlowHistory.class);
        List<FlowHistory> flowHistoryList = flowHistoryQuery.getResultList();
        initFlowTaskAppModule(flowHistoryList);
        return flowHistoryList;
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId    执行人账号
     * @param searchConfig  组合条件对象
     * @return
     */
    @Override
    public PageResult<FlowHistory> findByPage(String executorId, Search searchConfig){
        PageInfo pageInfo = searchConfig.getPageInfo();
        Collection<String> quickSearchProperties= searchConfig.getQuickSearchProperties();
        String  quickSearchValue = searchConfig.getQuickSearchValue();
        List<SearchOrder> sortOrders =   searchConfig.getSortOrders();
        List<SearchFilter>  searchFilters = searchConfig.getFilters();
        String select = "select fh ";
        String selectCount = "select count(fh.id) ";
        String fromAndWhere = " from FlowHistory fh where fh.executorId  = :executorId ";

        if (searchFilters!=null && searchFilters.size()>0 && searchFilters.get(0).getValue()!=null) {
            SearchFilter filters =  searchFilters.get(0);
            if("valid".equals(filters.getValue())){
                //有效数据(以前没这个字段，都作为有效数据)
                fromAndWhere += " and ( fh.flowExecuteStatus in ('submit','agree','disagree','turntodo','entrust','recall','reject')  or  fh.flowExecuteStatus is null  )";
            }else if("record".equals(filters.getValue())){
                //记录数据
                fromAndWhere += " and fh.flowExecuteStatus in ('end','auto') ";
            }
        }
        fromAndWhere += handleQuickSearch(quickSearchValue,quickSearchProperties);

        String hqlCount = selectCount + fromAndWhere;
        TypedQuery<Long> queryTotal = entityManager.createQuery( hqlCount, Long.class);
        queryTotal.setParameter("executorId",executorId);
        Long total = queryTotal.getSingleResult();

        fromAndWhere += handleSortOrder(sortOrders);
        String hqlQuery = select + fromAndWhere;
        TypedQuery<FlowHistory> query = entityManager.createQuery(hqlQuery, FlowHistory.class);
        query.setParameter("executorId",executorId);
        query.setFirstResult( (pageInfo.getPage()-1) * pageInfo.getRows() );
        query.setMaxResults( pageInfo.getRows() );
        List<FlowHistory>  result = query.getResultList();
        //处理结果
        PageResult<FlowHistory> pageResult = handlePageResult(pageInfo,result,total);
        return pageResult;
    }

    /**
     * 处理结果集
     * @param result    查询的结果集
     * @return
     */
    private List<FlowHistory> initFlowTaskAppModule(List<FlowHistory>  result ){
        if(result!=null && !result.isEmpty()){
            for(FlowHistory flowHistory:result){
                String apiBaseAddressConfig = flowHistory.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getApiBaseAddress();
                String apiBaseAddress =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                if(StringUtils.isNotEmpty(apiBaseAddress)){
                    flowHistory.setApiBaseAddressAbsolute(apiBaseAddress);
                    String[]  tempWebApiBaseAddress = apiBaseAddress.split("/");
                    if(tempWebApiBaseAddress!=null && tempWebApiBaseAddress.length>0){
                        apiBaseAddress = tempWebApiBaseAddress[tempWebApiBaseAddress.length-1];
                        flowHistory.setApiBaseAddress("/" + apiBaseAddress + "/");
                    }
                }
                String webBaseAddressConfig = flowHistory.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel().getAppModule().getWebBaseAddress();
                String webBaseAddress =  ContextUtil.getGlobalProperty(webBaseAddressConfig);
                if(StringUtils.isNotEmpty(webBaseAddress)){
                    flowHistory.setWebBaseAddressAbsolute(webBaseAddress);
                    String[]  tempWebBaseAddress = webBaseAddress.split("/");
                    if(tempWebBaseAddress!=null && tempWebBaseAddress.length>0){
                        webBaseAddress = tempWebBaseAddress[tempWebBaseAddress.length-1];
                        flowHistory.setWebBaseAddress("/" + webBaseAddress + "/");
                    }
                }
            }
        }
        return result;
    }

    /**
     * 处理快速查询条件
     * @param quickSearchValue          快速查询值
     * @param quickSearchProperties     快速查询字段
     * @return
     */
    private String handleQuickSearch(String quickSearchValue,Collection<String> quickSearchProperties){
        String result = "";
        if(StringUtils.isNotEmpty(quickSearchValue) && quickSearchProperties != null && !quickSearchProperties.isEmpty()){
            StringBuffer extraHql = new StringBuffer(" and (");
            boolean first = true;
            for(String s:quickSearchProperties){
                if(first){
                    extraHql.append("  fh." + s + " like '%"+quickSearchValue+"%'");
                    first = false;
                }else {
                    extraHql.append(" or fh." + s + " like '%"+quickSearchValue+"%'");
                }
            }
            extraHql.append(" )");
            result += extraHql.toString();
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
        if(sortOrders != null && sortOrders.size() > 0){
            for(int i = 0;i < sortOrders.size();i++){
                SearchOrder  searchOrder = sortOrders.get(i);
                if(i==0){
                    result += " order by fh."+searchOrder.getProperty() + " " + searchOrder.getDirection();
                }else{
                    result += ", fh." + searchOrder.getProperty() + " " + searchOrder.getDirection();
                }
            }
        }else{
            result += " order by fh.createdDate desc";
        }
        return result;
    }

    /**
     * 处理结果集
     * @param pageInfo          分页参数
     * @param flowHistoryList   流程历史数据
     * @param total             流程任务条数
     * @return
     */
    private PageResult<FlowHistory> handlePageResult(PageInfo pageInfo, List<FlowHistory> flowHistoryList, Long total){
        initFlowTaskAppModule(flowHistoryList);
        PageResult<FlowHistory> pageResult = new PageResult<>();
        pageResult.setPage(pageInfo.getPage());
        pageResult.setRows(flowHistoryList);
        pageResult.setRecords(total.intValue());
        pageResult.setTotal((total.intValue()+pageInfo.getRows()-1)/pageInfo.getRows());
        return pageResult;
    }
}
