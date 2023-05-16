package com.kcmp.test.ck.entity;



import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.IFrozen;
import com.kcmp.core.ck.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by kikock
 * 用户实体
 * @email kikock@qq.com
 * @create 2019-12-26 15:28
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "my_test")
@DynamicInsert
@DynamicUpdate
public class MyTest extends BaseAudiTableEntity implements ITenant, IFrozen {

    /**
     * 用户姓名
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * 是否冻结
     */
    @Column(name = "code",  length = 50, nullable = false)
    private String code;
    /**
     * 是否冻结
     */
    @Column(name = "frozen", nullable = false)
    private Boolean frozen = Boolean.FALSE;
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    @Override
    public Boolean getFrozen() {
        return null;
    }

    @Override
    public void setFrozen(Boolean frozen) {

    }
}
