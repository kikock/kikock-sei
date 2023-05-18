package com.kcmp.ck.flow.util;


import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.flow.basic.vo.*;
import com.kcmp.ck.flow.common.util.Constants;
import com.kcmp.ck.flow.dao.FlowDefVersionDao;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.UserQueryParamVo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.flow.basic.vo.AppModule;
import com.kcmp.ck.flow.basic.vo.Employee;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.basic.vo.Organization;
import com.kcmp.ck.flow.basic.vo.OrganizationDimension;
import com.kcmp.ck.flow.basic.vo.Position;
import com.kcmp.ck.flow.basic.vo.PositionCategory;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.core.GenericType;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kikock
 * 流程基础工具类
 * @author kikock
 * @email kikock@qq.com
 **/
@Component
public class FlowCommonUtil implements Serializable {

    public FlowCommonUtil(){
        clearAllCache();
    }

    @Autowired
    private FlowDefVersionDao flowDefVersionDao;

    //注入缓存模板
    @Autowired(required = false)
    protected RedisTemplate<String, Object> redisTemplate;

    public  String  getErrorLogString(String name){
        return "调用" + name + "接口异常,详情请查看日志!";
    }

    public  String  getNulString(String name){
        return "调用" + name + "接口,返回为空,详情请查看日志!";
    }


    //------------------------------------------------获取执行人--------------------------------//
    /**
     * 根据用户的id获取执行人
     * 1.剔除冻结的用户  2.如果有员工信息，赋值组织机构和岗位信息
     * @param userId  用户id
     * @return 流程执行人
     */
    public Executor getBasicUserExecutor(String userId) {
        List<String> userIds = Arrays.asList((userId));
        Map<String,Object> params = new HashMap();
        params.put("userIds",userIds);
        String url = Constants.getBasicUserGetExecutorsbyUseridsUrl();
        String messageLog = "开始调用【根据用户的id获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Executor> executors ;
        try{
            executors= ApiClient.getEntityViaProxy(url,new GenericType<List<Executor>>() {},params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("【根据id获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据id获取执行人】"));
        }
        Executor executor = null;
        if(executors != null && !executors.isEmpty()){
            executor = executors.get(0);
        }
        return executor;
    }
    /**
     * 根据用户的id列表获取执行人
     * 1.剔除冻结的用户  2.如果有员工信息，赋值组织机构和岗位信息
     * @param userIds  用户ID列表
     * @return  流程执行人集合
     */
    public List<Executor> getBasicUserExecutors(List<String> userIds) {
        Map<String,Object> params = new HashMap();
        params.put("userIds",userIds);
        String url = Constants.getBasicUserGetExecutorsbyUseridsUrl();
        List<Executor> executors;
        String messageLog = "开始调用【根据用户的id列表获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        try{
            executors   = ApiClient.getEntityViaProxy(url,new GenericType<List<Executor>>() {},params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("【根据id列表获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据id列表获取执行人】"));
        }
        return executors;
    }

    /**
     * 根据岗位id列集合获取执行人
     * @param positionIds   岗位id集合
     * @param orgId         组织机构id（平台basic接口没用这个参数，项目需要的，直接在basic接口添加参数使用）
     * @return  流程执行人集合
     */
    public List<Executor> getBasicExecutorsByPositionIds(List<String> positionIds,String orgId){
        Map<String, Object> params = new HashMap();
        params.put("positionIds", positionIds);
        params.put("orgId", orgId);
        String url = Constants.getBasicPositionGetexecutorsbypositionidsUrl();
        String messageLog = "开始调用【根据岗位的id列表获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Executor> executors;
        try{
            executors = ApiClient.getEntityViaProxy(url, new GenericType<List<Executor>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("根据岗位列表获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据岗位列表获取执行人】"));
        }
        return executors;
    }

    /**
     * 根据岗位类别id列集合获取执行人
     * @param postCatIds    岗位类别id集合
     * @param orgId         组织机构id
     * @return  流程执行人集合
     */
    public List<Executor> getBasicExecutorsByPostCatIds(List<String> postCatIds,String orgId){
        Map<String, Object> params = new HashMap();
        params.put("postCatIds", postCatIds);
        params.put("orgId", orgId);
        String url = Constants.getBasicPositionGetexecutorsbyposcateidsUrl();
        String messageLog = "开始调用【根据岗位类别的id列表获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Executor> executors;
        try{
            executors = ApiClient.getEntityViaProxy(url, new GenericType<List<Executor>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("【根据岗位类别列表获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据岗位类别列表获取执行人】"));
        }
        return executors;
    }

    /**
     * 通过岗位ids、组织维度ids和组织机构id来获取执行人
     * @param positionIds   岗位ids
     * @param orgDimIds     组织维度ids
     * @param orgId         组织机构id
     * @return   流程执行人集合
     */
    public  List<Executor> getExecutorsByPositionIdsAndorgDimIds(List<String> positionIds,List<String> orgDimIds,String orgId){
        Map<String, Object> params = new HashMap();
        params.put("orgId", orgId);
        params.put("orgDimIds", orgDimIds);
        params.put("positionIds", positionIds);
        String url = Constants.getBasicPositionGetExecutorsUrl();
        String messageLog = "开始调用【根据岗位集合、组织维度集合和组织机构获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Executor> executors;
        try{
            executors = ApiClient.getEntityViaProxy(url, new GenericType<List<Executor>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("【根据岗位、组织维度和组织机构获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据岗位、组织维度和组织机构获取执行人】"));
        }
        return executors;
    }

    /**
     * 通过岗位类别ids和组织机构ids获取执行人
     * @param postCatIds    岗位类别ids
     * @param orgIds        组织机构ids
     * @return    流程执行人集合
     */
    public List<Executor> getExecutorsByPostCatIdsAndOrgs(List<String> postCatIds,List<String> orgIds){
        Map<String, Object> params = new HashMap();
        params.put("postCatIds", postCatIds);
        params.put("orgIds", orgIds);
        String url = Constants.getExecutorsByPostCatAndOrgUrl();
        String messageLog = "开始调用【根据岗位类别集合和组织机构集合获取执行人】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Executor> executors;
        try{
            executors = ApiClient.getEntityViaProxy(url, new GenericType<List<Executor>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString("【根据岗位类别和组织机构获取执行人】"), e);
        }
        if(executors.isEmpty()){
            LogUtil.error(messageLog + "返回信息为空！");
            throw  new FlowException(getNulString("【根据岗位类别和组织机构获取执行人】"));
        }
        return executors;
    }

    /**
     * 通过岗位类别ids和组织机构ids获取执行人
     * @param appModuleCode     应用模块编码
     * @param selfName          用户名称
     * @param path              接口地址
     * @param flowInvokeParams  流程参数值
     * @return
     */
    public List<Executor>  getExecutorsBySelfDef(String appModuleCode,String selfName, String path, FlowInvokeParams flowInvokeParams){
        String messageLog = "调用【自定义执行人-"+selfName+"】";
        String mes = "-应用模块：" + appModuleCode + ",接口地址：" + path + ",参数值:" + JsonUtils.toJson(flowInvokeParams);
        List<Executor> executors;
        Boolean isSuccess = true;
        try{
            executors = ApiClient.postViaProxyReturnResult(appModuleCode, path,  new GenericType<List<Executor>>() {}, flowInvokeParams);
            if(executors==null){
                messageLog += "-返回执行人为空!";
                isSuccess=false;
            }
        }catch (Exception e){
            LogUtil.error(messageLog + mes + "-【调用异常】",e);
            throw  new FlowException(messageLog + "-【调用异常】,详情请查看日志！", e);
        }
        if(!isSuccess){
            LogUtil.info(messageLog+mes);
            throw  new FlowException(messageLog + ",详情请查看日志！");
        }
        return executors;
    }





    //------------------------------------------------获取组织机构--------------------------------//
    /**
     * 获取所有组织机构树（不包含冻结）
     * @return
     */
    public  List<Organization> getBasicAllOrgs(){
        String url = Constants.getBasicOrgListallorgsUrl();
        List<Organization> result;
        String messageLog = "开始调用【获取所有组织机构树】，接口url=" + url + ",参数值为空";
        try{
            result = ApiClient.getEntityViaProxy(url, new GenericType<List<Organization>>() {}, null);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return result;
    }

    /**
     * 获取所有组织机构树（不包含冻结）
     * @return
     */
    public  List<Organization> getBasicAllOrgByPower(){
        String url = Constants.getBasicOrgListByPowerUrl();
        List<Organization> result;
        String messageLog = "开始调用【获取所有有权限的组织机构树】，接口url=" + url + ",参数值为空";
        try{
            result = ApiClient.getEntityViaProxy(url, new GenericType<List<Organization>>() {}, null);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return result;
    }

    /**
     * 获取指定节点的父组织机构列表
     * @param nodeId  指定的组织机构id
     * @return  包含自己的所有父组织机构集合
     */
    public List<Organization> getParentOrganizations(String nodeId) {
        Map<String, Object> params = new HashMap();
        params.put("includeSelf", true); //默认包含本组织机构
        params.put("nodeId", nodeId);
        String url = Constants.getBasicOrgFindparentnodesUrl();
        String messageLog = "开始调用【获取指定节点的父组织机构列表】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Organization> organizationsList;
        try{
            organizationsList = ApiClient.getEntityViaProxy(url, new GenericType<List<Organization>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return organizationsList;
    }

    //----------------------------------------------获取员工--------------------------------//
    /**
     * 根据组织机构ID获取员工集合
     * @param orgId  组织机构ID
     * @return   员工list集合
     */
    public List<Employee> getEmployeesByOrgId(String orgId){
        Map<String,Object> params = new HashMap();
        if (Objects.isNull(orgId)) {
            orgId = StringUtils.EMPTY;
        }
        params.put("organizationId",orgId);
        String url = Constants.getBasicEmployeeFindbyorganizationidUrl();
        String messageLog = "开始调用【根据组织机构ID获取员工集合】，接口url=" + url + ",参数值" + JsonUtils.toJson(params);
        List<Employee> employeeList;
        try{
            employeeList = ApiClient.getEntityViaProxy(url, new GenericType<List<Employee>>() {}, params);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return employeeList;
    }

    /**
     * 根据组织机构获取员工（可以包含子节点）
     * @param userQueryParamVo
     * @return
     */
   public PageResult<Employee> getEmployeesByOrgIdAndQueryParam(UserQueryParamVo userQueryParamVo){
       String url = Constants.getBasicEmployeeFindByUserQueryParam();
       String messageLog = "开始调用【根据组织机构ID获取员工集合】，接口url=" + url + ",参数值" + JsonUtils.toJson(userQueryParamVo);
       PageResult<Employee> employeeList;
       try{
           employeeList = ApiClient.postViaProxyReturnResult(url,new GenericType<PageResult<Employee>>() {},userQueryParamVo);
       }catch (Exception e){
           messageLog += "-调用异常：" + e.getMessage();
           LogUtil.error(messageLog,e);
           throw  new FlowException(getErrorLogString(url), e);
       }
       return employeeList;
   }


    //----------------------------------------------获取岗位--------------------------------//

    /**
     * 获取岗位
     * @param search    查询参数
     * @return
     */
    public PageResult<Position> getBasicPositionFindbypage(Search search){
        String url = Constants.getBasicPositionFindbypageUrl();
        String messageLog = "开始调用【获取所有岗位】，接口url=" + url + ",参数值" + JsonUtils.toJson(search);
        PageResult<Position> positionList;
        try{
            positionList = ApiClient.postViaProxyReturnResult(url,new GenericType<PageResult<Position>>() {},search);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return positionList;
    }


    //----------------------------------------------获取岗位类别--------------------------------//

    /**
     * 获取所有岗位类别
     * @return
     */
    public List<PositionCategory> getBasicPositioncategoryFindall(){
        String url = Constants.getBasicPositioncategoryFindallUrl();
        String messageLog = "开始调用【获取所有岗位类别】，接口url=" + url + ",参数值为空" ;
        List<PositionCategory> positionCategoryList;
        try{
            positionCategoryList  = ApiClient.getEntityViaProxy(url,new GenericType<List<PositionCategory>>() {},null);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return positionCategoryList;
    }

    //----------------------------------------------获取组织维度--------------------------------//

    /**
     * 获取所有组织维度
     * @return
     */
    public List<OrganizationDimension> getBasicOrgDimension(){
        String url = Constants.getBasicOrgDimensionUrl();
        String messageLog = "开始调用【获取所有组织维度】，接口url=" + url + ",参数值为空" ;
        List<OrganizationDimension> organizationDimensionList;
        try{
            organizationDimensionList  = ApiClient.getEntityViaProxy(url,new GenericType<List<OrganizationDimension>>() {},null);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }
        return organizationDimensionList;
    }

    /**
     * 获取当前用户拥有权限的应用模块
     * @return  拥有权限的应用模块集合
     */
    public List<AppModule> getBasicTenantAppModule(){
        String url = Constants.getBasicTenantAppModuleUrl();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date startDate = new Date();
        String messageLog = sim.format(startDate) + "开始调用【获取当前用户拥有权限的应用模块】，接口url=" + url + ",不需要参数值";
        List<AppModule> appModuleList;
        try{
            appModuleList = ApiClient.getEntityViaProxy(url, new GenericType<List<AppModule>>() {}, null);
            Date endDate = new Date();
            messageLog += "," + sim.format(endDate);
        }catch (Exception e){
            messageLog += "-调用异常：" + e.getMessage();
            LogUtil.error(messageLog,e);
            throw  new FlowException(getErrorLogString(url), e);
        }finally {
            LogUtil.bizLog(messageLog);
        }
       return  appModuleList;
    }

    /**
     * 获取bmpn节点基类
     * @param flowDefVersion    流程定义版本数据
     * @return
     */
//    @Cacheable(value = "FLowGetDefinitionJSON", key = "'FLowGetDefinitionJSON_' + #flowDefVersion.id")
    public Definition flowDefinition(FlowDefVersion flowDefVersion ){
        String defObjStr = flowDefVersion.getDefJson();
        JSONObject defObj = JSONObject.fromObject(defObjStr);
        Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
        return definition;
    }

    /**
     * 获取流程定义版本
     * @param versionId 主键id
     * @return
     */
//    @Cacheable(value = "FLowGetLastFlowDefVersion", key = "'FLowGetLastFlowDefVersion_' + #versionId")
    public FlowDefVersion getLastFlowDefVersion(String versionId ){
        FlowDefVersion flowDefVersion = flowDefVersionDao.findOne(versionId);
        return flowDefVersion;
    }

    /**
     * 清理所有缓存数据
     */
    private void clearAllCache(){
        clearCache( "FLowGetDefinitionJSON_*");
        clearCache( "FLowGetBasicExecutor_*");
        clearCache( "FLowGetBasicExecutors*");
        clearCache( "FLowGetLastFlowDefVersion_*");
        clearCache( "FLowOrgParentCodes_*");
    }

    /**
     * 根据key清理缓存
     * @param pattern   缓存key
     */
    private void clearCache(String pattern){
//        String pattern = "FLowGetLastFlowDefVersion_*";
        if(redisTemplate != null){
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()){
                redisTemplate.delete(keys);
            }
        }
    }
}
