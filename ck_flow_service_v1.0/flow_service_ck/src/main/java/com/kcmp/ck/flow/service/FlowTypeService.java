package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.*;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.flow.api.IFlowTypeService;
import com.kcmp.ck.flow.basic.vo.AppModule;
import com.kcmp.ck.flow.dao.FlowTypeDao;
import com.kcmp.ck.flow.entity.FlowType;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.vo.SearchVo;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by kikock
 * 流程类型服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowTypeService extends BaseEntityService<FlowType> implements IFlowTypeService {

    @Autowired
    private FlowTypeDao flowTypeDao;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Override
    protected BaseEntityDao<FlowType> getDao(){
        return this.flowTypeDao;
    }

    /**
     * 根据业务实体id查询流程类型
     * @param businessModelId 业务实体id
     * @return 实体清单
     */
    @Override
    public List<FlowType> findByBusinessModelId(String businessModelId) {
        return flowTypeDao.findByBusinessModelId(businessModelId);
    }

    /**
     * 保存流程类型
     * @param flowType  流程类型数据
     * @return
     */
    @Override
    public OperateResultWithData<FlowType> save(FlowType flowType){
        OperateResultWithData<FlowType> resultWithData = null;
        try {
            resultWithData = super.save(flowType);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            e.printStackTrace();
            SQLException sqlException = (SQLException)e.getCause().getCause();
            if(sqlException!=null && "23000".equals(sqlException.getSQLState())){
                return OperateResultWithData.operationFailure("10028");
            }else {
                throw  e;
            }
        }
        clearFlowDefVersion();
        return resultWithData;
    }

    private void clearFlowDefVersion(){
        String pattern = "FLowGetLastFlowDefVersion_*";
        if(redisTemplate!=null){
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys!=null&&!keys.isEmpty()){
                redisTemplate.delete(keys);
            }
        }
    }

    /**
     * 获取流程类型分页数据
     * @return 实体清单
     */
    @Override
    public PageResult<FlowType> listFlowType(SearchVo searchVo){
        Search search = new Search();
        search.addQuickSearchProperty("code");
        search.addQuickSearchProperty("name");
        search.addQuickSearchProperty("businessModel.name");
        if(StringUtils.isNotEmpty(searchVo.getQuick_value())){
            search.setQuickSearchValue(searchVo.getQuick_value());
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(searchVo.getPage());
        pageInfo.setRows(searchVo.getRows());
        search.setPageInfo(pageInfo);

        if(StringUtils.isNotEmpty(searchVo.getSidx())){
            if("asc".equals(searchVo.getSord())){
                search.addSortOrder(SearchOrder.asc(searchVo.getSidx()));
            }else{
                search.addSortOrder(SearchOrder.desc(searchVo.getSidx()));
            }
        }
     return  this.findByPage(search);
    }

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return 实体清单
     */
    @Override
    public PageResult<FlowType> findByPage(Search searchConfig){
        List<AppModule> appModuleList = null;
        List<String > appModuleCodeList = null;
        appModuleList = flowCommonUtil.getBasicTenantAppModule();
        if(appModuleList!=null && !appModuleList.isEmpty()){
            appModuleCodeList = new ArrayList<String>();
            for(AppModule appModule:appModuleList){
                appModuleCodeList.add(appModule.getCode());
            }
        }
        if(appModuleCodeList!=null && !appModuleCodeList.isEmpty()){
            SearchFilter searchFilter = new SearchFilter("businessModel.appModule.code", appModuleCodeList, SearchFilter.Operator.IN);
            searchConfig.addFilter(searchFilter);
        }
        PageResult<FlowType> result = flowTypeDao.findByPage(searchConfig);
        return result;
    }

    /**
     * 主键删除判断
     * @param id 主键
     * @return 返回操作结果对象
     */
    @Override
    public OperateResult delete(String id) {
        OperateResult operateResult = preDelete(id);
        if (Objects.isNull(operateResult) || operateResult.successful()) {
            FlowType entity = findOne(id);
            if (entity != null) {
                try {
                    getDao().delete(entity);
                }catch (org.springframework.dao.DataIntegrityViolationException e){
                    e.printStackTrace();
                    SQLException sqlException = (SQLException)e.getCause().getCause();
                    if(sqlException!=null && "23000".equals(sqlException.getSQLState())){
                        return OperateResult.operationFailure("10027");
                    }else {
                        throw  e;
                    }
                }
                // 流程类型删除成功！
                return OperateResult.operationSuccess("10062");
            } else {
                // 流程类型{0}不存在！
                return OperateResult.operationWarning("10063");
            }
        }
        clearFlowDefVersion();
        return operateResult;
    }
}
