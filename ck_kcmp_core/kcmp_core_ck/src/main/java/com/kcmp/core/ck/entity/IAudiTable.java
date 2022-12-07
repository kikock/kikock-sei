package com.kcmp.core.ck.entity;

import java.util.Date;

/**
 * Created by kikock
 * 表数据创建信息和最后修改信息
 * @email kikock@qq.com
 **/
public interface IAudiTable {

    String getCreatorId();

    void setCreatorId(String creatorId);

    String getCreatorAccount();

    void setCreatorAccount(String creatorAccount);

    String getCreatorName();

    void setCreatorName(String creatorName);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    String getLastEditorId();

    void setLastEditorId(String lastEditorId);

    String getLastEditorAccount();

    void setLastEditorAccount(String lastEditorAccount);

    String getLastEditorName();

    void setLastEditorName(String lastEditorName);

    Date getLastEditedDate();

    void setLastEditedDate(Date lastEditedDate);
}
