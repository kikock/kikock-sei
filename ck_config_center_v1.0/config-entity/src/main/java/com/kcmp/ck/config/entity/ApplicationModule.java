package com.kcmp.ck.config.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by kikock
 * 应用模块实体类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "application_module")
@DynamicInsert
@DynamicUpdate
public class ApplicationModule extends BaseConfigEntity {

    /**
     * 平台Id
     */
    @Column(name = "platform_id", nullable = false, length = 36)
    private String platformId;
    /**
     * 平台
     */
    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false, insertable = false, updatable = false)
    private Platform platform;
    /**
     * 代码
     */
    @Column(name = "code", length = 10, nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
