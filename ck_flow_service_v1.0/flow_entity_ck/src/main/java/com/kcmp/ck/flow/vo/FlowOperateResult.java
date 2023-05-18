package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 事件操作结果vo
 * @email kikock@qq.com
 **/
public class FlowOperateResult implements Serializable {
    /**
     * 成功状态
     */
    private boolean success;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 签收人，针对需要立即签收执行人的情况，如工作池任务（工作池任务添加多人待办，直接以逗号隔开）
     */
    private String userId;

    public FlowOperateResult() {
        this.success = true;
        this.message = "操作成功";
    }

    public FlowOperateResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String toString() {
        return "成功状态：" + this.isSuccess() + ";返回消息=" + this.getMessage();
    }
}
