package com.kcmp.core.ck.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Persistable;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by kikock
 * 平台最基本的实体
 * @email kikock@qq.com
 **/
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID>, Serializable {
    private static final long serialVersionUID = 290247231882391642L;

    /**
     * 实体id
     */
    public abstract void setId(ID id);

    /**
     * 用于快速判断对象是否新建状态
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        Serializable id = getId();
        return null == id || StringUtils.isBlank(String.valueOf(id));
    }

    /**
     * 用于前端显示
     * @return 返回显示字符
     */
    @Transient
    public abstract String getDisplay();

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Persistable that = (Persistable) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += ((null == getId()) ? 0 : getId().hashCode()) * 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }
}
