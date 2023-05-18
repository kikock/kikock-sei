package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.flow.api.IFlowServiceUrlService;
import com.kcmp.ck.flow.dao.FlowServiceUrlDao;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kikock
 * 流程服务地址服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowServiceUrlService extends BaseEntityService<FlowServiceUrl> implements IFlowServiceUrlService {

    @Autowired
    private FlowServiceUrlDao flowServiceUrlDao;

    @Override
    protected BaseEntityDao<FlowServiceUrl> getDao(){
        return this.flowServiceUrlDao;
    }

    /**
     * 通过流程类型id查找拥有的服务方法
     * @param flowTypeId 流程类型id
     * @return 服务方法list
     */
    @Override
    public List<FlowServiceUrl> findByFlowTypeId(String flowTypeId){
        return flowServiceUrlDao.findByFlowTypeId(flowTypeId);
    }

    /**
     * 通过业务实体id查找拥有的服务方法
     * @param businessModelId 业务实体id
     * @return 服务方法list
     */
    @Override
    public List<FlowServiceUrl> findByBusinessModelId(String businessModelId){
        return flowServiceUrlDao.findByBusinessModelId(businessModelId);
    }
}
