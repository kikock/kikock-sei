package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Created by kikock
 * bmpn xml通用基类
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
