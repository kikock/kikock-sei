package com.kcmp.ck.flow.entity;

import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.ITenant;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by kikock
 * 流程服务地址Entity定义
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity()
@Table(name = "flow_service_url")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowServiceUrl extends BaseAudiTableEntity implements ITenant {

    /**
     * 乐观锁-版本
     */
    //@Version
    @Column(name = "version")
    private Integer version = 0;

    /**
     * 名称
     */
    @Column(length = 80, nullable = false)
    private String name;

    /**
     * 代码
     */
    @Column(length = 60, nullable = false,unique = true)
    private String code;

    /**
     * URL服务地址
     */
    @Lob
    @Basic(fetch=FetchType.LAZY)
    private String url;

    /**
     * 描述
     */
    @Column(length = 250)
    private String depict;

    /**
     * 关联业务实体模型
     */
    @ManyToOne()
    @JoinColumn(name = "business_model_id")
    private BusinessModel businessModel;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10)
    private String tenantCode;


    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BusinessModel getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(BusinessModel businessModel) {
        this.businessModel = businessModel;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", this.name)
                .append("code", this.code)
                .append("url", this.url)
                .append("depict",this.depict)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
