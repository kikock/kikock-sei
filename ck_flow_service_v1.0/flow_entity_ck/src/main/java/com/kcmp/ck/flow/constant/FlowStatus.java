package com.kcmp.ck.flow.constant;

import com.kcmp.ck.annotation.Remark;

import java.io.Serializable;

/**
 * Created by kikock
 * 业务实体状态
 * @email kikock@qq.com
 **/
public enum FlowStatus implements Serializable{

    /**
     * 未进入流程
     */
    @Remark("初始化状态")
    INIT("init"),
    /**
     * 流程处理中
     */
    @Remark("流程中")
    INPROCESS("inProcess"),
    /**
     * 流程处理完成
     */
    @Remark("流程处理完成")
    COMPLETED("completed");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    FlowStatus(String value) {
        this.value = value;
    }

}
