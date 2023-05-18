package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kikock
 * 流程头vo
 * @email kikock@qq.com
 **/
public class ApprovalHeaderVO implements Serializable{
    /**
     * 业务单据ID
     */
    private String businessId;

    /**
     * 业务单号
     */
    private String businessCode;
    /**
     * 创单人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上一步执行人
     */
    private String prUser;
    /**
     * 上一步审批意见
     */
    private String prOpinion;

    /**
     * 上一步执行时间
     */
    private Date preCreateTime;

    /**
     * 工作流根路径
     */
    private String flowBaseUrl;

    /**
     * 当前节点配置的默认意见
     */
    private String currentNodeDefaultOpinion;

    /**
     * 是否为固化流程
     */
    private Boolean solidifyFlow;

    /**
     * 工作说明（包含流程启动时的附加说明）
     */
    private String workAndAdditionRemark;


    //下面是新增参数、用着判断按钮的参数，联合判断逻辑位于TodoTaskView.js的showData()中

    /**
     * 是被转办的状态0，委托状态，1发起委托的任务，2被委托的任务,非委托状态为null,委托完成为3
     */
    private Integer trustState;

    /**
     * 是否允许驳回
     */
    private Boolean canReject;

    /**
     * 任务定义JSON
     */
    private String taskJsonDef;

    /**
     * 流程任务引擎实际的任务签收时间
     */
    private Date actClaimTime;

    /**
     * 是否允许流程中止（退出流程）
     */
    private Boolean canSuspension;

    /**
     * 执行人ID
     */
    private String executorId;

    /**
     * 流程实例创建人
     */
    private String flowInstanceCreatorId;


    public Integer getTrustState() {
        return trustState;
    }

    public void setTrustState(Integer trustState) {
        this.trustState = trustState;
    }

    public Boolean getCanReject() {
        return canReject;
    }

    public void setCanReject(Boolean canReject) {
        this.canReject = canReject;
    }

    public String getTaskJsonDef() {
        return taskJsonDef;
    }

    public void setTaskJsonDef(String taskJsonDef) {
        this.taskJsonDef = taskJsonDef;
    }

    public Date getActClaimTime() {
        return actClaimTime;
    }

    public void setActClaimTime(Date actClaimTime) {
        this.actClaimTime = actClaimTime;
    }

    public Boolean getCanSuspension() {
        return canSuspension;
    }

    public void setCanSuspension(Boolean canSuspension) {
        this.canSuspension = canSuspension;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getFlowInstanceCreatorId() {
        return flowInstanceCreatorId;
    }

    public void setFlowInstanceCreatorId(String flowInstanceCreatorId) {
        this.flowInstanceCreatorId = flowInstanceCreatorId;
    }

    public String getWorkAndAdditionRemark() {
        return workAndAdditionRemark;
    }

    public void setWorkAndAdditionRemark(String workAndAdditionRemark) {
        this.workAndAdditionRemark = workAndAdditionRemark;
    }

    public Boolean getSolidifyFlow() {
        return solidifyFlow;
    }

    public void setSolidifyFlow(Boolean solidifyFlow) {
        this.solidifyFlow = solidifyFlow;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getPrUser() {
        return prUser;
    }

    public void setPrUser(String prUser) {
        this.prUser = prUser;
    }

    public String getPrOpinion() {
        return prOpinion;
    }

    public void setPrOpinion(String prOpinion) {
        this.prOpinion = prOpinion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPreCreateTime() {
        return preCreateTime;
    }

    public void setPreCreateTime(Date preCreateTime) {
        this.preCreateTime = preCreateTime;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getFlowBaseUrl() {
        return flowBaseUrl;
    }

    public void setFlowBaseUrl(String flowBaseUrl) {
        this.flowBaseUrl = flowBaseUrl;
    }

    public String getCurrentNodeDefaultOpinion() {
        return currentNodeDefaultOpinion;
    }

    public void setCurrentNodeDefaultOpinion(String currentNodeDefaultOpinion) {
        this.currentNodeDefaultOpinion = currentNodeDefaultOpinion;
    }
}
