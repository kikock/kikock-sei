package com.kcmp.ck.flow.util;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.GenericType;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kikock
 * 条件表达式工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class ExpressionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExpressionUtil.class);

    public static AppModule getAppModule(BusinessModel businessModel){
        AppModule appModule = businessModel.getAppModule();
        return appModule;
    }

    public static String  getErrorLogString(String url){
        return  "【调用接口异常：" + url + ",详情请查看日志】";
    }

    /**
     * 获取条件属性说明（键值对）
     * @param businessModel 业务模型
     * @return
     */
    public  static Map<String,String>  getPropertiesDecMap(BusinessModel businessModel){
        String businessModelCode = businessModel.getClassName();
        String apiBaseAddressConfig = getAppModule(businessModel).getApiBaseAddress();
        String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
        String clientApiUrl = clientApiBaseUrl + businessModel.getConditonProperties();
        Map<String,Object> params = new HashMap();
        params.put(Constants.BUSINESS_MODEL_CODE,businessModelCode);
        params.put(Constants.ALL,false);
        String messageLog = "开始调用【条件属性说明服务地址】，接口url=" + clientApiUrl + ",参数值" + JsonUtils.toJson(params);
        Map<String,String> result;
        try {
            result = ApiClient.getEntityViaProxy(clientApiUrl, new GenericType<Map<String, String>>() {}, params);
            messageLog+=",【result=" + (null == result ? null : JsonUtils.toJson(result)) + "】";
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            throw  new FlowException(getErrorLogString(clientApiUrl),e);
        }finally {
            LogUtil.bizLog(messageLog);
        }
        return result;
    }

    /**
     * 获取条件属性值（键值对）
     * @param businessModel 业务模型
     * @param  businessId 业务ID
     * @return
     */
    public  static Map<String,Object>  getPropertiesValuesMap(BusinessModel businessModel, String businessId,Boolean all) {
        String businessModelCode = businessModel.getClassName();
        String apiBaseAddressConfig = getAppModule(businessModel).getApiBaseAddress();
        String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
        String clientApiUrl = clientApiBaseUrl + businessModel.getConditonPValue();
        Map<String,Object> params = new HashMap();
        params.put(Constants.BUSINESS_MODEL_CODE,businessModelCode);
        params.put(Constants.ID,businessId);
        params.put(Constants.ALL,all);
        Date startDate = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String messageLog = sim.format(startDate) + "开始调用【条件属性值服务地址】，接口url=" + clientApiUrl + ",参数值" + JsonUtils.toJson(params);
        Map<String,Object> result;
        try {
            result =  ApiClient.getEntityViaProxy(clientApiUrl,new GenericType<Map<String,Object> >() {},params);
            Date endDate = new Date();
            messageLog += "," + sim.format(endDate) + "【result=" + (null == result ? null : JsonUtils.toJson(result)) + "】";
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            throw  new FlowException(getErrorLogString(clientApiUrl),e);
        }finally {
            LogUtil.bizLog(messageLog);
        }
        return result;
    }

    /**
     * 获取条件属性初始值（键值对）
     * @param businessModel 业务实体模型
     * @return
     */
    public static Map<String,Object>  getPropertiesInitialValuesMap(BusinessModel businessModel){
        String apiBaseAddressConfig = getAppModule(businessModel).getApiBaseAddress();
        String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
        String clientApiUrl = clientApiBaseUrl + businessModel.getConditonPSValue();
        Map<String,Object> params = new HashMap();
        params.put(Constants.BUSINESS_MODEL_CODE,businessModel.getClassName());
        String messageLog = "开始调用【条件属性初始值服务地址】，接口url=" + clientApiUrl + ",参数值" + JsonUtils.toJson(params);
        Map<String,Object> result;
        try {
            result =  ApiClient.getEntityViaProxy(clientApiUrl,new GenericType<Map<String,Object> >() {},params);
            messageLog += ",【result=" + (null == result ? null : JsonUtils.toJson(result)) + "】";
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            throw  new FlowException(getErrorLogString(clientApiUrl),e);
        }finally {
            LogUtil.bizLog(messageLog);
        }
        return result;
    }

    /**
     * 检证表达式语法是否合法
     * @param  businessModel 业务实体
     * @param expression 表达式
     * @return
     */
    public static Boolean validate(BusinessModel businessModel,String expression) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //获取条件属性初始值
        Map<String,Object> pvs =  getPropertiesInitialValuesMap(businessModel);
        Boolean result = ConditionUtil.groovyTest(expression,pvs);
        return result;
    }

    /**
     * 直接获取表达式验证结果
     * @param  businessModel 业务实体
     * @param expression
     * @param businessId  业务ID
     * @return
     */
    public static boolean result(BusinessModel businessModel,String businessId,String expression){
        //获取条件属性值
        Map<String,Object> pvs =  getPropertiesValuesMap(businessModel,businessId,true);
        boolean result =  ConditionUtil.groovyTest(expression,pvs);
        return result;
    }

    /**
     * 重置单据状态
     * @param  businessModel 业务实体
     * @param status
     * @param businessId  业务ID
     * @return
     */
    public static boolean resetState(BusinessModel businessModel, String businessId, FlowStatus status){
        boolean result;
        String businessModelCode = businessModel.getClassName();
        String apiBaseAddressConfig = getAppModule(businessModel).getApiBaseAddress();
        String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
        String clientApiUrl = clientApiBaseUrl + businessModel.getConditonStatusRest();
        Map<String,Object> params = new HashMap();
        params.put(Constants.BUSINESS_MODEL_CODE,businessModelCode);
        params.put(Constants.ID,businessId);
        params.put(Constants.STATUS,status);
        String messageLog = "开始调用【重置单据状态】接口，接口url=" + clientApiUrl + ",参数值" + JsonUtils.toJson(params);
        try {
            result = ApiClient.postViaProxyReturnResult(clientApiUrl,new GenericType<Boolean>() {},params);
            messageLog += ",【result=" + result + "】";
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            throw  new FlowException(getErrorLogString(clientApiUrl),e);
        }finally {
            LogUtil.bizLog(messageLog);
        }
        return result;
    }

    /**
     * 模拟异步,上传调用日志
     * @param message
     */
    static void asyncUploadLog(String message){
        new Thread(new Runnable() {//模拟异步,上传调用日志
            @Override
            public void run() {
                LogUtil.bizLog(message);
            }
        }).start();
    }
}
