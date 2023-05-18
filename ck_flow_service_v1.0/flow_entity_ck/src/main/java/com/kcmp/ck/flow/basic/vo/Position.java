package com.kcmp.ck.flow.basic.vo;

import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.ICodeUnique;
import com.kcmp.core.ck.entity.ITenant;

/**
 * Created by kikock
 * 岗位实体
 * @email kikock@qq.com
 **/
//@Access(AccessType.FIELD)
//@Entity
//@Table(name = "position")
//@DynamicInsert
//@DynamicUpdate
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Position extends BaseAudiTableEntity
        implements ITenant, ICodeUnique {
    /**
     * 代码
     */
//    @Column(name = "code",unique = true, length = 8, nullable = false)
    private String code;
    /**
     * 名称
     */
//    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * 租户代码
     */
//    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;

//    @ManyToOne
//    @JoinColumn(name = "organization_Id",nullable = false)
    private Organization organization;

//    @ManyToOne
//    @JoinColumn(name = "category_id",nullable = false)
    private PositionCategory positionCategory;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PositionCategory getPositionCategory() {
        return positionCategory;
    }

    public void setPositionCategory(PositionCategory positionCategory) {
        this.positionCategory = positionCategory;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}
