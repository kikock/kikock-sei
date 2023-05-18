package com.kcmp.ck.flow.vo.bpmn;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by kikock
 * 任务手册
 * @email kikock@qq.com
 **/
@XmlType(name = "manualTask")
public class ManualTask extends BaseFlowNode implements Serializable {
    private static final long serialVersionUID = 1L;
}
