package com.kcmp.core.ck.entity.auth;

/**
 * Created by kikock
 * 权限管理的树形业务实体接口
 * @email kikock@qq.com
 **/
public interface IDataAuthTreeEntity extends IDataAuthEntity {
    Integer getRank();

    void setRank(Integer rank);

    Integer getNodeLevel();

    void setNodeLevel(Integer nodeLevel);

    String getParentId();

    void setParentId(String parentId);

    String getCodePath();

    void setCodePath(String codePath);

    String getNamePath();

    void setNamePath(String namePath);
}
