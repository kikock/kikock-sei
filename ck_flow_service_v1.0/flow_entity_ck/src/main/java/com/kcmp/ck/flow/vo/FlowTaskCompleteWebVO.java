package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 执行任务完成时的传输对象-web端
 * @email kikock@qq.com
 **/
public class FlowTaskCompleteWebVO implements Serializable{

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 自定义任务类型,common(普通),单签(singleSign),会签(countersign)
     */
    private String flowTaskType;

    /**
     * 用户ID,以逗号分隔
     */
    private String userIds;


    /**'
     * 流程节点用户变量名称
     */
    private String userVarName;


    /**
     * 调用子流程中的节点路径
     */
   private String callActivityPath;


   private Boolean instancyStatus;


   private Boolean solidifyFlow;


    public Boolean getSolidifyFlow() {
        return solidifyFlow;
    }

    public void setSolidifyFlow(Boolean solidifyFlow) {
        this.solidifyFlow = solidifyFlow;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getFlowTaskType() {
        return flowTaskType;
    }

    public void setFlowTaskType(String flowTaskType) {
        this.flowTaskType = flowTaskType;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUserVarName() {
        return userVarName;
    }

    public void setUserVarName(String userVarName) {
        this.userVarName = userVarName;
    }

    public String getCallActivityPath() {
        return callActivityPath;
    }

    public void setCallActivityPath(String callActivityPath) {
        this.callActivityPath = callActivityPath;
    }

    public Boolean getInstancyStatus() {
        return instancyStatus;
    }

    public void setInstancyStatus(Boolean instancyStatus) {
        this.instancyStatus = instancyStatus;
    }
}
