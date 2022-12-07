package com.kcmp.ck.center.config;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by kikock
 * 权限
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.setAuthority(authority);
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
