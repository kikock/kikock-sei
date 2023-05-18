package com.kcmp.ck.flow.constant;

import com.kcmp.ck.annotation.Remark;

import java.io.Serializable;

/**
 * Created by kikock
 * 任务紧急程度
 * @email kikock@qq.com
 **/
public enum UrgencyStatus implements Serializable {
    /**
     * 一般状态
     */
    @Remark("一般")
    NORMAL(0),
    /**
     * 紧急
     */
    @Remark("紧急")
    URGENT(1);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    UrgencyStatus(int value) {
        this.value = value;
    }
}
