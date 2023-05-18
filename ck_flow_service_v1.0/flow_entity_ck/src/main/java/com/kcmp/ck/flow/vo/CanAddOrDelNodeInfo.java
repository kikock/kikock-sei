package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 能够执行加减签-会签节点信息列表，先实现会签发起人权限级别
 * @author kikock
 * @email kikock@qq.com
 **/
public class CanAddOrDelNodeInfo implements Serializable {

    /**
     * 流程实例Id
     */
    private String actInstanceId;
    /**
     * 流程节点定义key
     */
    private String nodeKey;
    /**
     * 流程节点名称==taskName
     */
    private String nodeName;
    /**
     * 业务单据id
     */
    private String businessId;
    /**
     * 业务单据编号
     */
    private String businessCode;
    /**
     * 业务单据名称
     */
    private String businessName;
    /**
     * 业务摘要(工作说明)
     */
    private String businessModelRemark;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 流程定义key
     */
    private String flowDefKey;

    public CanAddOrDelNodeInfo(){}

    public CanAddOrDelNodeInfo(String actInstanceId,String nodeKey,String nodeName,String businessId,String businessCode
    ,String businessName,String businessModelRemark,String flowName,String flowDefKey){
        this.actInstanceId = actInstanceId;
        this.nodeKey = nodeKey;
        this.nodeName = nodeName;
        this.businessId = businessId;
        this.businessCode = businessCode;
        this.businessName = businessName;
        this.businessModelRemark = businessModelRemark;
        this.flowName = flowName;
        this.flowDefKey = flowDefKey;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getActInstanceId() {
        return actInstanceId;
    }

    public void setActInstanceId(String actInstanceId) {
        this.actInstanceId = actInstanceId;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessModelRemark() {
        return businessModelRemark;
    }

    public void setBusinessModelRemark(String businessModelRemark) {
        this.businessModelRemark = businessModelRemark;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowDefKey() {
        return flowDefKey;
    }

    public void setFlowDefKey(String flowDefKey) {
        this.flowDefKey = flowDefKey;
    }

}
