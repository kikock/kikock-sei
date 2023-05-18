package com.kcmp.ck.flow.listener;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.FlowInstanceDao;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.util.ExpressionUtil;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.util.FlowListenerTool;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.log.LogUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * Created by kikock
 * 启动完成监听器
 * @author kikock
 * @email kikock@qq.com
 **/
public class EndEventCompleteListener implements ExecutionListener {

	public EndEventCompleteListener(){
	}
    private static final long serialVersionUID = 1L;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private FlowListenerTool flowListenerTool;

    @Override
    @Transactional( propagation= Propagation.REQUIRED)
    public void notify(DelegateExecution delegateTask) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException{
        ExecutionEntity taskEntity = (ExecutionEntity) delegateTask;
        Map<String,Object> variables = delegateTask.getVariables();
        ProcessInstance processInstance  = taskEntity.getProcessInstance();
        FlowInstance flowInstance = flowInstanceDao.findByActInstanceId(processInstance.getId());
        String deleteReason = ((ExecutionEntity) delegateTask).getDeleteReason();
        int endSign = 0;
        String  endCode = taskEntity.getActivityId();
        if(endCode.indexOf("TerminateEndEvent")!=-1){ //TerminateEndEvent：强制终止   EndEvent:终止
            endSign = 3;
        }
        if(StringUtils.isNotEmpty(deleteReason)){
            if("10035".equals(deleteReason)){//"被管理员强制终止流程
                endSign = 2;
            }else if( "10036".equals(deleteReason)) {//被发起人终止流程
                endSign = 1;
            }
        }
        if(flowInstance==null){
             throw new FlowException(ContextUtil.getMessage("10002"));//流程实例不存在
        }else {
            if (processInstance.isEnded()) {//针对启动时只有服务任务这种情况（即启动就结束）
                BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                FlowOperateResult callBeforeEndResult = flowListenerTool.callBeforeEnd(processInstance.getBusinessKey(), flowInstance.getFlowDefVersion(),endSign,variables);
                if(callBeforeEndResult!=null&&!callBeforeEndResult.isSuccess()){
                    throw new FlowException(callBeforeEndResult.getMessage());
                }
                flowInstance.setEnded(true);
                flowInstance.setEndDate(new Date());
                flowInstanceDao.save(flowInstance);
                //回写状态
                FlowInstance flowInstanceP = flowInstance.getParent();
                Boolean result =  ExpressionUtil.resetState(businessModel,flowInstance.getBusinessId(), FlowStatus.COMPLETED);
                if(!result){
                    throw new FlowException(ContextUtil.getMessage("10049"));//流程结束-调用重置表单状态失败!
                }
                if(flowInstanceP!=null){
                    ExecutionEntity parent = taskEntity.getSuperExecution();
                    if(parent != null){
                        ProcessInstance   parentProcessInstance = parent.getProcessInstance();
                        Map<String,Object> variablesParent = runtimeService.getVariables(parent.getId());
                        String superExecutionId = processInstance.getSuperExecutionId();
                        variablesParent.putAll(variables);
                        variablesParent.put(processInstance.getId() + Constants.SUPER_EXECUTION_ID,superExecutionId);
                        runtimeService.setVariables(parentProcessInstance.getId(),variablesParent);
                    }
                }

                String businessId = delegateTask.getProcessBusinessKey();
                FlowOperateResult callEndResult=null;
                try{
                    callEndResult =  flowListenerTool.callEndService(businessId, flowInstance.getFlowDefVersion(),endSign,variables);
                }catch (Exception e){
                    new Thread() {
                        @Override
                        public void run() {
                            BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                            Boolean result = false;
                            int index = 5;
                            while (!result && index > 0) {
                                try {
                                    Thread.sleep(1000 * (6 - index));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    result = ExpressionUtil.resetState(businessModel, flowInstance.getBusinessId(), FlowStatus.INPROCESS);
                                } catch (Exception e) {
                                    LogUtil.error(e.getMessage(), e);
                                }
                                index--;
                            }
                        }
                    }.start();
                    throw e;
                }

                if(callEndResult!=null&&!callEndResult.isSuccess()){
                    new Thread() {
                        @Override
                        public void run() {
                            BusinessModel businessModel = flowInstance.getFlowDefVersion().getFlowDefination().getFlowType().getBusinessModel();
                            Boolean result = false;
                            int index = 5;
                            while (!result && index > 0) {
                                try {
                                    Thread.sleep(1000 * (6 - index));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    result = ExpressionUtil.resetState(businessModel, flowInstance.getBusinessId(), FlowStatus.INPROCESS);
                                } catch (Exception e) {
                                    LogUtil.error(e.getMessage(), e);
                                }
                                index--;
                            }
                        }
                    }.start();
                    throw new FlowException(callEndResult.getMessage());
                }
            }
        }
    }

    /**
     * 模拟异步,上传调用日志
     * @param message
     */
    static void asyncUploadLog(String message){
        //模拟异步,上传调用日志
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.bizLog(message);
            }
        }).start();
    }
}
