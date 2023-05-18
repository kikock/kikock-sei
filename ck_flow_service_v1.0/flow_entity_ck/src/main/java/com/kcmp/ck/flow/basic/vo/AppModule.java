package com.kcmp.ck.flow.basic.vo;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.kcmp.core.ck.entity.BaseAudiTableEntity;
import com.kcmp.core.ck.entity.ICodeUnique;
import com.kcmp.core.ck.entity.IRank;
import com.kcmp.core.ck.entity.ITenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Created by kikock
 * 应用模块Entity定义
 * @author kikock
 * @email kikock@qq.com
 **/
public class AppModule extends BaseAudiTableEntity
        implements ICodeUnique, IRank, ITenant {

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
     * 排序号
     */
    @Column(name = "rank", nullable = false)

    /**
     * 租户代码
     */
    private String tenantCode;

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

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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
