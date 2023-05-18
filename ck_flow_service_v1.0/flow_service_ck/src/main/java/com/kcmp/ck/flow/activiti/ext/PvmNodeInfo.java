package com.kcmp.ck.flow.activiti.ext;

import org.activiti.engine.impl.pvm.PvmActivity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by kikock
 * 用于获取当前节点下一步可到达的节点信息
 * @author kikock
 * @email kikock@qq.com
 **/
public class PvmNodeInfo implements java.io.Serializable {
  private  PvmActivity currActivity;
  private PvmNodeInfo parent;
  private Set<PvmNodeInfo> children = new LinkedHashSet<PvmNodeInfo>();

    public PvmActivity getCurrActivity() {
        return currActivity;
    }

    public void setCurrActivity(PvmActivity currActivity) {
        this.currActivity = currActivity;
    }

    public PvmNodeInfo getParent() {
        return parent;
    }

    public void setParent(PvmNodeInfo parent) {
        this.parent = parent;
    }

    public Set<PvmNodeInfo> getChildren() {
        return children;
    }

    public void setChildren(Set<PvmNodeInfo> children) {
        this.children = children;
    }
}
