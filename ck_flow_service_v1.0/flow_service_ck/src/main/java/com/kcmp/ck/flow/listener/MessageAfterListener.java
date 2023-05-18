package com.kcmp.ck.flow.listener;

import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.dao.FlowHistoryDao;
import com.kcmp.ck.flow.dao.FlowTaskDao;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.log.LogUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by kikock
 * 发送消息后监听事件
 * @author kikock
 * @email kikock@qq.com
 **/
public class MessageAfterListener implements Serializable, org.activiti.engine.delegate.ExecutionListener{

	public MessageAfterListener(){
	}
    private static final long serialVersionUID = 1L;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowTaskDao flowTaskDao;

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FlowHistoryDao flowHistoryDao;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FlowCommonUtil flowCommonUtil;


    @Override
    public void notify(DelegateExecution execution) throws Exception{
        try {
            String deleteReason = ((ExecutionEntity) execution).getDeleteReason();
            if(StringUtils.isNotEmpty(deleteReason)){
                return;
            }
            String eventType = "after";
            String contentTemplateCode = "EMAIL_TEMPLATE_AFTER_DOWORK";
            MessageSendThread messageSendThread = new MessageSendThread(eventType,execution,contentTemplateCode);
            messageSendThread.setFlowDefVersionDao(this.flowDefVersionDao);
            messageSendThread.setFlowTaskDao(this.flowTaskDao);
            messageSendThread.setHistoryService(this.historyService);
            messageSendThread.setFlowHistoryDao(this.flowHistoryDao);
            messageSendThread.setRuntimeService(runtimeService);
            messageSendThread.setTaskService(taskService);
            messageSendThread.setFlowCommonUtil(flowCommonUtil);
            messageSendThread.run();
        }catch (Exception e){
            LogUtil.error(e.getMessage(),e);
        }
    }
}
