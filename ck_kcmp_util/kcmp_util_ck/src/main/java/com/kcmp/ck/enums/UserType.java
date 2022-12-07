package com.kcmp.ck.enums;

import com.kcmp.ck.annotation.Remark;

/**
 * Created by kikock
 * 用户类型：枚举
 * @email kikock@qq.com
 **/
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
    Expert;
}
