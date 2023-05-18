package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.flow.api.IWorkPageUrlService;
import com.kcmp.ck.flow.dao.BusinessWorkPageUrlDao;
import com.kcmp.ck.flow.dao.WorkPageUrlDao;
import com.kcmp.ck.flow.entity.BusinessWorkPageUrl;
import com.kcmp.ck.flow.entity.WorkPageUrl;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kikock
 * 工作界面配置管理服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class WorkPageUrlService extends BaseEntityService<WorkPageUrl> implements IWorkPageUrlService {

    @Autowired
    private WorkPageUrlDao workPageUrlDao;

    @Autowired
    private BusinessWorkPageUrlDao businessWorkPageUrlDao;

    @Override
    protected BaseEntityDao<WorkPageUrl> getDao(){
        return this.workPageUrlDao;
    }

    /**
     * 根据应用模块id查询业务实体t
     * @param appModuleId 应用模块id
     * @return 实体清单
     */
    @Override
    public List<WorkPageUrl> findByAppModuleId(String appModuleId) {
        return workPageUrlDao.findByAppModuleId(appModuleId);
    }

    /**
     * 查看对应业务实体已选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @Override
    public List<WorkPageUrl> findSelectEdByAppModuleId(String appModuleId,String businessModelId) {
        return workPageUrlDao.findSelectEd(appModuleId, businessModelId);
    }

    /**
     * 查看对应业务实体未选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 选中的工作界面
     */
    @Override
    public List<WorkPageUrl> findNotSelectEdByAppModuleId(String appModuleId,String businessModelId) {
        return workPageUrlDao.findNotSelectEd(appModuleId, businessModelId);
    }

    /**
     * 查看对应业务实体未选中的工作界面
     * @param appModuleId  业务模块Id
     * @param businessModelId  业务实体ID
     * @return 未选中的工作界面
     */
    @Override
    public ResponseData listAllNotSelectEdByAppModuleId(String appModuleId, String businessModelId){
        ResponseData responseData = new ResponseData();
        if(StringUtils.isNotEmpty(appModuleId) && StringUtils.isNotEmpty(businessModelId)){
            List<WorkPageUrl> list = this.findNotSelectEdByAppModuleId(appModuleId,businessModelId);
            responseData.setData(list);
        }else{
            responseData.setSuccess(false);
            responseData.setMessage("参数不能为空！");
        }
        return responseData;
    }

    /**
     * 通过流程类型id查找拥有的工作界面
     * @param flowTypeId 流程类型id
     * @return 工作界面list
     */
    @Override
    public List<WorkPageUrl> findByFlowTypeId(String flowTypeId){
        return workPageUrlDao.findByFlowTypeId(flowTypeId);
    }

    /**
     * 查看对应业务实体已选中的工作界面
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @Override
    public List<WorkPageUrl> findSelectEdByBusinessModelId(String businessModelId){
     return    workPageUrlDao.findSelectEdByBusinessModelId(businessModelId);
    }

    /**
     * 查看对应业务实体已选中的工作界面
     * @param businessModelId  业务实体ID
     * @return 已选中的工作界面
     */
    @Override
    public  ResponseData listAllSelectEdByAppModuleId(String businessModelId){
        ResponseData responseData = new ResponseData();
        if(StringUtils.isNotEmpty(businessModelId)){
            List<WorkPageUrl> list =   this.findSelectEdByBusinessModelId(businessModelId);
            responseData.setData(list);
        }else{
            responseData.setSuccess(false);
            responseData.setMessage("参数不能为空！");
        }
        return responseData;
    }

    /**
     * 数据删除操作
     * (检查工作界面是不是已经分配，已经分配的不能进行删除，给出提供)
     * @param id 待操作数据
     */
    @Override
    public OperateResult delete(String id) {
      List<BusinessWorkPageUrl> list =  businessWorkPageUrlDao.findListByProperty("workPageUrlId",id);
      if(list!=null&&list.size()>0){
          return OperateResult.operationFailure("已分配的界面，不能进行删除！");
      }
        return super.delete(id);
    }
}
