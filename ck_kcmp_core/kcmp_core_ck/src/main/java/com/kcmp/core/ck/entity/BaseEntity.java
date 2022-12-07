package com.kcmp.core.ck.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by kikock
 * 业务实体持久化基类
 * @email kikock@qq.com
 **/
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity extends AbstractEntity<String> {

    private static final long serialVersionUID = 190675731769916235L;
    public static final String ID = "id";

    /**
     * 主键
     */
    @Id
    @Column(length = 36)
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用于前端显示
     * @return 返回[id]类名
     */
    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return null;
    }

}
