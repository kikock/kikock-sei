package com.kcmp.ck.flow.util;

/**
 * Created by kikock
 * 流程建模工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class BpmnUtil {
    public static String getCurrentNodeParamName(net.sf.json.JSONObject currentNode ){
        String result = null;
        String flowTaskType =  currentNode.get("nodeType")+"";
        String id =  currentNode.get("id")+"";
        if("Normal".equalsIgnoreCase(flowTaskType)){
            result = id+"_Normal";
        }else if("SingleSign".equalsIgnoreCase(flowTaskType)){
            result = id+"_SingleSign";
        }else if("CounterSign".equalsIgnoreCase(flowTaskType)|| "ParallelTask".equalsIgnoreCase(flowTaskType)
                || "SerialTask".equals(flowTaskType)){
            result = id+"_CounterSign";
        }else if("Approve".equalsIgnoreCase(flowTaskType)){
            result = id+"_Approve";
        }
        return result;
    }
}
