package com.kcmp.ck.config.entity.vo;


import com.kcmp.ck.config.entity.SerialNumberConfig;

/**
 * Created by kikock
 * 生成并获得一个序列编号的返回结果
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class FetchNumberResult {

    /**
     * 缓存键
     */
    private String key;
    /**
     * 缓存配置值
     */
    private SerialNumberConfig serialNumberConfig;
    /**
     * 新的当前编号
     */
    private String currentNumber;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SerialNumberConfig getSerialNumberConfig() {
        return serialNumberConfig;
    }

    public void setSerialNumberConfig(SerialNumberConfig serialNumberConfig) {
        this.serialNumberConfig = serialNumberConfig;
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(String currentNumber) {
        this.currentNumber = currentNumber;
    }
}
