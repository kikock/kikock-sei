package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 开始流程类型vo
 * @email kikock@qq.com
 **/
public class StartFlowTypeVO implements Serializable {
    private String id;
    private String name;
    private String flowDefName;//流程定义名称
    private String flowDefKey;//流程定义Key

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowDefName() {
        return flowDefName;
    }

    public void setFlowDefName(String flowDefName) {
        this.flowDefName = flowDefName;
    }

    public String getFlowDefKey() {
        return flowDefKey;
    }

    public void setFlowDefKey(String flowDefKey) {
        this.flowDefKey = flowDefKey;
    }
}
