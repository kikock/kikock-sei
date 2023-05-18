package com.kcmp.ck.flow.listener;

import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.util.FlowListenerTool;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import net.sf.json.JSONObject;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kikock
 * 通用用户任务完成监听器
 * @author kikock
 * @email kikock@qq.com
 **/
public class CommonUserTaskCompleteListener implements ExecutionListener {

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private FlowListenerTool flowListenerTool;

	public CommonUserTaskCompleteListener(){
	}
    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution delegateTask) {
        try {
            String deleteReason = ((ExecutionEntity) delegateTask).getDeleteReason();
            //流程结束不触发事件
            if(StringUtils.isNotEmpty(deleteReason)){
                return;
            }
            String actTaskDefKey = delegateTask.getCurrentActivityId();
            String actProcessDefinitionId = delegateTask.getProcessDefinitionId();
            String businessId =delegateTask.getProcessBusinessKey();
            FlowDefVersion flowDefVersion = flowDefVersionDao.findByActDefId(actProcessDefinitionId);
            String flowDefJson = flowDefVersion.getDefJson();
            JSONObject defObj = JSONObject.fromObject(flowDefJson);
            Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
            net.sf.json.JSONObject currentNode = definition.getProcess().getNodes().getJSONObject(actTaskDefKey);
            net.sf.json.JSONObject event =null;
            if(currentNode.has(Constants.NODE_CONFIG)&&currentNode.getJSONObject(Constants.NODE_CONFIG).has("event")){
                event = currentNode.getJSONObject(Constants.NODE_CONFIG).getJSONObject(Constants.EVENT);
            }
            if (event != null) {
                String afterExcuteServiceIdTemp = null;
                if(event.has(Constants.AFTER_EXCUTE_SERVICE_ID)){
                    afterExcuteServiceIdTemp = event.getString(Constants.AFTER_EXCUTE_SERVICE_ID);
                }
                String afterExcuteServiceId = afterExcuteServiceIdTemp;
                boolean async = false;//默认为同步
                if(event.has(Constants.AFTER_ASYNC)){
                    async =  event.getBoolean(Constants.AFTER_ASYNC);
                }
                if (!StringUtils.isEmpty(afterExcuteServiceId)) {
                    flowListenerTool.taskEventServiceCall(delegateTask, async, afterExcuteServiceId ,businessId);
                }
            }
        }catch (Exception e){
            if(e.getClass()!= FlowException.class){
                LogUtil.error(e.getMessage(),e);
            }
            throw new FlowException(e.getMessage());
        }
    }
}
