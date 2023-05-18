package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseRelationDao;
import com.kcmp.core.ck.service.BaseRelationService;
import com.kcmp.ck.flow.api.IFlowTaskControlAndPushService;
import com.kcmp.ck.flow.dao.FlowTaskControlAndPushDao;
import com.kcmp.ck.flow.entity.FlowTaskControlAndPush;
import com.kcmp.ck.flow.entity.FlowTaskPush;
import com.kcmp.ck.flow.entity.FlowTaskPushControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kikock
 * 推送任务关系服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowTaskControlAndPushService extends BaseRelationService<FlowTaskControlAndPush, FlowTaskPushControl, FlowTaskPush>
        implements IFlowTaskControlAndPushService {

    @Autowired
    private FlowTaskControlAndPushDao dao;

    @Override
    public BaseRelationDao<FlowTaskControlAndPush, FlowTaskPushControl, FlowTaskPush> getDao() {
        return dao;
    }

    @Autowired
    public FlowTaskPushService flowTaskPushService;

    /**
     * 获取可以分配的子实体清单
     * @return 子实体清单
     */
    @Override
    protected List<FlowTaskPush> getCanAssignedChildren(String parentId) {
        return super.getChildrenFromParentId(parentId);
    }
}
