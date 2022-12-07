package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 代码唯一的业务实体特征接口
 * @email kikock@qq.com
 **/
public interface ICodeUnique {
    String CODE_FIELD = "code";

    String getCode();

    void setCode(String code);
}
