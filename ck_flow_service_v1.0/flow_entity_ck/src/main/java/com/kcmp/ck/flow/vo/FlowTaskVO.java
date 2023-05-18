package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 任务vo对象
 * @email kikock@qq.com
 **/
public class FlowTaskVO implements Serializable {
    /**
     * 任务id
     */
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 工作页面
     */
    private String workPageUrl;

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

    public String getWorkPageUrl() {
        return workPageUrl;
    }

    public void setWorkPageUrl(String workPageUrl) {
        this.workPageUrl = workPageUrl;
    }
}
