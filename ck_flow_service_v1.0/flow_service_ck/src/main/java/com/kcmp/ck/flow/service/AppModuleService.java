package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.flow.api.IAppModuleService;
import com.kcmp.ck.flow.dao.AppModuleDao;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.vo.OperateResultWithData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by kikock
 * 应用模块服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class AppModuleService extends BaseEntityService<AppModule> implements IAppModuleService {

    @Autowired
    private AppModuleDao appModuleDao;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Override
    protected BaseEntityDao<AppModule> getDao() {
        return appModuleDao;
    }

    /**
     * 数据保存操作
     */
    @Override
    @SuppressWarnings("unchecked")
    public OperateResultWithData<AppModule> save(AppModule entity) {
        OperateResultWithData<AppModule> operateResultWithData = super.save(entity);
        clearFlowDefVersion();
        return operateResultWithData;
    }

    /**
     * 清除流程定义版本缓存
     */
    private void clearFlowDefVersion() {
        String pattern = "FLowGetLastFlowDefVersion_*";
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    /**
     * 分页查询应用服务
     * @param searchConfig  查询参数
     * @return
     */
    @Override
    public PageResult<AppModule> findByPage(Search searchConfig) {
        List<com.kcmp.ck.flow.basic.vo.AppModule> appModuleList = null;
        List<String> appModuleCodeList = null;
        appModuleList = flowCommonUtil.getBasicTenantAppModule();
        if (appModuleList != null && !appModuleList.isEmpty()) {
            appModuleCodeList = new ArrayList<String>();
            for (com.kcmp.ck.flow.basic.vo.AppModule appModule : appModuleList) {
                appModuleCodeList.add(appModule.getCode());
            }
        }
        if (appModuleCodeList != null && !appModuleCodeList.isEmpty()) {
            SearchFilter searchFilter = new SearchFilter("code", appModuleCodeList, SearchFilter.Operator.IN);
            searchConfig.addFilter(searchFilter);
        }
        PageResult<AppModule> result = appModuleDao.findByPage(searchConfig);
        return result;
    }

    /**
     * 获取当前用户权限范围所有
     * @return 实体清单
     */
    @Override
    public List<AppModule> findAllByAuth() {
        List<AppModule> result = null;
        List<com.kcmp.ck.flow.basic.vo.AppModule> appModuleList = null;
        List<String> appModuleCodeList = null;
        try {
            appModuleList =  flowCommonUtil.getBasicTenantAppModule();
            if (appModuleList != null && !appModuleList.isEmpty()) {
                appModuleCodeList = new ArrayList<String>();
                for (com.kcmp.ck.flow.basic.vo.AppModule appModule : appModuleList) {
                    appModuleCodeList.add(appModule.getCode());
                }
            }
            if (appModuleCodeList != null && !appModuleCodeList.isEmpty()) {
                result = appModuleDao.findByCodes(appModuleCodeList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = appModuleDao.findAll();
        }
        return result;
    }

}
