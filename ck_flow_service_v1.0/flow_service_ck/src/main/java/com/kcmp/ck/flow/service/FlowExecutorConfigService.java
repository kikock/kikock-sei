package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.ck.flow.api.IFlowExecutorConfigService;
import com.kcmp.ck.flow.dao.FlowExecutorConfigDao;
import com.kcmp.ck.flow.entity.FlowExecutorConfig;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kikock
 * 自定义执行人配置的服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowExecutorConfigService extends BaseEntityService<FlowExecutorConfig> implements IFlowExecutorConfigService {

    @Autowired
    private FlowExecutorConfigDao flowExecutorConfigDao;

    @Override
    protected BaseEntityDao<FlowExecutorConfig> getDao() {
        return this.flowExecutorConfigDao;
    }

    /**
     * 保存一个实体(并验证code的唯一性)
     * @param flowExecutorConfig 实体
     * @return 保存后的实体
     */
    @Override
    public OperateResultWithData<FlowExecutorConfig> saveValidateCode(FlowExecutorConfig flowExecutorConfig){
        FlowExecutorConfig  bean =  flowExecutorConfigDao.findByProperty("code",flowExecutorConfig.getCode());
        if(bean!=null&&!bean.getId().equals(flowExecutorConfig.getId())){
            return  OperateResultWithData.operationFailure("操作失败：代码已存在！");
        }
        flowExecutorConfigDao.save(flowExecutorConfig);
        return OperateResultWithData.operationSuccessWithData(flowExecutorConfig);
    }

    /**
     * 通过业务实体ID获取自定义执行人
     * @param businessModelId
     * @return
     */
    @Override
    public ResponseData listCombo(String businessModelId) {
        if(StringUtils.isEmpty(businessModelId)){
            return  ResponseData.operationFailure("参数不能为空!");
        }
        List<FlowExecutorConfig>  list = flowExecutorConfigDao.findListByProperty("businessModel.id",businessModelId);
        return ResponseData.operationSuccessWithData(list);
    }
}
