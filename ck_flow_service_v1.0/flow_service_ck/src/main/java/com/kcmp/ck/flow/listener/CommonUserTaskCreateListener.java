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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kikock
 * 通用用户任务创建监听器
 * @author kikock
 * @email kikock@qq.com
 **/
public class CommonUserTaskCreateListener implements ExecutionListener {

	public CommonUserTaskCreateListener(){
	}
    private static final long serialVersionUID = 1L;

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private FlowListenerTool flowListenerTool;

    @Override
    @Transactional( propagation= Propagation.REQUIRED)
    public void notify(DelegateExecution delegateTask) {
        try {
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
                String beforeExcuteServiceId = (String) event.get(Constants.BEFORE_EXCUTE_SERVICE_ID);
                boolean async = true;//默认为异步
                if(event.has(Constants.BEFORE_ASYNC)){
                    Boolean beforeAsync = event.getBoolean(Constants.BEFORE_ASYNC);
                    if(beforeAsync != true){
                        async=false;
                    }
                }
                if (!StringUtils.isEmpty(beforeExcuteServiceId)) {
                    flowListenerTool.taskEventServiceCall(delegateTask, async, beforeExcuteServiceId ,businessId);
                }
            }
        }catch(Exception e){
            if(e.getClass()!= FlowException.class){
                LogUtil.error(e.getMessage(),e);
            }
            throw new FlowException(e.getMessage());
        }
    }
}
