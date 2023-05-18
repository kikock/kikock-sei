package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by kikock
 * bmpn文档
 * @email kikock@qq.com
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentation")
public class Documentation implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlValue
    private String value;

    public Documentation(){

    }

    public Documentation(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
