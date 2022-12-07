package com.kcmp.ck.vo;

import com.kcmp.ck.config.annotation.Remark;
import com.kcmp.ck.config.util.EnumUtils;
import com.kcmp.ck.context.ContextUtil;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author kikock
 * @version 1.0.0
 */
public class BaseResult implements Serializable {
    /**
     * 操作状态
     */
    StatusEnum status;
    /**
     * 消息
     */
    String message;

    BaseResult() {
    }

    /**
     * @param status  操作状态
     * @param key     多语言key
     * @param msgArgs 消息参数
     */
    BaseResult(StatusEnum status, String key, Object[] msgArgs) {
        this.status = status;

        this.message = ContextUtil.getMessage(key, msgArgs, Locale.getDefault());
    }

    /**
     * 获取操作状态
     *
     * @return 返回操作状态
     * @see StatusEnum
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * 获取操作消息
     *
     * @return 返回操作消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 状态枚举
     * 1-成功；0-警告；-1-失败
     */
    protected enum StatusEnum {
        /**
         * 成功
         */
        @Remark(value = "成功")
        SUCCESS,
        /**
         * 失败
         */
        @Remark(value = "失败")
        FAILURE,
        /**
         * 警告
         */
        @Remark(value = "警告")
        WARNING;


        @Override
        public String toString() {
            return this.name() + "[" + this.ordinal() + "] - " + EnumUtils.getEnumItemRemark(StatusEnum.class, this);
        }
    }
}
