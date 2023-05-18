package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 固定开始任务vo对象
 * @email kikock@qq.com
 **/
public class SolidifyStartFlowVo implements Serializable {

    /**
     * 业务单据ID
     */
    private String businessId;

    /**
     * 业务实体类路径
     */
    private String businessModelCode;

    /**
     * 流程定义ID
     */
    private String flowDefinationId;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessModelCode() {
        return businessModelCode;
    }

    public void setBusinessModelCode(String businessModelCode) {
        this.businessModelCode = businessModelCode;
    }

    public String getFlowDefinationId() {
        return flowDefinationId;
    }

    public void setFlowDefinationId(String flowDefinationId) {
        this.flowDefinationId = flowDefinationId;
    }
}
