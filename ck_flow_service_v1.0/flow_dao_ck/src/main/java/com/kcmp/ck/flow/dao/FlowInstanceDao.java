package com.kcmp.ck.flow.dao;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.ck.flow.entity.FlowInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;

/**
 * Created by kikock
 * 流程实例dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface FlowInstanceDao extends BaseEntityDao<FlowInstance> {

    /**
     * 通过业务单据id查询没有结束并且没有挂起的流程实例
     * @param businessId 业务单据id
     * @return 流程实例集合
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select ft from FlowInstance ft where ft.suspended = false and ft.ended = false and ft.businessId = :businessId order by ft.lastEditedDate desc")
    List<FlowInstance> findNoEndByBusinessIdOrder(@Param("businessId") String businessId);

    /**
     * 查询流程实例
     * @param actInstanceId 关联的实际流程引擎实例id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select ft from FlowInstance ft where ft.actInstanceId = :actInstanceId")
    FlowInstance findByActInstanceId(@Param("actInstanceId") String actInstanceId);

    /**
     * 根据业务id查询流程实例
     * @param businessId    业务id
     * @return
     */
    List<FlowInstance> findByBusinessId(String businessId);

    /**
     * 根据业务id查询流程实例，最后修改时间降序排列
     * @param businessId    业务id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("select ft from FlowInstance ft where ft.businessId = :businessId order by ft.lastEditedDate desc")
    List<FlowInstance> findByBusinessIdOrder(@Param("businessId") String businessId);

    /**
     * 根据业务id查询未结束的流程实例
     * @param businessId    业务id
     * @return
     */
    @Query("select ft from FlowInstance ft where ft.businessId = :businessId and ft.ended = false")
    FlowInstance findByBusinessIdNoEnd(@Param("businessId") String businessId);

    /**
     * 根据业务id列表查询未结束的流程实例列表
     * @param businessIds   业务id列表
     * @return
     */
    @Query("select ft from FlowInstance ft where  ft.businessId in  :businessIds and ft.ended = false  ")
    List<FlowInstance> findByBusinessIdListNoEnd(@Param("businessIds") List<String> businessIds);

    /**
     * 根据父流程实例id查询子流程实例
     * @param parentId  父流程实例id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<FlowInstance> findByParentId(String parentId);

    /**
     * 根据所属流程定义版本id查询流程实例
     * @param flowDefVersionId  所属流程定义版本id
     * @return
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<FlowInstance> findByFlowDefVersionId(String flowDefVersionId);

    /**
     * 根据创建用户id查询流程定义id和条数
     * @param creatorId 创建用户id
     * @return
     */
    @Query("select count(ft.id),ft.flowDefVersion.flowDefination.id from FlowInstance ft where ft.creatorId  = :creatorId  group by ft.flowDefVersion.flowDefination.id")
    List findBillsByGroup(@Param("creatorId") String creatorId);

    /**
     * 根据创建用户id和应用模块代码查询流程定义id和条数
     * @param creatorId     创建用户id
     * @param appModelCode  应用模块代码
     * @return
     */
    @Query("select count(ft.id),ft.flowDefVersion.flowDefination.id from FlowInstance ft where ft.creatorId  = :creatorId and  ft.flowDefVersion.flowDefination.flowType.businessModel.appModule.code = :appModelCode   group by ft.flowDefVersion.flowDefination.id")
    List findBillsByGroupAndAppCode(@Param("creatorId") String creatorId, @Param("appModelCode") String appModelCode);

    /**
     * 根据创建用户id、是否结束和是否手动结束查询流程定义id和条数
     * @param creatorId     创建用户id
     * @param ended         是否结束
     * @param manuallyEnd   是否手动结束
     * @return
     */
    @Query("select count(ft.id),ft.flowDefVersion.flowDefination.id from FlowInstance ft where ft.creatorId  = :creatorId and ft.ended = :ended  and  ft.manuallyEnd = :manuallyEnd  group by ft.flowDefVersion.flowDefination.id")
    List findBillsByExecutorIdGroup(@Param("creatorId") String creatorId, @Param("ended") Boolean ended, @Param("manuallyEnd") Boolean manuallyEnd);

    /**
     * 根据创建用户id、是否结束、是否手动结束和应用模块代码查询流程定义id和条数
     * @param creatorId     创建用户id
     * @param ended         是否结束
     * @param manuallyEnd   是否手动结束
     * @param appModelCode  应用模块代码
     * @return
     */
    @Query("select count(ft.id),ft.flowDefVersion.flowDefination.id from FlowInstance ft where ft.creatorId  = :creatorId and ft.ended = :ended  and  ft.manuallyEnd = :manuallyEnd and ft.flowDefVersion.flowDefination.flowType.businessModel.appModule.code = :appModelCode  group by ft.flowDefVersion.flowDefination.id")
    List findBillsByExecutorIdGroupAndAppCode(@Param("creatorId") String creatorId, @Param("ended") Boolean ended, @Param("manuallyEnd") Boolean manuallyEnd, @Param("appModelCode") String appModelCode);

    /**
     * 根据创建用户id、是否结束、开始时间和结束时间查询流程定义id和条数
     * @param creatorId 创建用户id
     * @param ended     是否结束
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @Query("select count(ft.id) from FlowInstance ft where ft.creatorId  = :creatorId and ft.ended = :ended and ft.startDate>= :startDate and ft.endDate<= :endDate")
    Integer getBillsSum(@Param("creatorId") String creatorId, @Param("ended") Boolean ended, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
