package com.kcmp.ck.config.entity.dto;

import java.io.Serializable;

/**
 * Created by kikock
 * 参数配置的说明
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class ParamConfigVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 参数配置键
     */
    private String paramKey;
    /**
     * 参数说明
     */
    private String remark;

    /**
     * 默认构造函数
     */
    public ParamConfigVo() {
    }

    /**
     * 构造函数
     *
     * @param paramKey
     * @param remark
     */
    public ParamConfigVo(String paramKey, String remark) {
        this.paramKey = paramKey;
        this.remark = remark;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
