package com.kcmp.core.ck.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 移除和添加关系的输入参数
 * @email kikock@qq.com
 **/
public class RelationParam implements Serializable {
    private static final long serialVersionUID = 176130572071065971L;

    /**
     * 父实体Id
     */
    protected String parentId;
    /**
     * 子实体Id清单
     */
    protected List<String> childIds;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }
}
