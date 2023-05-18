package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 调用子流程
 * @email kikock@qq.com
 **/
@XmlType(name = "callActivity")
public class CallActivity extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 调用的流程id
     */
    @XmlAttribute
    private String calledElement;

    public String getCalledElement() {
        return calledElement;
    }

    public void setCalledElement(String calledElement) {
        this.calledElement = calledElement;
    }
}
