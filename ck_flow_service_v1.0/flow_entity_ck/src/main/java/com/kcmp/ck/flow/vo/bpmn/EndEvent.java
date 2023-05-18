package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 结束事件
 * @email kikock@qq.com
 **/
@XmlType(name = "endEvent")
public class EndEvent extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "terminateEventDefinition")
    private String terminateEventDefinition;

    public String getTerminateEventDefinition() {
        return terminateEventDefinition;
    }

    public void setTerminateEventDefinition(String terminateEventDefinition) {
        this.terminateEventDefinition = terminateEventDefinition;
    }
}
