package com.kcmp.ck.flow.vo;

import com.kcmp.ck.flow.entity.FlowHistory;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowTask;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 流程跟踪vo对象
 * @email kikock@qq.com
 **/
public class ProcessTrackVO implements Serializable {
   private FlowInstance flowInstance;
   private List<FlowHistory>  flowHistoryList;
   private List<FlowTask> flowTaskList;

    public FlowInstance getFlowInstance() {
        return flowInstance;
    }

    public void setFlowInstance(FlowInstance flowInstance) {
        this.flowInstance = flowInstance;
    }

    public List<FlowHistory> getFlowHistoryList() {
        return flowHistoryList;
    }

    public void setFlowHistoryList(List<FlowHistory> flowHistoryList) {
        this.flowHistoryList = flowHistoryList;
    }

    public List<FlowTask> getFlowTaskList() {
        return flowTaskList;
    }

    public void setFlowTaskList(List<FlowTask> flowTaskList) {
        this.flowTaskList = flowTaskList;
    }
}
