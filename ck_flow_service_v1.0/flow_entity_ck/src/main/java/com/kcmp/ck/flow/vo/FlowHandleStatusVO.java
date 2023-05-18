package com.kcmp.ck.flow.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kikock
 * 流程处理状态vo对象
 * @email kikock@qq.com
 **/
public class FlowHandleStatusVO implements Serializable {

    /**
     * 流程当前处理状态
     */
    private String flowCurHandleStatusTaskName;

    /**
     * 流程等待处理人
     */
    private String flowWaitingPerson;

    /**
     * 流程任务到达时间
     */
    private Date flowTaskArriveTime;

    public String getFlowCurHandleStatusTaskName() {
        return flowCurHandleStatusTaskName;
    }

    public void setFlowCurHandleStatusTaskName(String flowCurHandleStatusTaskName) {
        this.flowCurHandleStatusTaskName = flowCurHandleStatusTaskName;
    }

    public String getFlowWaitingPerson() {
        return flowWaitingPerson;
    }

    public void setFlowWaitingPerson(String flowWaitingPerson) {
        this.flowWaitingPerson = flowWaitingPerson;
    }

    public Date getFlowTaskArriveTime() {
        return flowTaskArriveTime;
    }

    public void setFlowTaskArriveTime(Date flowTaskArriveTime) {
        this.flowTaskArriveTime = flowTaskArriveTime;
    }

    public FlowHandleStatusVO() {
    }

    public FlowHandleStatusVO(String flowCurHandleStatusTaskName, String flowWaitingPerson, Date flowTaskArriveTime) {
        this.flowCurHandleStatusTaskName = flowCurHandleStatusTaskName;
        this.flowWaitingPerson = flowWaitingPerson;
        this.flowTaskArriveTime = flowTaskArriveTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("flowCurHandleStatusTaskName", flowCurHandleStatusTaskName)
                .append("flowWaitingPerson", flowWaitingPerson)
                .append("flowTaskArriveTime", flowTaskArriveTime)
                .toString();
    }
}
