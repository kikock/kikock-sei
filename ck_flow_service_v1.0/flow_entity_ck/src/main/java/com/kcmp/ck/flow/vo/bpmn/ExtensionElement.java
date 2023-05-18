package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 执行监听器元素
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extensionElements")
public class ExtensionElement implements Serializable{
    @XmlElement(name = "activiti:taskListener")
    private List<TaskListener> taskListener;

    @XmlElement(name = "activiti:executionListener")
    private List<ExecutionListener> executionListener;

    @XmlElement(name = "activiti:in")
    private List<CallActivityInParam> callActivityInParam;

    @XmlElement(name = "activiti:out")
    private List<CallActivityOutParam> callActivityOutParam;

    public List<TaskListener> getTaskListener() {
        return taskListener;
    }

    public void setTaskListener(List<TaskListener> taskListener) {
        this.taskListener = taskListener;
    }

    public List<ExecutionListener> getExecutionListener() {
        return executionListener;
    }

    public void setExecutionListener(List<ExecutionListener> executionListener) {
        this.executionListener = executionListener;
    }

    public List<CallActivityInParam> getCallActivityInParam() {
        return callActivityInParam;
    }

    public void setCallActivityInParam(List<CallActivityInParam> callActivityInParam) {
        this.callActivityInParam = callActivityInParam;
    }

    public List<CallActivityOutParam> getCallActivityOutParam() {
        return callActivityOutParam;
    }

    public void setCallActivityOutParam(List<CallActivityOutParam> callActivityOutParam) {
        this.callActivityOutParam = callActivityOutParam;
    }
}
