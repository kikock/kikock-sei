package com.kcmp.ck.flow.listener;

import com.kcmp.ck.flow.common.util.Constants;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by kikock
 * 通用会签任务监听器，当会签任务完成时统计投票数量
 * counterSignAgree代表同意、counterSignOpposition代表不同意、counterSignWaiver代表弃权
 * @author kikock
 * @email kikock@qq.com
 **/
public class CommonCounterSignCompleteListener implements TaskListener{

    @Autowired
    private RuntimeService runtimeService;

    public CommonCounterSignCompleteListener(){
	}
    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String,VariableInstance> processVariables =  runtimeService.getVariableInstances(delegateTask.getProcessInstanceId());
        Integer counterSignAgree = 0;//同意票数
        if(processVariables.get(Constants.COUNTER_SIGN_AGREE+delegateTask.getTaskDefinitionKey())!=null) {
            counterSignAgree = (Integer) processVariables.get(Constants.COUNTER_SIGN_AGREE+delegateTask.getTaskDefinitionKey()).getValue();//同意票数
        }
        Integer counterSignOpposition = 0;//反对
        if( processVariables.get(Constants.COUNTER_SIGN_OPPOSITION+delegateTask.getTaskDefinitionKey())!=null) {
             counterSignOpposition = (Integer) processVariables.get(Constants.COUNTER_SIGN_OPPOSITION+delegateTask.getTaskDefinitionKey()).getValue();
        }

        Integer counterSignWaiver = 0;//弃权
        if( processVariables.get(Constants.COUNTER_SIGN_WAIVER+delegateTask.getTaskDefinitionKey())!=null) {
            counterSignWaiver = (Integer) processVariables.get(Constants.COUNTER_SIGN_WAIVER+delegateTask.getTaskDefinitionKey()).getValue();
        }

        String approved = delegateTask.getVariable(Constants.APPROVED)+"";

        if("true".equalsIgnoreCase(approved)){
            counterSignAgree++;
        }else if("false".equalsIgnoreCase(approved)){
            counterSignOpposition++;
        }else {
            counterSignWaiver++;
        }
        runtimeService.setVariable(delegateTask.getProcessInstanceId(),Constants.COUNTER_SIGN_AGREE+delegateTask.getTaskDefinitionKey(), counterSignAgree);
        runtimeService.setVariable(delegateTask.getProcessInstanceId(),Constants.COUNTER_SIGN_OPPOSITION+delegateTask.getTaskDefinitionKey(), counterSignOpposition);
        runtimeService.setVariable(delegateTask.getProcessInstanceId(),Constants.COUNTER_SIGN_WAIVER+delegateTask.getTaskDefinitionKey(), counterSignWaiver);
    }
}
