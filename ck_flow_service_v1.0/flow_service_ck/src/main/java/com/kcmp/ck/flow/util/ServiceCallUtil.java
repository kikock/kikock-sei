package com.kcmp.ck.flow.util;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.dao.FlowServiceUrlDao;
import com.kcmp.ck.flow.entity.AppModule;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.core.GenericType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 服务调用
 * @author kikock
 * @email kikock@qq.com
 **/
public class ServiceCallUtil {

    /**
     * 服务调用
     * @param serviceUrlId  服务调用路径
     * @param businessId    业务id
     * @param args          参数
     * @return
     */
    public static FlowOperateResult callService(String serviceUrlId, String businessId, String... args){
        FlowOperateResult result = null;
        if(!StringUtils.isEmpty(serviceUrlId)){
            ApplicationContext applicationContext = ContextUtil.getApplicationContext();
            FlowServiceUrlDao flowServiceUrlDao = (FlowServiceUrlDao)applicationContext.getBean(Constants.FLOW_SERVICE_URL_DAO);
            FlowServiceUrl flowServiceUrl = flowServiceUrlDao.findOne(serviceUrlId);
            if(flowServiceUrl != null){
                String  clientUrl = flowServiceUrl.getUrl();
                AppModule appModule = flowServiceUrl.getBusinessModel().getAppModule();

                Map<String,String> paramMap = new HashMap<String,String>();
                FlowInvokeParams params = new FlowInvokeParams();
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(args[0])){
                    try {
                        JSONObject jsonObject = JSONObject.fromObject(args[0]);
                        if(jsonObject.has(Constants.APPROVED)){
                            String approved = jsonObject.get(Constants.APPROVED)+"";
                            if (StringUtils.isNotEmpty(approved) && !Constants.NULL_S.equalsIgnoreCase(approved)) {
                                params.setAgree(Boolean.parseBoolean(approved));
                            }
                        }
                        if(jsonObject.has(Constants.APPROVE_RESULT)){
                            String approveResult = jsonObject.get(Constants.APPROVE_RESULT)+"";
                            if (StringUtils.isNotEmpty(approveResult) && !Constants.NULL_S.equalsIgnoreCase(approveResult)) {
                                params.setFinalAgree(Boolean.parseBoolean(approveResult));
                            }
                        }
                        if(jsonObject.has(Constants.RECEIVE_TASK_ACT_DEF_ID)){
                            String receiveTaskActDefId = jsonObject.get(Constants.RECEIVE_TASK_ACT_DEF_ID)+"";
                            if (StringUtils.isNotEmpty(receiveTaskActDefId) && !Constants.NULL_S.equalsIgnoreCase(receiveTaskActDefId)) {
                                params.setTaskActDefId(receiveTaskActDefId);
                            }
                        }
//                        tempV.put(Constants.POOL_TASK_ACT_DEF_ID,actTaskDefKey);
                        if(jsonObject.has(Constants.POOL_TASK_ACT_DEF_ID)){
                            String poolTaskActDefId = jsonObject.get(Constants.POOL_TASK_ACT_DEF_ID)+"";
                            if (StringUtils.isNotEmpty(poolTaskActDefId) && !Constants.NULL_S.equalsIgnoreCase(poolTaskActDefId)) {
                                params.setTaskActDefId(poolTaskActDefId);
                            }
                        }
                        if(jsonObject.has(Constants.POOL_TASK_CODE)){
                            String poolTaskCode = jsonObject.get(Constants.POOL_TASK_CODE)+"";
                            if (StringUtils.isNotEmpty(poolTaskCode) && !Constants.NULL_S.equalsIgnoreCase(poolTaskCode)) {
                                params.setPoolTaskCode(poolTaskCode);
                            }
                        }
                        if(jsonObject.has(Constants.REJECT)){
                            int reject = jsonObject.getInt(Constants.REJECT);
                            if(reject==1){
                                params.setReject(true);//是否被驳回
                            }
                        }
                        if(jsonObject.has(Constants.CALL_ACTIVITY_SON_PATHS)){
                            List<String> callActivitySonPaths = jsonObject.getJSONArray(Constants.CALL_ACTIVITY_SON_PATHS);
                            params.setCallActivitySonPaths(callActivitySonPaths);
                        }
                        if(jsonObject.has(Constants.OPINION)){
                            String  opinion = jsonObject.get(Constants.OPINION)+"";
                            paramMap.put(Constants.OPINION,opinion);
                        }
                        if(jsonObject.has("selectedNodesUserMap")){
                            JSONObject itemJSONObj = jsonObject.getJSONObject("selectedNodesUserMap");
                            Map<String, List<String>> itemMap = (Map<String, List<String>>) JSONObject.toBean(itemJSONObj, Map.class);
                            params.setNextNodeUserInfo(itemMap);
                        }
                        if(jsonObject.has("currentNodeCode")){
                            String  currentNodeCode = jsonObject.get("currentNodeCode")+"";
                            paramMap.put("nodeCode",currentNodeCode);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        throw e;
                    }
                }
                params.setId(businessId);
                params.setParams(paramMap);
                String apiBaseAddressConfig = appModule.getApiBaseAddress();
                String clientApiBaseUrl =  ContextUtil.getGlobalProperty(apiBaseAddressConfig);
                String url = clientApiBaseUrl + "/"+clientUrl;
                Date startDate = new Date();
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String msg = sim.format(startDate)+"事件【"+flowServiceUrl.getName()+"】";
                String urlAndData = "-请求地址：" + url + "，参数：" + JsonUtils.toJson(params);
                try {
                    result = ApiClient.postViaProxyReturnResult(url, new GenericType<FlowOperateResult>() {}, params);
                    Date endDate = new Date();
                    if(result==null){
                        result = new FlowOperateResult(false,msg+"返回信息为空！");
                        LogUtil.bizLog(msg + "返回参数为空!" + urlAndData);
                    }else if(!result.isSuccess()){
                        LogUtil.bizLog(msg + "返回信息：【" + result.toString() + "】" + urlAndData);
                        result.setMessage(msg + "返回信息：【" + result.getMessage() + "】");
                    }else{
                        LogUtil.bizLog(msg + urlAndData + "返回信息时间：" + sim.format(endDate));
                    }
                }catch (Exception e){
                    LogUtil.error(msg + "内部报错!" + urlAndData,e);
                    throw new FlowException(msg + "内部报错，详情请查看日志！");
                }
            }else {
                throw new FlowException("事件信息不能找到，可能已经被删除，serviceId=" + serviceUrlId);
            }
        }
        return result;
    }
}
