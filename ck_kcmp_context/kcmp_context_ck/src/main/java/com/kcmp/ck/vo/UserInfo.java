package com.kcmp.ck.vo;

import com.kcmp.ck.enums.UserAuthorityPolicy;
import com.kcmp.ck.enums.UserType;

import java.io.Serializable;

/**
 * Created by kikock
 * 用户账号
 * @email kikock@qq.com
 **/
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 141698980536669769L;
    /**
     * 账号
     */
    private String account;
    /**
     * 租户代码
     */
    private String tenantCode;
    /**
     * 用户
     */
    private User user;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User {
        /**
         * 用户姓名
         */
        private String id;
        /**
         * 用户姓名
         */
        private String userName;
        /**
         * 用户类型
         */
        private UserType userType;
        /**
         * 用户权限策略
         */
        private UserAuthorityPolicy userAuthorityPolicy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public UserType getUserType() {
            return userType;
        }

        public void setUserType(UserType userType) {
            this.userType = userType;
        }

        public UserAuthorityPolicy getUserAuthorityPolicy() {
            return userAuthorityPolicy;
        }

        public void setUserAuthorityPolicy(UserAuthorityPolicy userAuthorityPolicy) {
            this.userAuthorityPolicy = userAuthorityPolicy;
        }
    }
}
