package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by kikock
 * 业务流程字段
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activiti:field")
public class ActivitiField implements Serializable {

    @XmlAttribute()
    private String name;
    @XmlElement(name = "activiti:string")
    private String activitiString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivitiString() {
        return activitiString;
    }

    public void setActivitiString(String activitiString) {
        this.activitiString = activitiString;
    }
}
