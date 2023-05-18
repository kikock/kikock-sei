package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 服务任务
 * @email kikock@qq.com
 **/
@XmlType(name = "serviceTask")
public class ServiceTask extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "activiti:delegateExpression")
    private String delegateExpression;

    @XmlAttribute(name = "activiti:expression")
    private String expression;

    @XmlAttribute(name = "activiti:class")
    private String classStr;

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
}
