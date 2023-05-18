package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 以默认值启动流程的参数
 * @email kikock@qq.com
 **/
public class DefaultStartParam implements Serializable {
    /**
     * 业务实体代码
     */
    private String businessModelCode;
    /**
     * 业务实体id
     */
    private  String businessKey;

    /**
     * 默认构造函数
     */
    public DefaultStartParam() {
    }

    /**
     * 构造函数
     * @param businessModelCode 业务实体类型代码
     * @param businessKey 业务实体Id
     */
    public DefaultStartParam(String businessModelCode, String businessKey) {
        this.businessModelCode = businessModelCode;
        this.businessKey = businessKey;
    }

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
}
