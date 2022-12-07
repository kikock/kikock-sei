package com.kcmp.core.ck.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by kikock
 * 业务实体持久化基类
 * 提供了乐观锁支持和基本字段(创建人，创建时间，最后编辑人和编辑时间)
 * @email kikock@qq.com
 **/
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseAudiTableEntity extends BaseEntity implements IAudiTable {

    private static final long serialVersionUID = 379169556330919654L;
    public static final String CREATED_DATE = "createdDate";

    /**
     * 创建者id
     */
    @Column(name = "creator_id", length = 36, updatable = false)
    protected String creatorId;
    /**
     * 创建者账号
     */
    @Column(name = "creator_account", length = 50, updatable = false)
    protected String creatorAccount;
    /**
     * 创建者名字
     */
    @Column(name = "creator_name", length = 50, updatable = false)
    protected String creatorName;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    protected Date createdDate;
    /**
     * 最后修改者id
     */
    @Column(name = "last_editor_id", length = 36)
    protected String lastEditorId;
    /**
     * 最后修改者账号
     */
    @Column(name = "last_editor_account", length = 50)
    protected String lastEditorAccount;
    /**
     * 最后修改者姓名
     */
    @Column(name = "last_editor_name", length = 50)
    protected String lastEditorName;
    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_edited_date")
    protected Date lastEditedDate;

    @Override
    // @JsonIgnore
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    // @JsonIgnore
    public String getCreatorAccount() {
        return creatorAccount;
    }

    @Override
    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    @Override
    // @JsonIgnore
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    // @JsonIgnore
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    // @JsonIgnore
    public String getLastEditorId() {
        return lastEditorId;
    }

    @Override
    public void setLastEditorId(String lastEditorId) {
        this.lastEditorId = lastEditorId;
    }

    @Override
    // @JsonIgnore
    public String getLastEditorAccount() {
        return lastEditorAccount;
    }

    @Override
    public void setLastEditorAccount(String lastEditorAccount) {
        this.lastEditorAccount = lastEditorAccount;
    }

    @Override
    // @JsonIgnore
    public String getLastEditorName() {
        return lastEditorName;
    }

    @Override
    public void setLastEditorName(String lastEditorName) {
        this.lastEditorName = lastEditorName;
    }

    @Override
    // @JsonIgnore
    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    @Override
    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }
}
