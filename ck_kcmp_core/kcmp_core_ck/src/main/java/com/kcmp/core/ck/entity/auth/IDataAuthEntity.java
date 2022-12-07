package com.kcmp.core.ck.entity.auth;

/**
 * Created by kikock
 * 数据权限管理的业务实体接口
 * @email kikock@qq.com
 **/
public interface IDataAuthEntity {
    //属性名:Id标识
    String ID_FIELD = "id";
    //属性名:代码
    String CODE_FIELD = "code";
    //属性名:名称
    String NAME_FIELD = "name";
    //属性名：租户代码
    String TENANTCODE_FIELE = "tenantCode";

    String getId();

    void setId(String id);

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);

    String getTenantCode();

    void setTenantCode(String tenantCode);
}
