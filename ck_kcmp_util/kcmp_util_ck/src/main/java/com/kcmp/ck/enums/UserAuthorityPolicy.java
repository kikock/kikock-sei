package com.kcmp.ck.enums;

import com.kcmp.ck.annotation.Remark;

/**
 * Created by kikock
 * 平台用户权限策略
 * @email kikock@qq.com
 **/
public enum UserAuthorityPolicy {
    @Remark("一般用户")
    NormalUser,

    @Remark("租户管理员")
    TenantAdmin,

    @Remark("全局管理员")
    GlobalAdmin
}
