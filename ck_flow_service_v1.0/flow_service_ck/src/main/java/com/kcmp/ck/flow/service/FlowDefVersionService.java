package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchFilter;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.IFlowDefVersionService;
import com.kcmp.ck.flow.constant.FlowDefinationStatus;
import com.kcmp.ck.flow.dao.*;
import com.kcmp.ck.flow.dao.AppModuleDao;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowDefinationDao;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.dao.FlowTypeDao;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowDefination;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowType;
import com.kcmp.ck.flow.util.FlowTaskTool;
import com.kcmp.ck.flow.util.XmlUtil;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.flow.vo.bpmn.Process;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import org.activiti.engine.ProcessEngine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by kikock
 * 流程定义版本服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowDefVersionService extends BaseEntityService<FlowDefVersion> implements IFlowDefVersionService {

    @Override
    protected BaseEntityDao<FlowDefVersion> getDao() {
        return this.flowDefVersionDao;
    }

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private FlowDefinationDao flowDefinationDao;

    @Autowired
    private FlowDefinationService flowDefinationService;

    @Autowired
    private FlowTypeDao flowTypeDao;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private FlowTaskTool flowTaskTool;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private AppModuleDao appModuleDao;

    /**
     * 新增修改操作
     * @param entity
     * @return
     */
    @Override
    public OperateResultWithData<FlowDefVersion> save(FlowDefVersion entity) {
        FlowDefination flowDefination = entity.getFlowDefination();
        if (flowDefination == null) { //流程版本必须指定流程定义
            return null;
        }
        flowDefination = flowDefinationDao.findOne(flowDefination.getId());
        boolean isNew = entity.isNew();
        if (entity.isNew()) {
            preInsert(entity);
        } else {
            preUpdate(entity);
            entity.setId(null);//更改时默认版本向上加+,重新建立一条版本数据
            entity.setVersionCode(entity.getVersionCode() + 1);
        }
        entity.setFlowDefination(flowDefination);
        flowDefVersionDao.save(entity);
        LogUtil.info("Saved FlowDefVersion id is {}", entity.getId());
        flowDefination.setLastVersionId(entity.getId());
        flowDefinationDao.save(flowDefination);
        LogUtil.info("Saved FlowDefination id is {}", flowDefination.getId());
        OperateResultWithData<FlowDefVersion> operateResult;
        // 流程版本保存成功！
        operateResult = OperateResultWithData.operationSuccess("10056");
        operateResult.setData(entity);
        clearFlowDefVersion();
        return operateResult;
    }

    /**
     * 切换版本状态
     * @param id 单据id
     * @param status 状态
     * @return 结果
     */
    @Override
    public OperateResultWithData<FlowDefVersion> changeStatus(String id, FlowDefinationStatus status) {
        FlowDefVersion flowDefVersion = flowDefVersionDao.findOne(id);
        if (flowDefVersion == null) {
            return OperateResultWithData.operationFailure("10003");
        }
        if (status == FlowDefinationStatus.Freeze) {
            if (flowDefVersion.getFlowDefinationStatus() != FlowDefinationStatus.Activate) {
                //10021=当前非激活状态，禁止冻结！
                return OperateResultWithData.operationFailure("10021");
            }
        } else if (status == FlowDefinationStatus.Activate) {
            if (flowDefVersion.getFlowDefinationStatus() != FlowDefinationStatus.Freeze) {
                //10020=当前非冻结状态，禁止激活！
                return OperateResultWithData.operationFailure("10020");
            }
        }
        OperateResultWithData resultWithData = flowTaskTool.statusCheck(status, flowDefVersion.getFlowDefinationStatus());
        if (resultWithData != null && resultWithData.notSuccessful()) {
            return resultWithData;
        }
        flowDefVersion.setFlowDefinationStatus(status);
        flowDefVersionDao.save(flowDefVersion);
        //10018=冻结成功
        //10019=激活成功
        return OperateResultWithData.operationSuccess(status == FlowDefinationStatus.Freeze ? "10018" : "10019");
    }

    /**
     * 获取分页数据
     * @param searchConfig  查询参数
     * @return 实体清单
     */
    @Override
    public PageResult<FlowDefVersion> findByPage(Search searchConfig){
        List<SearchFilter> filters = searchConfig.getFilters();
        SearchFilter  filter =new SearchFilter();
        filter.setFieldName("versionCode");
        filter.setOperator(SearchFilter.Operator.GE);
        filter.setValue(1);
        filter.setFieldType("Integer");
        filters.add(filter);
        return super.findByPage(searchConfig);
    }

    /**
     * 通过json流程定义数据，保存流程版本定义
     * @param definition json对象实体
     * @throws    JAXBException  jaxb解析异常
     * @throws    CloneNotSupportedException 复制异常
     * @return 保存后的流程版本定义实体
     */
    public OperateResultWithData<FlowDefVersion> save(Definition definition) throws JAXBException, CloneNotSupportedException {
        String flowTypeId = definition.getFlowTypeId();
        FlowType flowType = flowTypeDao.findOne(flowTypeId);
        if (flowType == null) { //流程版本必须指定流程类型
            return OperateResultWithData.operationFailure("10007");
        }
        Process process = definition.getProcess();
        FlowDefination flowDefination = null;
        Boolean canAsSubProcess = definition.getSubProcess();
        Boolean canAssolidifyFlow =  definition.getSolidifyFlow();
        flowDefination = flowDefinationDao.findByDefKey(process.getId());
        if(StringUtils.isEmpty(definition.getId())&&flowDefination!=null){  //新增的流程定义
            return OperateResultWithData.operationFailure("流程代码重复！");
        }
        String defBpm = XmlUtil.serialize(definition);
        FlowDefVersion entity = null;

        String defJson = definition.getDefJson();
        try{
            ResponseData responseData = flowDefinationService.resetPositionByJson(defJson);
            if(responseData.getSuccess()){
                defJson = (String)responseData.getData();
            }
        }catch (Exception e){
            LogUtil.error("重置位置报错！",e);
        }

        if (flowDefination == null) {//定义为空

            flowDefination = new FlowDefination();
            flowDefination.setTenantCode(ContextUtil.getTenantCode());
            entity = new FlowDefVersion();
            entity.setTenantCode(ContextUtil.getTenantCode());
            flowDefination.setName(process.getName());
            flowDefination.setDefKey(process.getId());
            if (process.getStartUEL() != null) {
                // flowDefination.setStartUel(process.getStartUEL().toString());
                entity.setStartUel(process.getStartUEL().toString());
            }
            flowDefination.setFlowType(flowType);
            flowDefination.setOrgId(definition.getOrgId());
            flowDefination.setOrgCode(definition.getOrgCode());
            // flowDefination.setCurrentFlowDefVersion(1L);I
            flowDefination.setFlowDefinationStatus(FlowDefinationStatus.INIT);
            flowDefination.setPriority(definition.getPriority());
            flowDefination.setSubProcess(canAsSubProcess);
            flowDefination.setSolidifyFlow(canAssolidifyFlow);
            flowDefinationDao.save(flowDefination);

            entity.setFlowDefinationStatus(FlowDefinationStatus.INIT);
            entity.setActDefId(process.getId());
            entity.setName(process.getName());
            entity.setDefKey(process.getId());
            entity.setStartCheckServiceUrlId(process.getBeforeStartServiceId());
            entity.setStartCheckServiceUrlName(process.getBeforeStartServiceName());
            entity.setStartCheckServiceAync(process.getBeforeStartServiceAync());
            entity.setAfterStartServiceId(process.getAfterStartServiceId());
            entity.setAfterStartServiceName(process.getAfterStartServiceName());
            entity.setAfterStartServiceAync(process.getAfterStartServiceAync());
            entity.setEndBeforeCallServiceUrlId(process.getBeforeEndServiceId());
            entity.setEndBeforeCallServiceUrlName(process.getBeforeEndServiceName());
            entity.setEndBeforeCallServiceAync(process.getBeforeEndServiceAync());
            entity.setEndCallServiceUrlId(process.getAfterEndServiceId());
            entity.setEndCallServiceUrlName(process.getAfterEndServiceName());
            entity.setEndCallServiceAync(process.getAfterStartServiceAync());
//            entity.setVersionCode(1);
            entity.setFlowDefination(flowDefination);
            entity.setDefJson(defJson);
//            entity.setDefBpmn(defBpm);
            entity.setDefXml(defBpm);
            entity.setPriority(definition.getPriority());
            entity.setSubProcess(canAsSubProcess);
            entity.setSolidifyFlow(canAssolidifyFlow);

            flowDefVersionDao.save(entity);
            LogUtil.info("Saved FlowDefVersion id is {}", entity.getId());
            flowDefination.setLastVersionId(entity.getId());
            flowDefinationDao.save(flowDefination);
            LogUtil.info("Saved FlowDefination id is {}", flowDefination.getId());
        } else {
            if (StringUtils.isNoneEmpty(process.getFlowDefVersionId())) {
                entity = flowDefVersionDao.findOne(process.getFlowDefVersionId());
            }

            if (entity != null) {//版本不为空
                if (!entity.getDefKey().equals(process.getId())) {
//                    throw new RuntimeException("版本key与当前流程定义key不一致！");
                    entity = new FlowDefVersion();
                    entity.setActDefId(process.getId());
                    entity.setDefKey(process.getId());
                } else {
                    if (StringUtils.isNotEmpty(entity.getActDeployId())) {//对于已经有发布ID的对象进行拷贝
                        FlowDefVersion old = entity;
                        entity = new FlowDefVersion();
                        entity.setActDefId(old.getId());
                        entity.setDefKey(old.getDefKey());
                    } else {
                        entity.setActDefId(process.getId());
                        entity.setDefKey(process.getId());
                    }
                }
                if (process.getStartUEL() != null) {
                    // flowDefination.setStartUel(process.getStartUEL().toString());
                    entity.setStartUel(process.getStartUEL().toString());
                }
                entity.setFlowDefinationStatus(FlowDefinationStatus.INIT);
                entity.setFlowDefination(flowDefination);
                entity.setDefJson(defJson);
//                entity.setDefBpmn(defBpm);
                entity.setDefXml(defBpm);
                entity.setName(process.getName());
                entity.setPriority(definition.getPriority());
                entity.setSubProcess(canAsSubProcess);
                entity.setSolidifyFlow(canAssolidifyFlow);
                entity.setStartCheckServiceUrlId(process.getBeforeStartServiceId());
                entity.setStartCheckServiceUrlName(process.getBeforeStartServiceName());
                entity.setStartCheckServiceAync(process.getBeforeStartServiceAync());
                entity.setAfterStartServiceId(process.getAfterStartServiceId());
                entity.setAfterStartServiceName(process.getAfterStartServiceName());
                entity.setAfterStartServiceAync(process.getAfterStartServiceAync());
                entity.setEndBeforeCallServiceUrlId(process.getBeforeEndServiceId());
                entity.setEndBeforeCallServiceUrlName(process.getBeforeEndServiceName());
                entity.setEndBeforeCallServiceAync(process.getBeforeEndServiceAync());
                entity.setEndCallServiceUrlId(process.getAfterEndServiceId());
                entity.setEndCallServiceUrlName(process.getAfterEndServiceName());
                entity.setEndCallServiceAync(process.getAfterEndServiceAync());

                flowDefVersionDao.save(entity);
                flowDefination.setLastVersionId(entity.getId());
                flowDefination.setPriority(definition.getPriority());
                flowDefination.setName(process.getName());
                flowDefination.setSubProcess(canAsSubProcess);
                flowDefination.setSolidifyFlow(canAssolidifyFlow);
                flowDefinationDao.save(flowDefination);
                LogUtil.info("Saved FlowDefVersion id is {}", entity.getId());
            } else {//版本为空
                entity = new FlowDefVersion();
                entity.setFlowDefinationStatus(FlowDefinationStatus.INIT);
                entity.setActDefId(process.getId());
                entity.setName(process.getName());
                entity.setDefKey(process.getId());
//                entity.setVersionCode(1);
                entity.setFlowDefination(flowDefination);
                entity.setDefJson(defJson);
//                entity.setDefBpmn(defBpm);
                entity.setDefXml(defBpm);
                if (process.getStartUEL() != null) {
                    // flowDefination.setStartUel(process.getStartUEL().toString());
                    entity.setStartUel(process.getStartUEL().toString());
                }
                entity.setPriority(definition.getPriority());
                entity.setSubProcess(canAsSubProcess);
                entity.setSolidifyFlow(canAssolidifyFlow);
                entity.setStartCheckServiceUrlId(process.getBeforeStartServiceId());
                entity.setStartCheckServiceUrlName(process.getBeforeStartServiceName());
                entity.setStartCheckServiceAync(process.getBeforeStartServiceAync());
                entity.setAfterStartServiceId(process.getAfterStartServiceId());
                entity.setAfterStartServiceName(process.getAfterStartServiceName());
                entity.setAfterStartServiceAync(process.getAfterStartServiceAync());
                entity.setEndBeforeCallServiceUrlId(process.getBeforeEndServiceId());
                entity.setEndBeforeCallServiceUrlName(process.getBeforeEndServiceName());
                entity.setEndBeforeCallServiceAync(process.getBeforeEndServiceAync());
                entity.setEndCallServiceUrlId(process.getAfterEndServiceId());
                entity.setEndCallServiceUrlName(process.getAfterEndServiceName());
                entity.setEndCallServiceAync(process.getAfterEndServiceAync());
                flowDefVersionDao.save(entity);
                LogUtil.info("Saved FlowDefVersion id is {}", entity.getId());
                flowDefination.setName(process.getName());
                flowDefination.setLastVersionId(entity.getId());
                flowDefination.setPriority(definition.getPriority());
                flowDefination.setSubProcess(canAsSubProcess);
                flowDefination.setSolidifyFlow(canAssolidifyFlow);
                flowDefinationDao.save(flowDefination);
                LogUtil.info("Saved FlowDefination id is {}", flowDefination.getId());
            }

        }
        OperateResultWithData<FlowDefVersion> operateResult;
        // 流程版本保存成功！
        operateResult = OperateResultWithData.operationSuccess("10056");
        operateResult.setData(entity);
        clearFlowDefVersion();
        return operateResult;
    }

    private void clearFlowDefVersion() {
        String pattern = "FLowGetLastFlowDefVersion_*";
        if (redisTemplate != null) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
//        appModuleDao.clearLevel2Cache();//手动清除二级缓存
    }

    /**
     * 数据删除操作
     * 清除有关联的流程版本及对应的流程引擎数据
     * @param id 待操作数据
     */
    @Override
    public OperateResult delete(String id) {
        // 流程版本删除成功！
        OperateResult result = OperateResult.operationSuccess("10060");
        FlowDefVersion entity = flowDefVersionDao.findOne(id);
        List<FlowInstance> flowInstanceList = flowInstanceDao.findByFlowDefVersionId(entity.getId());
        if (flowInstanceList != null && !flowInstanceList.isEmpty()) {
            result = OperateResult.operationFailure("10024");
            return result;
        }
        FlowDefination flowDefination = entity.getFlowDefination();
        String actDeployId = entity.getActDeployId();
        if (StringUtils.isNotEmpty(actDeployId)) {
            this.deleteActivtiProcessDefinition(actDeployId, false);
        }
        flowDefVersionDao.delete(entity);
        List<FlowDefVersion> flowDefVersionList = flowDefVersionDao.findByFlowDefinationId(flowDefination.getId());
        if (flowDefVersionList == null || flowDefVersionList.isEmpty()) {//找不到对应的版本，删除流程定义
            flowDefinationDao.delete(flowDefination);
        }
        clearFlowDefVersion(id);
        return result;
    }

    /**
     * 通过ID批量删除
     * @param ids
     */
    @Override
    public void delete(Collection<String> ids) {
        for (String id : ids) {
            this.delete(id);
        }
    }

    private void clearFlowDefVersion(String defVersionId) {
        String key = "FLowGetLastFlowDefVersion_" + defVersionId;
        if (redisTemplate != null) {
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
//        appModuleDao.clearLevel2Cache();//手动清除二级缓存
    }

    /**
     * 使用部署ID，删除流程引擎数据定义
     * @param deploymentId 发布ID
     * @param force        是否强制删除（能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息）
     */
    private void deleteActivtiProcessDefinition(String deploymentId, Boolean force) {
        /*
         * 不带级联的删除
         * 只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
        if (force) {
            /*
             * 能级联的删除
             * 能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
             */
            processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                    .deleteDeployment(deploymentId, true);
        } else {
            processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                    .deleteDeployment(deploymentId);
        }
    }

}
