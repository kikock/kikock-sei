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
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by kikock
 * 流程类型实体类
 * @email kikock@qq.com
 **/
@Entity()
@Table(name = "flow_type")
@DynamicInsert
@DynamicUpdate
@Cacheable(true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowType extends BaseAudiTableEntity implements ITenant {

    public FlowType(){}

    /**
     * 乐观锁-版本
     */
   // @Version
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
     * 拥有的流程定义
     */
    @Transient
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "flowType")
    private Set<FlowDefination> flowDefinations = new LinkedHashSet<>(0);

    /**
     * 完成任务时调用的web地址
     */
    @Column(length = 255, name = "complete_task_service_url")
    private String completeTaskServiceUrl;

    /**
     * 查看单据的URL
     */
    @Column(length = 6000,name = "look_url")
    @Lob
    private String lookUrl;

    /**
     * 业务单据明细服务地址-主要供移动端使用
     */
    @Column(name="business_detail_service_url")
    private String businessDetailServiceUrl;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10)
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

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public BusinessModel getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(BusinessModel businessModel) {
        this.businessModel = businessModel;
    }

    public Set<FlowDefination> getFlowDefinations() {
        return this.flowDefinations;
    }

    public void setFlowDefinations(Set<FlowDefination> flowDefinations) {
        this.flowDefinations = flowDefinations;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", name)
                .append("code", code)
                .append("depict", depict)
                .append("businessModel", businessModel)
                .append("completeTaskServiceUrl", completeTaskServiceUrl)
                .append("lookUrl", lookUrl)
                .append("flowDefinations", flowDefinations)
                .append("businessDetailServiceUrl",businessDetailServiceUrl)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String getCompleteTaskServiceUrl() {
        return completeTaskServiceUrl;
    }

    public void setCompleteTaskServiceUrl(String completeTaskServiceUrl) {
        this.completeTaskServiceUrl = completeTaskServiceUrl;
    }

    public String getLookUrl() {
        return lookUrl;
    }

    public void setLookUrl(String lookUrl) {
        this.lookUrl = lookUrl;
    }

    public String getBusinessDetailServiceUrl() {
        return businessDetailServiceUrl;
    }

    public void setBusinessDetailServiceUrl(String businessDetailServiceUrl) {
        this.businessDetailServiceUrl = businessDetailServiceUrl;
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
