package com.kcmp.core.ck.entity.auth;

import java.io.Serializable;

/**
 * Created by kikock
 * 定义权限对象的数据（一般实体类）
 * @email kikock@qq.com
 **/
public class AuthEntityData implements Serializable, IDataAuthEntity {
    private static final long serialVersionUID = 309030188302256572L;

    /**
     * Id标识
     */
    protected String id;
    /**
     * 代码
     */
    protected String code;
    /**
     * 名称
     */
    protected String name;
    /**
     * 租户代码
     */
    protected String tenantCode;

    /**
     * 默认构造函数
     */
    public AuthEntityData() {
    }

    /**
     * 构造函数（业务实体）
     * @param entity 业务实体
     */
    public AuthEntityData(IDataAuthEntity entity) {
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        tenantCode = entity.getTenantCode();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthEntityData)) {
            return false;
        }

        AuthEntityData that = (AuthEntityData) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
