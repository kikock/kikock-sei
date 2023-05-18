package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 保存流程vo对象
 * @email kikock@qq.com
 **/
public class SaveEntityVo implements Serializable {

    /**
     * 流程设计json
     */
    private String def;

    /**
     * 是否发布
     */
    private Boolean deploy;


    public Boolean getDeploy() {
        return deploy;
    }

    public void setDeploy(Boolean deploy) {
        this.deploy = deploy;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
