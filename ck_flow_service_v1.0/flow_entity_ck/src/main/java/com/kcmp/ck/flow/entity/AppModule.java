package com.kcmp.ck.flow.entity;

import javax.persistence.*;

import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.ICodeUnique;
import com.kcmp.core.ck.entity.IRank;
import com.kcmp.core.ck.entity.ITenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Created by kikock
 * 应用模块实体类
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity()
@Table(name = "app_module")
@DynamicInsert
@DynamicUpdate
@Cacheable(true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppModule extends BaseAudiTableEntity
        implements ICodeUnique, IRank {

    /**
     * 名称
     */
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    /**
     * 代码
     */
    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;


    /**
     * web基地址
     */
    @Column(name = "web_base_address")
    private String webBaseAddress;

    /**
     * api基地址
     */
    @Column(name = "api_base_address")
    private String apiBaseAddress;


    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10)
    private String tenantCode;

    /**
     * 排序号
     */
    @Column(name = "rank", nullable = false)
    private Integer rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getWebBaseAddress() {
        return webBaseAddress;
    }

    public void setWebBaseAddress(String webBaseAddress) {
        this.webBaseAddress = webBaseAddress;
    }

    public String getApiBaseAddress() {
        return apiBaseAddress;
    }

    public void setApiBaseAddress(String apiBaseAddress) {
        this.apiBaseAddress = apiBaseAddress;
    }

//    @Override
    public String getTenantCode() {
        return tenantCode;
    }

//    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
