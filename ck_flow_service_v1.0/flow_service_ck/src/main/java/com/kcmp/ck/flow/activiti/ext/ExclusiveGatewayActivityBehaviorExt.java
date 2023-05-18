package com.kcmp.ck.flow.activiti.ext;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.util.ConditionUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 扩展网关判断
 * @author kikock
 * @email kikock@qq.com
 **/
public class ExclusiveGatewayActivityBehaviorExt extends ExclusiveGatewayActivityBehavior {

    protected static Logger log =  LoggerFactory.getLogger(ExclusiveGatewayActivityBehaviorExt.class);


    private RepositoryService repositoryService;

    @Override
    protected void leave(ActivityExecution execution) {
        ApplicationContext applicationContext = ContextUtil.getApplicationContext();
        repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
        List<PvmTransition> outSequenceList = execution.getActivity().getOutgoingTransitions();
        if (outSequenceList != null && outSequenceList.size() > 0) {
            for (PvmTransition pv : outSequenceList) {
                String conditionText = (String) pv.getProperty(Constants.CONDITION_TEXT);
                if(conditionText != null && conditionText.startsWith("#{")){
                    String conditionFinal = conditionText.substring(conditionText.indexOf("#{")+2, conditionText.lastIndexOf("}"));
                    Map<String, Object> map = execution.getVariables();
                    if(ConditionUtil.groovyTest(conditionFinal, map)){
                        execution.take(pv);
                        return;
                    }
                }
            }
        }
        // 执行父类的写法，以使其还是可以支持旧式的在跳出线上写条件的做法
        super.leave(execution);
    }

}
