package com.kcmp.ck.config.entity.vo;

import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;

/**
 * Created by kikock
 * 操作结果vo
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class OperateResultVo extends OperateResult {

    /**
     * 操作成功
     */
    private boolean success;
    /**
     * 操作消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

    /**
     * 默认构造函数
     */
    public OperateResultVo() {
    }

    /**
     * 构造函数
     */
    public OperateResultVo(String msg,boolean success) {
        setOperateStatusEnum(OperateStatusEnum.ERROR);
        if (success){
            setOperateStatusEnum(OperateStatusEnum.SUCCESS);
        }
        setMessage(msg);
        this.data = data;
        this.success = success;
        this.msg = msg;
    }


    /**
     * 构造函数
     */
    public OperateResultVo(OperateResult operateResult, Object data) {
        setOperateStatusEnum(operateResult.getOperateStatusEnum());
        setMessage(operateResult.getMessage());
        this.data = data;
        this.success = operateResult.successful();
        this.msg = operateResult.getMessage();
    }

    /**
     * 构造函数
     */
    public OperateResultVo(OperateResult operateResult) {
        setOperateStatusEnum(operateResult.getOperateStatusEnum());
        setMessage(operateResult.getMessage());
        this.success = operateResult.successful();
        this.msg = operateResult.getMessage();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
