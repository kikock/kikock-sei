package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 数据关系映射
 * @email kikock@qq.com
 **/
public interface IDataValue<ID, V> {
    /**
     * id
     */
    ID getId();

    /**
     * 名称
     */
    String getName();

    /**
     * 值
     */
    V getValue();
}
