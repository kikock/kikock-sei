package com.kcmp.ck.config.enums;

import com.kcmp.ck.config.annotation.Remark;

/**
 * 实现功能：用户类型：枚举
 *
 * @author ck
 * @version 1.0.0
 */
public enum UserType {
    /**
     * 员工
     */
    @Remark("员工")
    Employee,

    /**
     * 供应商
     */
    @Remark("供应商")
    Supplier,
    /**
     * 客户
     */
    @Remark("客户")
    Customer,

    /**
     * 专家
     */
    @Remark("专家")
    Expert,
    /////////////产融管理用户类型/////////////////////////////////////
    /**
     * 核心企业
     */
    @Remark("核心企业")
    CoreEnterprise,
    /**
     * 产融供应商
     */
    @Remark("产融供应商")
    FinancialSupplier,
    /**
     * 产融供应商
     */
    @Remark("融资机构")
    FinancialAgent,

    ///////////////////////////////////////;
}
