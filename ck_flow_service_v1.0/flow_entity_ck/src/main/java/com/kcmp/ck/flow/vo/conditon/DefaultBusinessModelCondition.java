package com.kcmp.ck.flow.vo.conditon;

import com.kcmp.ck.flow.constant.ConditionAnnotaion;
import com.kcmp.ck.flow.entity.IConditionPojo;
import com.kcmp.ck.flow.entity.DefaultBusinessModel;

/**
 * Created by kikock
 * 默认业务对象Entity定义
 * @email kikock@qq.com
 **/
public class DefaultBusinessModelCondition extends DefaultBusinessModel implements IConditionPojo {

    private Integer customeInt ;

    @Override
    @ConditionAnnotaion(name="名称")
    public String getName() {
        return super.getName();
    }

//    @ConditionAnnotaion(name="id序号",rank = -1)
//    public String getId() {
//        return super.getId();
//    }

    @Override
    @ConditionAnnotaion(name="单价",rank = 3)
    public double getUnitPrice() {
        return super.getUnitPrice();
    }

    @Override
    @ConditionAnnotaion(name="数量",rank = 4)
    public int getCount() {
        return super.getCount();
    }

    @ConditionAnnotaion(name="额外属性",rank = 2)
    public Integer getCustomeInt() {
        return customeInt;
    }

    @Override
    @ConditionAnnotaion(name="额外属性",canSee = false)
    public int getPriority() {
        return super.getPriority();
    }

    public void setCustomeInt(Integer customeInt) {
        this.customeInt = customeInt;
    }

    @Override
    public void init(){
          this.setName("name");
          this.setId("id");
          this.setWorkCaption("work caption");
          this.setCount(0);
          this.setUnitPrice(0.0);
          this.setCustomeInt(0);
    }

    @Override
    public void customLogic(){
        customeInt = this.getPriority()+10;
    }

}
