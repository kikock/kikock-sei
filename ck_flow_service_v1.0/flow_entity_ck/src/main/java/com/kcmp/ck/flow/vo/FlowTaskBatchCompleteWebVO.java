package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 执行任务完成时的传输对象-web端
 * @email kikock@qq.com
 **/
public class FlowTaskBatchCompleteWebVO implements Serializable{

    /**
     * 任务id
     */
    private List<String> taskIdList;

    private List<FlowTaskCompleteWebVO> flowTaskCompleteList;

    private Boolean  solidifyFlow;

    public List<String> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(List<String> taskIdList) {
        this.taskIdList = taskIdList;
    }

    public List<FlowTaskCompleteWebVO> getFlowTaskCompleteList() {
        return flowTaskCompleteList;
    }

    public void setFlowTaskCompleteList(List<FlowTaskCompleteWebVO> flowTaskCompleteList) {
        this.flowTaskCompleteList = flowTaskCompleteList;
    }

    public Boolean getSolidifyFlow() {
        return solidifyFlow;
    }

    public void setSolidifyFlow(Boolean solidifyFlow) {
        this.solidifyFlow = solidifyFlow;
    }
}
