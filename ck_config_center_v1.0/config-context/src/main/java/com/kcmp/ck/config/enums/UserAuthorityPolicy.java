package com.kcmp.ck.config.enums;


import com.kcmp.ck.config.annotation.Remark;

/**
 * <strong>实现功能:</strong>.
 * <p>平台用户权限策略</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public enum UserAuthorityPolicy {

    @Remark("一般用户")
    NormalUser,

    @Remark("租户管理员")
    TenantAdmin,

    @Remark("全局管理员")
    GlobalAdmin
}
