package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by kikock
 * bmpn连线类
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conditionExpression")
public class ConditionExpression implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "xsi:type")
    private String type = "tFormalExpression";

    @XmlValue
    private String uel;

    public String getUel() {
        return uel;
    }

    public void setUel(String uel) {
        this.uel = uel;
    }

    public ConditionExpression(String uel) {
        this.uel = uel;
    }

    public ConditionExpression(){}
}
