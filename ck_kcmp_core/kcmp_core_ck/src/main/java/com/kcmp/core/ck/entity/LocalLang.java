package com.kcmp.core.ck.entity;

import java.io.Serializable;

/**
 * Created by kikock
 * 本地语言
 * @email kikock@qq.com
 **/
public class LocalLang implements Serializable {
    private static final long serialVersionUID = 6969321119829126L;
    /**
     * 语种
     */
    private String language;
    /**
     * 语言key
     */
    private String localKey;
    /**
     * 语义(本地化)
     */
    private String localValue;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocalKey() {
        return localKey;
    }

    public void setLocalKey(String localKey) {
        this.localKey = localKey;
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }
}
