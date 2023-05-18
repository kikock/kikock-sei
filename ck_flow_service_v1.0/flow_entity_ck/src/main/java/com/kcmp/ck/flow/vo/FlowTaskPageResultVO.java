package com.kcmp.ck.flow.vo;



import com.kcmp.core.ck.dto.PageResult;

import java.io.Serializable;

/**
 * Created by kikock
 * 待办任务返回vo
 * @email kikock@qq.com
 **/
public class FlowTaskPageResultVO<T extends Serializable> extends PageResult<T> {
    private Long allTotal;//所有待办的总数

    public Long getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(Long allTotal) {
        this.allTotal = allTotal;
    }
}
