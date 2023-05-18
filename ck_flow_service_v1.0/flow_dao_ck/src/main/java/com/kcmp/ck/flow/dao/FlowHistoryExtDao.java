package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowHistory;

import java.util.List;

/**
 * Created by kikock
 * 流程历史扩展dao
 * @author kikock
 * @email kikock@qq.com
 **/
public interface FlowHistoryExtDao {

    /**
     * 通过业务实体类型id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorId        执行人账号
     * @param searchConfig      组合条件对象
     * @return
     */
    PageResult<FlowHistory> findByPageByBusinessModelId(String businessModelId, String executorId, Search searchConfig);

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorAccount   执行人账号
     * @param searchConfig      组合条件对象
     * @return
     */
    PageResult<FlowHistory> findByPageByBusinessModelId(String executorAccount, Search searchConfig);

    /**
     * 根据业务实体类型id，业务单据id，获取最新流程实体执行的待办，不包括撤销之前的历史任务
     * @param businessId    业务实体类型id
     * @return
     */
    List<FlowHistory> findLastByBusinessId(String businessId);

    /**
     * 查询所有转授权历史
     * @return
     */
    List<FlowHistory>  findByAllTaskMakeOverPowerHistory();

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId    执行人账号
     * @param searchConfig  组合条件对象
     * @return
     */
    PageResult<FlowHistory> findByPage(String executorId, Search searchConfig);
}
