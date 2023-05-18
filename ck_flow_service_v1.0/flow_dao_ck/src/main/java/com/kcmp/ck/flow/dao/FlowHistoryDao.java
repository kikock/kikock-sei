package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kikock
 * 流程历史dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowHistoryDao extends BaseEntityDao<FlowHistory>, FlowHistoryExtDao {

    /**
     * 根据流程实例id删除流程历史
     * @param flowInstanceId    流程实例id
     * @return
     */
    @Transactional(readOnly = true)
    long deleteByFlowInstanceId(String flowInstanceId);

    /**
     * 根据执行人id归类查询历史
     * @param executorId    执行人id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefId from FlowHistory ft where ft.executorId  = :executorId  group by ft.flowDefId ")
    List findHisByExecutorIdGroup(@Param("executorId") String executorId);

    /**
     * 根据执行人id归类查询历史(有效数据)
     * @param executorId    执行人id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefId from FlowHistory ft where ft.executorId  = :executorId  and  ( ft.flowExecuteStatus in ('submit','agree','disagree','turntodo','entrust','recall','reject') or  ft.flowExecuteStatus is null )  group by ft.flowDefId ")
    List findHisByExecutorIdGroupValid(@Param("executorId") String executorId);

    /**
     * 根据执行人id归类查询历史(记录数据)
     * @param executorId    执行人id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefId from FlowHistory ft where ft.executorId  = :executorId and   ft.flowExecuteStatus in ('end','auto')   group by ft.flowDefId")
    List findHisByExecutorIdGroupRecord(@Param("executorId") String executorId);

    /**
     * 根据流程实例id查询流程历史
     * @param instanceId    流程实例id
     * @return
     */
    @Query("select fh from FlowHistory fh where fh.flowInstance.id  = :instanceId order by fh.actEndTime asc")
    List<FlowHistory> findByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 根据业务实体类型id，业务单据id，获取所有业务执行的待办，
     * 包括撤销之前的历史任务以及不同流程类型、不同流程定义、不同版本的数据
     * @param businessId    业务实体类型id
     * @return
     */
    @Query("select ft from FlowHistory ft where ft.flowInstance.id in(select fi.id from FlowInstance fi where fi.businessId = :businessId ) order by ft.lastEditedDate desc")
    List<FlowHistory> findAllByBusinessId(@Param("businessId") String businessId);

    /**
     * 查询流程历史
     * @param actTaskDefKey 流程引擎的实际任务定义key
     * @param actInstanceId 关联的实际流程引擎实例ID
     * @return
     */
    @Query("select ft from FlowHistory ft where ft.flowInstance.actInstanceId  = :actInstanceId and ft.actTaskDefKey = :actTaskDefKey")
    List<FlowHistory> findByActTaskDefKeyAndActInstanceId(@Param("actTaskDefKey") String actTaskDefKey, @Param("actInstanceId") String actInstanceId);

    /**
     * 查询流程历史条数
     * @param actTaskDefKey 流程引擎的实际任务定义key
     * @param actInstanceId 关联的实际流程引擎实例ID
     * @return
     */
    @Query("select count(ft.id) from FlowHistory ft where ft.flowInstance.actInstanceId  = :actInstanceId and ft.actTaskDefKey = :actTaskDefKey order by ft.createdDate desc")
    Integer findCountByActTaskDefKeyAndActInstanceId(@Param("actTaskDefKey") String actTaskDefKey, @Param("actInstanceId") String actInstanceId);
}
