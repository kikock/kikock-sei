package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 任务启动时的引擎返回的对象
 * @email kikock@qq.com
 **/
public class FlowStartResultVO implements Serializable{


    private List<StartFlowTypeVO> flowTypeList;//流程类型选择（一个流程实体存在多个流程类型的情况下）
    private List<NodeInfo> nodeInfoList;//启动时节点信息

    private Boolean checkStartResult=true;

    private Boolean allowChooseInstancy;//是否允许选择任务紧急状态

    private Boolean solidifyFlow;//是否为固化流程

    private String flowDefinationId;//固化流程需要这个参数调用流程图

    /**
     * 额外参数
     */
    private Map<String, Object> variables;


    public Boolean getSolidifyFlow() {
        return solidifyFlow;
    }

    public void setSolidifyFlow(Boolean solidifyFlow) {
        this.solidifyFlow = solidifyFlow;
    }

    public String getFlowDefinationId() {
        return flowDefinationId;
    }

    public void setFlowDefinationId(String flowDefinationId) {
        this.flowDefinationId = flowDefinationId;
    }

    public List<StartFlowTypeVO> getFlowTypeList() {
        return flowTypeList;
    }

    public void setFlowTypeList(List<StartFlowTypeVO> flowTypeList) {
        this.flowTypeList = flowTypeList;
    }

    public List<NodeInfo> getNodeInfoList() {
        return nodeInfoList;
    }

    public void setNodeInfoList(List<NodeInfo> nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Boolean getCheckStartResult() {
        return checkStartResult;
    }

    public void setCheckStartResult(Boolean checkStartResult) {
        this.checkStartResult = checkStartResult;
    }

    public Boolean getAllowChooseInstancy() {
        return allowChooseInstancy;
    }

    public void setAllowChooseInstancy(Boolean allowChooseInstancy) {
        this.allowChooseInstancy = allowChooseInstancy;
    }
}

