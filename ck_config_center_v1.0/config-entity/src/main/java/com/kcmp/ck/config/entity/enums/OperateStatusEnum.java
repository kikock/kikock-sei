package com.kcmp.ck.config.entity.enums;


import com.kcmp.ck.config.annotation.Remark;

/**
 * Created by kikock
 * 操作状态
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public enum OperateStatusEnum {
    /**
     * 成功
     */
    @Remark("成功")
    SUCCESS,
    /**
     * 失败
     */
    @Remark("失败")
    ERROR,
    /**
     * 警告
     */
    @Remark("警告")
    WARNING
}
