package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseRelationDao;
import com.kcmp.ck.flow.entity.FlowTaskControlAndPush;
import com.kcmp.ck.flow.entity.FlowTaskPush;
import com.kcmp.ck.flow.entity.FlowTaskPushControl;
import org.springframework.stereotype.Repository;

/**
 * Created by kikock
 * 推送任务、推送任务记录父表、推送任务记录子表关联dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowTaskControlAndPushDao extends BaseRelationDao<FlowTaskControlAndPush, FlowTaskPushControl, FlowTaskPush> {

}
