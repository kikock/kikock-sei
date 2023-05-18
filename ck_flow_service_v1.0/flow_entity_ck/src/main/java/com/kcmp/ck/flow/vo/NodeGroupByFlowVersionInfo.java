package com.kcmp.ck.flow.vo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kikock
 * 批量审批，通过版本分组
 * @email kikock@qq.com
 **/
public class NodeGroupByFlowVersionInfo implements Serializable{

    private String id;//流程版本id
    private String name;//流程版本名称
    private List<NodeGroupInfo> nodeGroupInfos = new ArrayList<>();
	private Boolean isSolidifyFlow; //是否固化流程


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

	public List<NodeGroupInfo> getNodeGroupInfos() {
		return nodeGroupInfos;
	}

	public void setNodeGroupInfos(List<NodeGroupInfo> nodeGroupInfos) {
		this.nodeGroupInfos = nodeGroupInfos;
	}

	public Boolean getSolidifyFlow() {
		return isSolidifyFlow;
	}

	public void setSolidifyFlow(Boolean solidifyFlow) {
		isSolidifyFlow = solidifyFlow;
	}
}
