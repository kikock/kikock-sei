package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kikock
 * 业务表单值
 * @email kikock@qq.com
 **/
public class BusinessFormValue implements Serializable {
    /**
     * 值
     */
    private Object value;

    private Map<String, Object> son;


    public BusinessFormValue(){}

    public BusinessFormValue(Object value){
        this.value = value;
    }

    public BusinessFormValue( Object value,Map<String, Object> son){
        this.value = value;
        this.son = son;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isHasSon() {
        return this.son == null || this.son.isEmpty() ? false:true;
    }

    public Map<String, Object> getSon() {
        return son;
    }

    public void setSon(Map<String, Object> son) {
        this.son = son;
    }
}
