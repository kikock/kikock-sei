package com.kcmp.core.ck.entity;

import java.util.List;

/**
 * Created by kikock
 * 树形结构特征接口
 * @email kikock@qq.com
 **/
public interface TreeEntity<T extends TreeEntity<T>> extends IRank {
    /**
     * 代码路径分隔符
     */
    String CODE_DELIMITER = "|";
    /**
     * 名称路径分隔符
     */
    String NAME_DELIMITER = "/";
    /**
     * 代码
     */
    String CODE = "code";
    /**
     * 名称
     */
    String NAME = "name";
    /**
     * 层级
     */
    String NODE_LEVEL = "nodeLevel";
    /**
     * 代码路径
     */
    String CODE_PATH = "codePath";
    /**
     * 名称路径
     */
    String NAME_PATH = "namePath";
    /**
     * 父节点id
     */
    String PARENT_ID = "parentId";

    /**
     * 获取Id标识
     */
    String getId();
    /**
     * 获取代码
     */
    String getCode();
    /**
     * 获取名称
     */
    String getName();
    /**
     * 获取层级
     */
    Integer getNodeLevel();
    /**
     * 设置层级
     */
    void setNodeLevel(Integer nodeLevel);
    /**
     * 获取代码路径
     */
    String getCodePath();
    /**
     * 设置代码路径
     */
    void setCodePath(String codePath);
    /**
     * 获取名称路径
     */
    String getNamePath();
    /**
     * 设置名称路径
     */
    void setNamePath(String namePath);
    /**
     * 获取父节点Id
     */
    String getParentId();
    /**
     * 设置父节点Id
     */
    void setParentId(String parentId);

    /**
     * 获取排序
     */
    @Override
    Integer getRank();

    List<T> getChildren();

    void setChildren(List<T> children);
}
