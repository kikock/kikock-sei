package com.kcmp.ck.flow.vo.bpmn;

import net.sf.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * Created by kikock
 * bmpn节点基类
 * @email kikock@qq.com
 **/
@XmlRootElement(name = "definitions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Definition implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private String xmlns = "http://www.omg.org/spec/BPMN/20100524/MODEL";

//    @XmlAttribute
//    private String xs = "http://www.w3.org/2001/XMLSchema";

    @XmlAttribute
    private String expressionLanguage = "http://www.w3.org/1999/XPath";

    @XmlAttribute
    private String typeLanguage = "http://www.w3.org/2001/XMLSchema";

    @XmlAttribute
    private String targetNamespace = "bpmn";

    @XmlElement
    private Process process;

    /**
     * 前端设计json文本
     */
    @XmlTransient
    private String defJson;

    /**
     * 流程类型Id
     */
    @XmlTransient
    private String flowTypeId;
    /**
     * 流程类型Name
     */
    @XmlTransient
    private String flowTypeName;

    /**
     * 组织机构ID
     */
    @XmlTransient
    private String orgId;

    /**
     * 组织机构ID
     */
    @XmlTransient
    private String orgCode;

    /**
     * 流程定义ID
     */
    @XmlTransient
    private String id;

    /**
     * 优先级
     */
    @XmlTransient
    private Integer priority;

    /**
     * 流程版本
     */
    @XmlTransient
    private int versionCode;

    /**
     * 就否允许作为子流程引用
     */
    @XmlTransient
    private Boolean subProcess;

    /**
     * 是否为固化流程
     */
    @XmlTransient
    private Boolean solidifyFlow;


    public Boolean getSolidifyFlow() {
        return solidifyFlow;
    }

    public void setSolidifyFlow(Boolean solidifyFlow) {
        this.solidifyFlow = solidifyFlow;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getDefJson() {
        return defJson;
    }

    public void setDefJson(String defJson) {
        this.defJson = defJson;
    }

    public String getFlowTypeId() {
        return flowTypeId;
    }

    public void setFlowTypeId(String flowTypeId) {
        this.flowTypeId = flowTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getFlowTypeName() {
        return flowTypeName;
    }

    public void setFlowTypeName(String flowTypeName) {
        this.flowTypeName = flowTypeName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public static void main(String[] args) throws JAXBException {
        Definition df = new Definition();
        Process process = new Process();
        process.setName("测试");
        process.setId("1122");
      //  process.setStartUEL("{23sfsfsf}");
        df.setDefJson("sdfsdfs");
        JSONObject nodes = JSONObject.fromObject("{\"StartEvent_0\":{\"type\":\"StartEvent\",\"x\":188,\"y\":136,\"id\":\"StartEvent_0\",\"target\":[{\"targetId\":\"UserTask_2\",\"uel\":\"\"}],\"name\":\"开始\",\"nodeConfig\":{}},\"EndEvent_1\":{\"type\":\"EndEvent\",\"x\":704,\"y\":232,\"id\":\"EndEvent_1\",\"target\":[],\"name\":\"结束\",\"nodeConfig\":{}},\"UserTask_2\":{\"type\":\"UserTask\",\"x\":438,\"y\":185,\"id\":\"UserTask_2\",\"target\":[{\"targetId\":\"EndEvent_1\",\"uel\":\"\"}],\"name\":\"普通任务\",\"nodeConfig\":{\"normal\":{\"name\":\"普通任务\",\"executeTime\":\"44\",\"workPageName\":\"默认审批页面\",\"workPageUrl\":\"http://localhost:8081/lookApproveBill/show\",\"allowTerminate\":true,\"allowPreUndo\":true,\"allowReject\":true},\"executor\":{\"userType\":\"AnyOne\"},\"event\":{\"beforeExcuteService\":\"\",\"afterExcuteService\":\"\",\"afterExcuteServiceId\":\"\"},\"notify\":null}}}");
        process.setNodes(nodes);
        df.setProcess(process);
        JAXBContext context = JAXBContext.newInstance(df.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(df, writer);
        System.out.println(writer.toString());
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getSubProcess() {
        return subProcess;
    }

    public void setSubProcess(Boolean subProcess) {
        this.subProcess = subProcess;
    }
}
