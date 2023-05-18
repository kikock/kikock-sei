package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 查找执行人vo对象
 * @email kikock@qq.com
 **/
public class FindExecutorsVo  implements Serializable {

    private String requestExecutorsVos;

    private String businessModelCode;

    private String businessId;

    public String getRequestExecutorsVos() {
        return requestExecutorsVos;
    }

    public void setRequestExecutorsVos(String requestExecutorsVos) {
        this.requestExecutorsVos = requestExecutorsVos;
    }

    public String getBusinessModelCode() {
        return businessModelCode;
    }

    public void setBusinessModelCode(String businessModelCode) {
        this.businessModelCode = businessModelCode;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
