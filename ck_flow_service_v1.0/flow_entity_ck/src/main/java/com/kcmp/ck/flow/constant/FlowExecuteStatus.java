package com.kcmp.ck.flow.constant;

import java.io.Serializable;

/**
 * Created by kikock
 * 流程节点执行状态
 * @email kikock@qq.com
 **/
public enum FlowExecuteStatus implements Serializable {

    /**
     * 提交
     */
    SUBMIT("submit", "提交"),
    /**
     * 同意
     */
    AGREE("agree", "同意"),
    /**
     * 不同意
     */
    DISAGREE("disagree", "不同意"),
    /**
     * 转办
     */
    TURNTODO("turntodo", "转办"),
    /**
     * 委托
     */
    ENTRUST("entrust", "委托"),
    /**
     * 撤回
     */
    RECALL("recall", "撤回"),
    /**
     * 驳回
     */
    REJECT("reject","驳回"),
    /**
     * 终止
     */
    END("end","终止"),
    /**
     * 自动执行
     */
    AUTO("auto", "自动执行");


    private String code;

    private String name;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    FlowExecuteStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code值得到中文字段
     *
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (FlowExecuteStatus flowCode : FlowExecuteStatus.values()) {
            if (flowCode.equals(code)) {
                return flowCode.getName();
            }
        }
        //如果匹配不上，默认返回自动执行
        return FlowExecuteStatus.AUTO.getName();
    }

}
