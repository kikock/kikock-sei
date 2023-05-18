package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 执行监听器对象
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activiti:executionListener")
public class ExecutionListener implements Serializable {
    @XmlAttribute
    private String event;
    @XmlAttribute
    private String delegateExpression;
    @XmlAttribute
    private String expression;
    @XmlAttribute(name="class")
    private String classStr;

    private List<ActivitiField> activitiField;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDelegateExpression() {
        return delegateExpression;
    }

    public void setDelegateExpression(String delegateExpression) {
        this.delegateExpression = delegateExpression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public List<ActivitiField> getActivitiField() {
        return activitiField;
    }

    public void setActivitiField(List<ActivitiField> activitiField) {
        this.activitiField = activitiField;
    }
}
