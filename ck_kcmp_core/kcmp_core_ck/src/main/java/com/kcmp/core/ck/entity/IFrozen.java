package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 冻结属性接口
 * @email kikock@qq.com
 **/
public interface IFrozen {

    String FROZEN = "frozen";

    Boolean getFrozen();

    void setFrozen(Boolean frozen);
}
