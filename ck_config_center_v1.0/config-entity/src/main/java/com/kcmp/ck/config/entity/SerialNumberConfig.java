package com.kcmp.ck.config.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by kikock
 * 编号生成器配置
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Access(AccessType.FIELD)
@Entity
@Table(name = "serial_number_config")
@DynamicInsert
@DynamicUpdate
public class SerialNumberConfig extends BaseConfigEntity {

    /**
     * 运行环境代码
     */
    @Column(name = "env_code", length = 10, nullable = false)
    private String envCode;
    /**
     * 实体类名（全名）
     */
    @Column(name = "entity_class_name", length = 100, nullable = false)
    private String entityClassName;
    /**
     * 实体名称
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    /**
     * 编号前缀
     */
    @Column(name = "prefixes", length = 10)
    private String prefixes;
    /**
     * 编号长度
     */
    @Column(name = "length", nullable = false)
    private int length;
    /**
     * 初始序号
     */
    @Column(name = "initial_serial", nullable = false)
    private int initialSerial;
    /**
     * 当前序号
     */
    @Column(name = "current_serial", nullable = false)
    private int currentSerial;
    /**
     * 当前编号
     */
    @Column(name = "current_number", length = 100)
    private String currentNumber;
    /**
     * 隔离码
     */
    @Column(name = "isolation_code", length = 50)
    private String isolationCode;
    /**
     * 无前导零
     */
    @Column(name = "no_leading_zero")
    private Boolean noLeadingZero = Boolean.FALSE;

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(String prefixes) {
        this.prefixes = prefixes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getInitialSerial() {
        return initialSerial;
    }

    public void setInitialSerial(int initialSerial) {
        this.initialSerial = initialSerial;
    }

    public int getCurrentSerial() {
        return currentSerial;
    }

    public void setCurrentSerial(int currentSerial) {
        this.currentSerial = currentSerial;
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(String currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getIsolationCode() {
        return isolationCode;
    }

    public void setIsolationCode(String isolationCode) {
        this.isolationCode = isolationCode;
    }

    public Boolean getNoLeadingZero() {
        return noLeadingZero;
    }

    public void setNoLeadingZero(Boolean noLeadingZero) {
        this.noLeadingZero = noLeadingZero;
    }
}
