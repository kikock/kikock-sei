package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 数据字典特征接口
 * @email kikock@qq.com
 **/
public interface IDataDict {
    /**
     * 字典分类code
     */
    String getCategoryCode();

    /**
     * 代码
     */
    String getCode();

    /**
     * 值
     */
    String getValue();

    /**
     * 值名称
     */
    String getValueName();

}
