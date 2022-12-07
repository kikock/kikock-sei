package com.kcmp.ck.config.entity.dto;


import com.kcmp.ck.config.entity.enums.OperateStatusEnum;

import java.io.Serializable;

/**
 * Created by kikock
 * 操作结果
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class OperateResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作状态
     */
    private OperateStatusEnum operateStatusEnum;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 默认构造函数
     */
    public OperateResult() {
        this.operateStatusEnum = OperateStatusEnum.SUCCESS;
        this.message = "操作成功！";
    }

    /**
     * 构造函数
     *
     * @param message 返回消息
     */
    public OperateResult(String message) {
        this.operateStatusEnum = OperateStatusEnum.SUCCESS;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param operateStatusEnum 操作状态
     * @param message           返回消息
     */
    public OperateResult(OperateStatusEnum operateStatusEnum, String message) {
        this.operateStatusEnum = operateStatusEnum;
        this.message = message;
    }

    public OperateStatusEnum getOperateStatusEnum() {
        return operateStatusEnum;
    }

    public void setOperateStatusEnum(OperateStatusEnum operateStatusEnum) {
        this.operateStatusEnum = operateStatusEnum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 操作成功
     *
     * @return 是否成功
     */
    public boolean successful() {
        return OperateStatusEnum.SUCCESS.equals(operateStatusEnum);
    }

    /**
     * 操作失败
     *
     * @return 是否失败
     */
    public boolean unSuccessful() {
        return !successful();
    }
}
