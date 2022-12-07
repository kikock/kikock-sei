package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 租户业务实体特征接口
 * @email kikock@qq.com
 **/
public interface ITenant {
    /**
     * 租户代码属性
     */
    String TENANT_CODE = "tenantCode";

    /**
     * 租户代码
     */
    String getTenantCode();

    /**
     * 设置租户代码
     */
    void setTenantCode(String tenantCode);
}
