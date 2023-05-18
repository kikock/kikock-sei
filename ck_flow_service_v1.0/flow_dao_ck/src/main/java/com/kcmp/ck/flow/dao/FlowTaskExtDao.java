package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.entity.FlowTask;

import java.util.List;

/**
 * Created by kikock
 * 流程任务扩展dao
 * @author kikock
 * @email kikock@qq.com
 **/
public interface FlowTaskExtDao {

    /**
     * 通过业务实体类型id,执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageByBusinessModelId(String businessModelId, String executorId, Search searchConfig);

    /**
     * 通过业务实体类型id,执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体类型id
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageByBusinessModelIdOfPower(String businessModelId, List<String> executorIdList, Search searchConfig);

    /**
     * 通过执行人id,应用标识代码,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId        执行人id
     * @param appSign           应用标识代码
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPage(String executorId, String appSign, Search searchConfig);

    /**
     * 通过执行人id列表,应用标识代码,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorIdList    执行人id列表
     * @param appSign           应用标识代码
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageOfPower(List<String> executorIdList, String appSign, Search searchConfig);

    /**
     * 通过执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorId    执行人id
     * @param searchConfig  组合条件
     * @return
     */
    PageResult<FlowTask> findByPageCanBatchApproval(String executorId, Search searchConfig);

    /**
     * 通过执行人id,动态组合条件查询数据条数
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    Long findCountByExecutorId(String executorId, Search searchConfig);

    /**
     * 通过执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageCanBatchApprovalOfPower(List<String> executorIdList, Search searchConfig);

    /**
     * 通过业务实体id,执行人id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体id
     * @param executorId        执行人id
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageCanBatchApprovalByBusinessModelId(String businessModelId, String executorId, Search searchConfig);

    /**
     * 通过业务实体id,执行人id列表,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param businessModelId   业务实体id
     * @param executorIdList    执行人id列表
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageCanBatchApprovalByBusinessModelIdOfPower(String businessModelId, List<String> executorIdList, Search searchConfig);

    /**
     * 通过应用模块id,业务实体id,流程类型id,基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param appModuleId       应用模块id
     * @param businessModelId   业务实体id
     * @param flowTypeId        流程类型id
     * @param searchConfig      组合条件
     * @return
     */
    PageResult<FlowTask> findByPageByTenant(String appModuleId, String businessModelId, String flowTypeId, Search searchConfig);

    /**
     * 通过待办任务id获取一个待办任务(设置了办理任务URL)
     * @param taskId 待办任务id
     * @return 待办任务
     */
    FlowTask findTaskById(String taskId);

    /**
     * 完成待办任务的URL设置
     * @param flowTasks 待办任务清单
     * @return 待办任务
     */
    void initFlowTasks(List<FlowTask> flowTasks);
}
