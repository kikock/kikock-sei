package com.kcmp.ck.flow.basic.vo;

import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.core.ck.entity.BaseAudiTableEntity;

/**
 * Created by kikock
 * 企业员工实体
 * @email kikock@qq.com
 **/
public class Employee extends BaseAudiTableEntity implements ITenant {
    /**
     * 员工编号
     */
//    @Column(name = "code", length = 10, nullable = false)
    private String code;

    /**
     * 租户代码
     */
//    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;

//    @OneToOne
//    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "organization_id",nullable = false)
    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return 租户代码
     */
    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    /**
     * 用户姓名
     */
//    @Transient
    private String userName;

    /**
     * 是否冻结
     */
//    @Transient
    private boolean frozen;

    /**
     * 是否是创建租户管理员
     */
//    @Transient
    private boolean createAdmin;

    /**
     * 邮箱,创建租户管理员时发送邮件
     */
//    @Transient
    private String email;

    /**
     * 设置租户代码
     *
     * @param tenantCode 租户代码
     */
    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public boolean isCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(boolean createAdmin) {
        this.createAdmin = createAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
