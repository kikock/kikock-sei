package com.kcmp.ck.flow.util;

/**
 * Created by kikock
 * 定义任务状态
 * @author kikock
 * @email kikock@qq.com
 **/
public enum TaskStatus {

    /**
     * 待办、签收、撤销、完成、驳回、挂起、删除
     */
    INIT("init"),
    CLAIM("claim"),
    CANCEL("cancel"),
    COMPLETED("completed"),
    REJECT("reject"),
    SUSPEND("suspend"),
    DELETE("delete");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    TaskStatus(String value) {
        this.value = value;
    }

}
