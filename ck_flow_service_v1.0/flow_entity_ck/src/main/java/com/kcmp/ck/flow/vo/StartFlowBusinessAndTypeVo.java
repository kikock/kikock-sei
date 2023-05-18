package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 通过业务信息和流程类型启动流程的参数vo
 * @email kikock@qq.com
 **/
public class StartFlowBusinessAndTypeVo  implements Serializable{
    /**
     * 业务实体代码
     */
    private String businessModelCode;
    /**
     * 业务实体id
     */
    private String businessKey;

    /**
     * 流程类型代码
     */
    private String  flowTypeCode;



    public String getBusinessModelCode() {
        return businessModelCode;
    }

    public void setBusinessModelCode(String businessModelCode) {
        this.businessModelCode = businessModelCode;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getFlowTypeCode() {
        return flowTypeCode;
    }

    public void setFlowTypeCode(String flowTypeCode) {
        this.flowTypeCode = flowTypeCode;
    }
}
