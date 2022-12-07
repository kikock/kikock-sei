package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 分配关系业务实体基类接口
 * @email kikock@qq.com
 **/
public interface RelationEntity<P extends AbstractEntity<String>, C extends AbstractEntity<String>> {
    /**
     * 父实体属性名
     */
    String PARENT_FIELD = "parent";
    /**
     * 父实体属性名
     */
    String CHILD_FIELD = "child";
    /**
     * 父实体
     */
    P getParent();

    void setParent(P parent);

    /**
     * 子实体
     */
    C getChild();

    void setChild(C child);
}
