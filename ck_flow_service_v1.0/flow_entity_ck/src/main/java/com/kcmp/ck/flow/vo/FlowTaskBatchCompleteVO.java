package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 执行任务完成时的传输对象
 * @email kikock@qq.com
 **/
public class FlowTaskBatchCompleteVO implements Serializable{

    /**
     * 任务id
     */
    private List<String> taskIdList;
    /**
     * 手动选择出口分支节点的节点路径，id
     * 如果没有子流程，key与value的值均设置为id
     */
    private Map<String,String> manualSelectedNode ;


    /**
     * 审批意见
     */
    private String opinion;
    /**
     * 额外参数
     */
    Map<String, Object> variables;


    public List<String> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(List<String> taskIdList) {
        this.taskIdList = taskIdList;
    }

    public Map<String, String> getManualSelectedNode() {
        return manualSelectedNode;
    }

    public void setManualSelectedNode(Map<String, String> manualSelectedNode) {
        this.manualSelectedNode = manualSelectedNode;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
