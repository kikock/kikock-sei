package com.kcmp.ck.flow.constant;

import com.kcmp.ck.annotation.Remark;

import java.io.Serializable;

/**
 * Created by kikock
 * 待办状态
 * @email kikock@qq.com
 **/
public enum FlowDefinationStatus implements Serializable{

    /**
     * 未发布
     */
    @Remark("未发布")
    INIT,
    /**
     * 激活
     */
    @Remark("激活")//已经发布并可用
    Activate,

    /**
     * 冻结
     */
    @Remark("冻结")//已经发布，并手动冻结
    Freeze;
}
