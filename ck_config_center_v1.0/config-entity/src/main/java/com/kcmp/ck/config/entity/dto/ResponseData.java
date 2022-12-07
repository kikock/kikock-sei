package com.kcmp.ck.config.entity.dto;

import java.io.Serializable;

/**
 * Created by kikock
 * 带数据集的返回数据
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class ResponseData implements Serializable {
    private static final long serialVersionUID = 51233686047226545L;

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
