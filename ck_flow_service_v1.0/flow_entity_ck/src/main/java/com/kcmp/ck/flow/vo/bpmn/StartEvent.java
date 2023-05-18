package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 开始事件
 * @email kikock@qq.com
 **/
@XmlType(name = "startEvent")
public class StartEvent extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 流程启动人
     */
    @XmlAttribute(name = "activiti:initiator")
    private String initiator = "startUserId";

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
}
