package com.kcmp.ck.flow.vo.conditon;

import com.kcmp.ck.flow.constant.ConditionAnnotaion;
import com.kcmp.ck.flow.entity.IConditionPojo;
import com.kcmp.ck.flow.entity.DefaultBusinessModel3;

/**
 * Created by kikock
 * 默认业务对象Entity定义
 * @email kikock@qq.com
 **/
public class DefaultBusinessModel3Condition extends DefaultBusinessModel3 implements IConditionPojo {

    private Integer customeInt ;

    @Override
    @ConditionAnnotaion(name="名称")
    public String getName() {
        return super.getName();
    }

    @Override
    @ConditionAnnotaion(name="id序号",rank = -1)
    public String getId() {
        return super.getId();
    }

    @Override
    @ConditionAnnotaion(name="工作说明",rank = 1)
    public String getWorkCaption(){return super.getWorkCaption();}

    @Override
    @ConditionAnnotaion(name="销售单价",rank = 3)
    public double getUnitPrice() {
        return super.getUnitPrice();
    }

    @Override
    @ConditionAnnotaion(name="销售数量",rank = 4)
    public int getCount() {
        return super.getCount();
    }

    @ConditionAnnotaion(name="额外属性",rank = 2)
    public Integer getCustomeInt() {
        return customeInt;
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
