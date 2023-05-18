package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 排他网关
 * @email kikock@qq.com
 **/
@XmlType(name = "parallelGateway")
public class ParallelGateway extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;
}
