package com.kcmp.core.ck.entity.vo;

import com.kcmp.core.ck.entity.IDataDict;

import java.io.Serializable;

/**
 * Created by kikock
 * 字典数据vo
 * @email kikock@qq.com
 **/
public class DataDictVo implements IDataDict, Serializable {
    private static final long serialVersionUID = 421246793236636521L;
    /**
     * 字典分类code
     */
    private String categoryCode;
    /**
     * 代码
     */
    private String code;
    /**
     * 值
     */
    private String value;
    /**
     * 值名称
     */
    private String valueName;

    @Override
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
