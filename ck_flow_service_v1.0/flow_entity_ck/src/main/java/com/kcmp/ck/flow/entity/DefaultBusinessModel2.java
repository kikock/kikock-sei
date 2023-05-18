package com.kcmp.ck.flow.entity;

import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.ck.flow.constant.BusinessEntityAnnotaion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by kikock
 * 采购业务实体定义
 * @email kikock@qq.com
 **/
@JsonIgnoreProperties(value={"conditionPojo"})
@Entity()
@Table(name = "default_business_model2")
@DynamicInsert
@DynamicUpdate
@BusinessEntityAnnotaion(conditionBean="com.kcmp.ck.flow.vo.conditon.DefaultBusinessModel2Condition",daoBean="defaultBusinessModel2Dao")
public class DefaultBusinessModel2 extends AbstractBusinessModel implements ITenant {
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

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

    public DefaultBusinessModel2() {
    }

    public DefaultBusinessModel2(double unitPrice, int count, double sum, String applyCaption) {
        this.unitPrice = unitPrice;
        this.count = count;
        this.sum = sum;
        this.applyCaption = applyCaption;
    }
}
