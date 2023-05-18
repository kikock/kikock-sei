package com.kcmp.ck.flow.vo;

import com.kcmp.ck.flow.basic.vo.Executor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 节点和默认执行人信息
 * @email kikock@qq.com
 **/
public class NodeAndExecutes  implements Serializable {

    /**
     * 流程节点信息
     */
    private FlowNodeVO   flowNode;

    /**
     * 默认配置的执行人信息
      */
    private List<Executor> executorList;

    public NodeAndExecutes(){
    }

    public NodeAndExecutes(FlowNodeVO flowNode,List<Executor> executorList){
        this.flowNode = flowNode;
        this.executorList = executorList;
    }


    public FlowNodeVO getFlowNode() {
        return flowNode;
    }

    public void setFlowNode(FlowNodeVO flowNode) {
        this.flowNode = flowNode;
    }

    public List<Executor> getExecutorList() {
        return executorList;
    }

    public void setExecutorList(List<Executor> executorList) {
        this.executorList = executorList;
    }
}
