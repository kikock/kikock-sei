package com.kcmp.ck.flow.entity;

import java.io.Serializable;

/**
 * Created by kikock
 * 条件Pojo接口
 * @email kikock@qq.com
 **/
public interface IConditionPojo extends Serializable{

    /**
     * 条件表达式初始化，提供给表达式做初始化验证，
     * 结合具体业务实现
     */
    public  void init();

    /**
     * 自定义逻辑方法，
     * 场景：应用于条件表达式POJO的额外定义属性值初始化
     */
    public void customLogic();

}
