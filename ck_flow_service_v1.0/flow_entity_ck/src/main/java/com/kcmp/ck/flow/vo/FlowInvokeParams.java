package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 服务、事件输入参数vo
 * @email kikock@qq.com
 **/
public class FlowInvokeParams implements Serializable{
    private String id;//业务单据id
    private Map<String,String> params;//其他参数
    private Boolean isAgree;  //是否同意
    private Boolean isFinalAgree;//是否最终同意，针对会签
    private String taskActDefId;//针对接收任务\用户池任务等，传递任务key
    private String poolTaskCode;//用户池任务，传递任务用户池代码
    private Boolean reject=false;//是否是被驳回的任务,用于业务接口判断任务驳回时是否需要调用业务处理逻辑
    private List<String> callActivitySonPaths;//调用子流程路径
    private String orgId;//组织机构id
    private List<String> positionIds;//岗位代码集合（参数自选）
    private List<String> positionTypeIds;//岗位类别集合（参数自选）
    private List<String> organizationIds;//组织机构集合（参数自选）
    private List<String> orgDimensionCodes;//组织维度代码集合（参数自选）
    private String jsonParam;//其他json格式参数
    private Map<String,List<String>> nextNodeUserInfo;//下一步操作用户信息,key为节点代码（如没有配置，指定为默认id）,下一步选择的用户id集合
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }

    public Boolean getFinalAgree() {
        return isFinalAgree;
    }

    public void setFinalAgree(Boolean finalAgree) {
        isFinalAgree = finalAgree;
    }

    public String getTaskActDefId() {
        return taskActDefId;
    }

    public void setTaskActDefId(String taskActDefId) {
        this.taskActDefId = taskActDefId;
    }

    public Boolean getReject() {
        return reject;
    }

    public void setReject(Boolean reject) {
        this.reject = reject;
    }

    public List<String> getCallActivitySonPaths() {
        return callActivitySonPaths;
    }

    public void setCallActivitySonPaths(List<String> callActivitySonPaths) {
        this.callActivitySonPaths = callActivitySonPaths;
    }

    public String getPoolTaskCode() {
        return poolTaskCode;
    }

    public void setPoolTaskCode(String poolTaskCode) {
        this.poolTaskCode = poolTaskCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<String> getOrgDimensionCodes() {
        return orgDimensionCodes;
    }

    public void setOrgDimensionCodes(List<String> orgDimensionCodes) {
        this.orgDimensionCodes = orgDimensionCodes;
    }

    public List<String> getPositionIds() {
        return positionIds;
    }

    public void setPositionIds(List<String> positionIds) {
        this.positionIds = positionIds;
    }

    public String getJsonParam() {
        return jsonParam;
    }

    public void setJsonParam(String jsonParam) {
        this.jsonParam = jsonParam;
    }

    public Map<String, List<String>> getNextNodeUserInfo() {
        return nextNodeUserInfo;
    }

    public void setNextNodeUserInfo(Map<String, List<String>> nextNodeUserInfo) {
        this.nextNodeUserInfo = nextNodeUserInfo;
    }

    public List<String> getPositionTypeIds() {
        return positionTypeIds;
    }

    public void setPositionTypeIds(List<String> positionTypeIds) {
        this.positionTypeIds = positionTypeIds;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }
}
