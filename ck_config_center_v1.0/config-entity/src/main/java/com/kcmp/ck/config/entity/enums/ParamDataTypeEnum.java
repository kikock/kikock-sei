package com.kcmp.ck.config.entity.enums;


import com.kcmp.ck.config.annotation.Remark;

/**
 * Created by kikock
 * 全局参数数据类型
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public enum ParamDataTypeEnum {
    /**
     * 字符串
     */
    @Remark("字符串")
    STRING,
    /**
     * 键值对
     */
    @Remark("键值对")
    KEY_VALUES,
    /**
     * 配置键
     */
    @Remark("引用")
    REFERENCE
}
