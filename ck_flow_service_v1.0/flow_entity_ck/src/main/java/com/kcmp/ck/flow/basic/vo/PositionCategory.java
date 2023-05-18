package com.kcmp.ck.flow.basic.vo;

import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.ICodeUnique;
import com.kcmp.core.ck.entity.ITenant;

/**
 * Created by kikock
 * 岗位类别实体
 * @email kikock@qq.com
 **/
//@Access(AccessType.FIELD)
//@Entity
//@Table(name="position_category")
//@DynamicInsert
//@DynamicUpdate
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PositionCategory extends BaseAudiTableEntity
        implements ITenant, ICodeUnique {

    /**
     * 代码
     */
//    @Column(name = "code",length = 6,nullable = false)
    private String code;

    /**
     * 名称
     */
//    @Column(name = "name",length = 50,nullable = false)
    private String name;

    /**
     * 租户代码
     */
//    @Column( name="tenant_code",length = 10,nullable = false)
    private String tenantCode;

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

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
