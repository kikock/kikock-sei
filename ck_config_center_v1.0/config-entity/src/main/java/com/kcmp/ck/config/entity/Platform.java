package com.kcmp.ck.config.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by kikock
 * 平台实体类
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "platform")
@DynamicInsert
@DynamicUpdate
public class Platform extends BaseConfigEntity {

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
    /**
     * 排序
     */
    @Column(name = "rank", nullable = false)
    private Integer rank = 0;

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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
