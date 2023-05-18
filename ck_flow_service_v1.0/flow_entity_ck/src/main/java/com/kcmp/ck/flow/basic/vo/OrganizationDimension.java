package com.kcmp.ck.flow.basic.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 组织维度
 * @email kikock@qq.com
 **/
public class OrganizationDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
