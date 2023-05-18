package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 调用子流程外参数
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activiti:out")
public class CallActivityOutParam implements Serializable {
    @XmlAttribute
    private String source;
    @XmlAttribute
    private String target;
    @XmlAttribute
    private String sourceExpression;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSourceExpression() {
        return sourceExpression;
    }

    public void setSourceExpression(String sourceExpression) {
        this.sourceExpression = sourceExpression;
    }
}
