package com.kcmp.ck.flow.vo.bpmn;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * bmpn连线类
 * @email kikock@qq.com
 **/
@XmlType(name = "sequenceFlow")
public class SequenceFlow extends BaseNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 源节点
     */
    @XmlAttribute
    private String sourceRef;
    /**
     * 目标节点
     */
    @XmlAttribute
    private String targetRef;

    @XmlAttribute
    private String name;

    @XmlElement(name = "documentation")
    private Documentation documentation;

    /**
     * UEL
     */
    @XmlElement(name = "conditionExpression")
    private ConditionExpression conditionExpression;

    public SequenceFlow(String id, String sourceRef, String targetRef, String uel) {
        this.id = id;
        this.sourceRef = sourceRef;
        this.targetRef = targetRef;
        if (StringUtils.isNotBlank(uel)) {
            JSONObject uelObject = JSONObject.fromObject(uel);
            String uelValue = (String) uelObject.get("groovyUel");
            this.name = (String) uelObject.get("name");
            if(StringUtils.isNotEmpty(uelValue)){
                this.conditionExpression = new ConditionExpression(uelValue);
            }
            if(uelObject.has("code")){
                String code = (String) uelObject.get("code");
                if(StringUtils.isNotEmpty(code)){
                    documentation = new Documentation(code);
                }
            }

        }

    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public ConditionExpression getConditionExpression() {
        return conditionExpression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConditionExpression(ConditionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public SequenceFlow(){};

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }
}
