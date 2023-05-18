package com.kcmp.ck.flow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 工作池任务设置多执行人请求参数
 * @email kikock@qq.com
 **/
public class SignalPoolTaskVO implements Serializable {

    /**
     * 业务表单ID
     */
    private String businessId;

    /**
     * 工作池任务节点ID
     */
    private String poolTaskActDefId;

    /**
     * 设置工作池任务的多执行人ID集合
     */
    private List<String> userIds;

    /**
     * 其他参数集合
     */
    private Map<String, Object> map;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPoolTaskActDefId() {
        return poolTaskActDefId;
    }

    public void setPoolTaskActDefId(String poolTaskActDefId) {
        this.poolTaskActDefId = poolTaskActDefId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
