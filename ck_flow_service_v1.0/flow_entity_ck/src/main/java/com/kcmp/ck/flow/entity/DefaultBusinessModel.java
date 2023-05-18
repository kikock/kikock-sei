package com.kcmp.ck.flow.entity;

import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.ck.annotation.Remark;
import com.kcmp.ck.flow.constant.BusinessEntityAnnotaion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by kikock
 * 默认业务实体定义
 * @email kikock@qq.com
 **/
@JsonIgnoreProperties(value={"conditionPojo"})
@Access(AccessType.FIELD)
@Entity()
@Table(name = "default_business_model")
@DynamicInsert
@DynamicUpdate
@BusinessEntityAnnotaion(conditionBean="com.kcmp.ck.flow.vo.conditon.DefaultBusinessModelCondition",daoBean="defaultBusinessModelDao")
public class DefaultBusinessModel extends AbstractBusinessModel implements ITenant {
    /**
     * 单价
     */
    @Column(name = "unit_price")
    private double unitPrice=0;

    /**
     * 数量
     */
    private int count=0;

    /**
     * 金额
     */
    private double sum = 0;

    /**
     * 申请说明
     */
    @Column(name = "apply_caption")
    private  String applyCaption;

    @Remark(value = "单价",rank = 11)
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Remark(value = "数量",rank = 10)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSum() {
        return sum = unitPrice* count;
    }

    public void setSum(double sum) {
        this.sum =  unitPrice* count;
    }

    public String getApplyCaption() {
        return applyCaption;
    }

    public void setApplyCaption(String applyCaption) {
        this.applyCaption = applyCaption;
    }

    public DefaultBusinessModel() {
    }

    public DefaultBusinessModel(double unitPrice, int count, double sum, String applyCaption) {
        this.unitPrice = unitPrice;
        this.count = count;
        this.sum = sum;
        this.applyCaption = applyCaption;
    }

    @Transient
    private DefaultBusinessModel2 defaultBusinessModel2;

    @Transient
    private DefaultBusinessModel3 defaultBusinessModel3;

    @Remark("采购")
    public DefaultBusinessModel2 getDefaultBusinessModel2() {
        return defaultBusinessModel2;
    }

    public void setDefaultBusinessModel2(DefaultBusinessModel2 defaultBusinessModel2) {
        this.defaultBusinessModel2 = defaultBusinessModel2;
    }

    @Remark("销售")
    public DefaultBusinessModel3 getDefaultBusinessModel3() {
        return defaultBusinessModel3;
    }

    public void setDefaultBusinessModel3(DefaultBusinessModel3 defaultBusinessModel3) {
        this.defaultBusinessModel3 = defaultBusinessModel3;
    }
}
