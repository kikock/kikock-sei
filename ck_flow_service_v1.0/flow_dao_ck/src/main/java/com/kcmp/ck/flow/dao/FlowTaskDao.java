package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.dto.FlowTaskExecutorIdAndCount;
import com.kcmp.ck.flow.entity.FlowTask;
import com.kcmp.ck.flow.vo.CanAddOrDelNodeInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kikock
 * 流程任务dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowTaskDao extends BaseEntityDao<FlowTask>, FlowTaskExtDao {

    /**
     * 删除没有进行任务签收的任务
     * @param actTaskId 关联流程引擎实际的任务id
     * @param idExclude 排除的id,一般是指当前进行任务签收的任务
     * @return
     */
    @Modifying
    @Transactional(readOnly = true)
    @Query("delete from FlowTask fv where (fv.actTaskId = :actTaskId) and (fv.id <> :idExclude) ")
    Integer deleteNotClaimTask(@Param("actTaskId") String actTaskId, @Param("idExclude") String idExclude);

    /**
     * 删除任务
     * @param actTaskId 关联流程引擎实际的任务id
     * @return
     */
    @Modifying
    @Transactional(readOnly = true)
    long deleteByActTaskId(String actTaskId);

    /**
     * 删除任务
     * @param flowInstanceId    流程实例id
     * @return
     */
    @Modifying
    @Transactional(readOnly = true)
    long deleteByFlowInstanceId(String flowInstanceId);

    /**
     * 查询任务
     * @param actTaskId 关联流程引擎实际的任务id
     * @return
     */
    FlowTask findByActTaskId(String actTaskId);

    /**
     * 查询任务
     * @param actTaskDefKey 任务定义key
     * @param actInstanceId 关联的实际流程引擎实例id
     * @return
     */
    @Query("select ft from FlowTask ft where ft.flowInstance.actInstanceId  = :actInstanceId and ft.actTaskDefKey = :actTaskDefKey")
    List<FlowTask> findByActTaskDefKeyAndActInstanceId(@Param("actTaskDefKey") String actTaskDefKey, @Param("actInstanceId") String actInstanceId);

    /**
     * 查询任务
     * @param actTaskDefKey 任务定义key
     * @param actInstanceId 关联的实际流程引擎实例id
     * @param executorId    执行人id
     * @return
     */
    @Query("select ft from FlowTask ft where ft.flowInstance.actInstanceId  = :actInstanceId and ft.actTaskDefKey = :actTaskDefKey and ft.executorId = :executorId")
    List<FlowTask> findByActTaskDefKeyAndActInstanceIdAndExecutorId(@Param("actTaskDefKey") String actTaskDefKey, @Param("actInstanceId") String actInstanceId, @Param("executorId") String executorId);

    /**
     * 查询任务条数
     * @param actTaskDefKey 任务定义key
     * @param actInstanceId 关联的实际流程引擎实例id
     * @return
     */
    @Query("select count(ft.id) from FlowTask ft where ft.flowInstance.actInstanceId  = :actInstanceId and ft.actTaskDefKey = :actTaskDefKey")
    Integer findCountByActTaskDefKeyAndActInstanceId(@Param("actTaskDefKey") String actTaskDefKey, @Param("actInstanceId") String actInstanceId);

    /**
     * 查询任务
     * @param instanceId    流程实例id
     * @return
     */
    @Query("select ft from FlowTask ft where ft.flowInstance.id  = :instanceId")
    List<FlowTask> findByInstanceId(@Param("instanceId") String instanceId);

    /**
     *根据执行人账号归类查询
     * @param executorId    执行人id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefinitionId from FlowTask ft where ft.executorId  = :executorId  and (ft.trustState !=1  or ft.trustState is null ) group by ft.flowDefinitionId")
    List findByExecutorIdGroup(@Param("executorId") String executorId);

    /**
     * 根据执行人集合查询归类查询(转授权)
     * @param executorIdList    执行人id列表
     * @return
     */
    @Query("select count(ft.id),ft.flowDefinitionId from FlowTask ft where ft.executorId  in (:executorIdList) and (ft.trustState != 1 or ft.trustState is null ) group by ft.flowDefinitionId")
    List findByExecutorIdGroupOfPower(@Param("executorIdList") List<String> executorIdList);


    /**
     * 根据执行人集合查询待办总数(转授权)
     * @param executorIdList    执行人id列表
     * @return
     */
    @Query("select count(ft.id) from FlowTask ft where ft.executorId  in (:executorIdList)  and (ft.trustState != 1 or ft.trustState is null )")
    Integer findTodoSumByExecutorIds(@Param("executorIdList") List<String> executorIdList);

    /**
     * 根据执行人账号归类查询,可批量审批
     * @param executorId    执行人id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefinitionId from FlowTask ft where ft.executorId  = :executorId and ft.canBatchApproval = true and (ft.trustState !=1  or ft.trustState is null )   group by ft.flowDefinitionId")
    List findByExecutorIdGroupCanBatchApproval(@Param("executorId") String executorId);

    /**
     * 根据执行人集合查询,可批量审批(转授权)
     * @param executorIdList    执行人id列表
     * @return
     */
    @Query("select count(ft.id),ft.flowDefinitionId from FlowTask ft where ft.executorId  in (:executorIdList) and ft.canBatchApproval = true and (ft.trustState !=1  or ft.trustState is null )   group by ft.flowDefinitionId")
    List findByExecutorIdGroupCanBatchApprovalOfPower(@Param("executorIdList") List<String> executorIdList);

    /**
     * 查询可以加签的待办-针对启动
     * @param executorId    执行人id
     * @return
     */
    @Query("select new com.kcmp.ck.flow.vo.CanAddOrDelNodeInfo( ft.flowInstance.actInstanceId,ft.actTaskDefKey,ft.taskName,ft.flowInstance.businessId,ft.flowInstance.businessCode,ft.flowInstance.businessName,ft.flowInstance.businessModelRemark,ft.flowInstance.flowName,ft.flowInstance.flowDefVersion.defKey) from FlowTask ft where ft.allowAddSign  = true and (ft.preId is null and ft.flowInstance.creatorId = :executorId)")
    List<CanAddOrDelNodeInfo> findByAllowAddSignStart(@Param("executorId") String executorId);

    /**
     * 查询可以加签的待办-针对非启动
     * @param executorId    执行人id
     * @return
     */
    @Query("select new com.kcmp.ck.flow.vo.CanAddOrDelNodeInfo( ft.flowInstance.actInstanceId,ft.actTaskDefKey,ft.taskName,ft.flowInstance.businessId,ft.flowInstance.businessCode,ft.flowInstance.businessName,ft.flowInstance.businessModelRemark,ft.flowInstance.flowName,ft.flowInstance.flowDefVersion.defKey) from FlowTask ft inner join  FlowHistory fh on ft.preId = fh.id where ft.allowAddSign  = true and fh.executorId = :executorId")
    List<CanAddOrDelNodeInfo> findByAllowAddSign(@Param("executorId") String executorId);

    /**
     * 查询可以减签的待办-针对启动
     * @param executorId    执行人id
     * @return
     */
    @Query("select new com.kcmp.ck.flow.vo.CanAddOrDelNodeInfo( ft.flowInstance.actInstanceId,ft.actTaskDefKey,ft.taskName,ft.flowInstance.businessId,ft.flowInstance.businessCode,ft.flowInstance.businessName,ft.flowInstance.businessModelRemark,ft.flowInstance.flowName,ft.flowInstance.flowDefVersion.defKey) from FlowTask ft where ft.allowSubtractSign  = true and (ft.preId is null and ft.flowInstance.creatorId = :executorId)")
    List<CanAddOrDelNodeInfo> findByAllowSubtractSignStart(@Param("executorId") String executorId);

    /**
     * 查询可以减签的待办-针对非启动
     * @param executorId    执行人id
     * @return
     */
    @Query("select new com.kcmp.ck.flow.vo.CanAddOrDelNodeInfo( ft.flowInstance.actInstanceId,ft.actTaskDefKey,ft.taskName,ft.flowInstance.businessId,ft.flowInstance.businessCode,ft.flowInstance.businessName,ft.flowInstance.businessModelRemark,ft.flowInstance.flowName,ft.flowInstance.flowDefVersion.defKey) from FlowTask ft inner join  FlowHistory fh on ft.preId = fh.id where ft.allowSubtractSign  = true and  fh.executorId = :executorId")
    List<CanAddOrDelNodeInfo> findByAllowSubtractSign(@Param("executorId") String executorId);

    /**
     * 查询执行人待办统计
     * @return
     */
    @Query("select new com.kcmp.ck.flow.dto.FlowTaskExecutorIdAndCount(ft.executorId,count(ft.id)) from FlowTask ft group by ft.executorId")
    List<FlowTaskExecutorIdAndCount> findAllExecutorIdAndCount();

    /**
     * 查询执行人待办统计
     * @param executorId    执行人id
     * @return
     */
    @Query("select new com.kcmp.ck.flow.dto.FlowTaskExecutorIdAndCount(ft.executorId,ft.executorName,ft.flowDefinitionId,ft.flowName,ft.actTaskDefKey,ft.taskName,count(ft.id)) from FlowTask ft where ft.executorId = :executorId group by ft.executorId,ft.executorName,ft.flowDefinitionId,ft.flowName,ft.actTaskDefKey,ft.taskName")
    List<FlowTaskExecutorIdAndCount> findAllTaskKeyAndCountByExecutorId(@Param("executorId") String executorId);
}
