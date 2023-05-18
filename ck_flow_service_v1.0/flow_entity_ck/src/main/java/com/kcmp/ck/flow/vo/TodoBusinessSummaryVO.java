package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 待办汇总vo对象
 * @email kikock@qq.com
 **/
public class TodoBusinessSummaryVO implements Serializable {
    /**
     * 业务实体类型id
     */
    private String businessModeId;

    /**
     * 业务实体类型代码(类全路径)
     */
    private String businessModelCode;

    /**
     * 业务单据数据
     */
    private int count;

    /**
     * 业务实体类型名称
     */
    private String businessModelName;

    public String getBusinessModeId() {
        return businessModeId;
    }

    public void setBusinessModeId(String businessModeId) {
        this.businessModeId = businessModeId;
    }

    public String getBusinessModelCode() {
        return businessModelCode;
    }

    public void setBusinessModelCode(String businessModelCode) {
        this.businessModelCode = businessModelCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }
}
