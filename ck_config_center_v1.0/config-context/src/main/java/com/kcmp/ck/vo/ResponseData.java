package com.kcmp.ck.vo;

import java.io.Serializable;

/**
 * @author kikock
 * @version 1.0.0
 */
public class ResponseData implements Serializable {
    /**
     * 成功标志
     */
    private Boolean success = Boolean.TRUE;

    /**
     * 返回数据
     */
    private Integer statusCode = 200;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public Boolean getSuccess() {
        return success;
    }

    public ResponseData setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public ResponseData setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseData setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseData setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 是否成功
     *
     * @return 返回true，则表示操作成功，反之失败
     */
    public boolean successful() {
        return success;
    }

    /**
     * 是否未成功
     *
     * @return 返回true，则表示操作失败，反之成功
     */
    public boolean notSuccessful() {
        return !successful();
    }

    public static ResponseData build() {
        return new ResponseData();
    }

}
