package com.kcmp.ck.flow.util;

import java.util.Map;

import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.JsonUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;

/**
 * Created by kikock
 * 条件表达式基础工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class ConditionUtil {
	  public static Object uelResult(String uelExpressStr,Map<String,Object> map) throws NoSuchMethodException, SecurityException{
       ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
       //创建上下文对象context
       ELContext context = new SimpleContext();
       ValueExpression e = factory.createValueExpression(context, uelExpressStr, Object.class);
       for(Map.Entry<String,Object> entry : map.entrySet()){
           factory.createValueExpression(context, "${"+entry.getKey()+"}", entry.getValue().getClass()).setValue(context, entry.getValue());
       }
       // get value for our expression
       return e.getValue(context);
   }
    public static Boolean groovyTest(String condition,Map<String, Object> map){

        Boolean result = null;

        Binding bind = new Binding();
        for (Map.Entry<String, Object> pv : map.entrySet()) {
            bind.setVariable(pv.getKey(), pv.getValue());
        }
        GroovyShell shell = new GroovyShell(bind);
        try {
            Object obj = shell.evaluate(condition);
            if(obj instanceof  Boolean){
                result = (Boolean) obj;
            }
        } catch (Exception e) {
            result = null;
            LogUtil.error("验证表达式失败！表达式：【"+condition+"】,带入参数：【"+ JsonUtils.toJson(map)+"】",e);
            e.printStackTrace();
        }
        return result;
    }
}
