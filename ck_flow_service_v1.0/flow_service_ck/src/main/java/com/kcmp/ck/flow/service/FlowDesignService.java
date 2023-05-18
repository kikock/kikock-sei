package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dto.PageInfo;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.core.ck.dto.SearchOrder;
import com.kcmp.ck.flow.api.IFlowDesignService;
import com.kcmp.ck.flow.basic.vo.OrganizationDimension;
import com.kcmp.ck.flow.basic.vo.Position;
import com.kcmp.ck.flow.basic.vo.PositionCategory;
import com.kcmp.ck.flow.entity.FlowDefVersion;
import com.kcmp.ck.flow.entity.FlowInstance;
import com.kcmp.ck.flow.entity.FlowServiceUrl;
import com.kcmp.ck.flow.entity.WorkPageUrl;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.util.FlowException;
import com.kcmp.ck.flow.vo.SaveEntityVo;
import com.kcmp.ck.flow.vo.SearchVo;
import com.kcmp.ck.flow.vo.bpmn.Definition;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by kikock
 * 流程设计器默认服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class FlowDesignService implements IFlowDesignService {

    @Autowired
    private FlowDefinationService flowDefinationService;

    @Autowired
    private FlowDefVersionService flowDefVersionService;

    @Autowired
    private FlowServiceUrlService flowServiceUrlService;

    @Autowired
    private WorkPageUrlService workPageUrlService;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Autowired
    private FlowSolidifyExecutorService flowSolidifyExecutorService;

    /**
     * 获取流程设计
     * @param id
     * @param versionCode
     * @param businessModelCode
     * @param businessId
     * @return
     */
    @Override
    public ResponseData getEntity(String id, Integer versionCode, String businessModelCode, String businessId) {
        if (StringUtils.isEmpty(id)) {
            return ResponseData.operationFailure("参数id为空");
        }
        if (versionCode == null) {
            return ResponseData.operationFailure("参数versionCode为空");
        }
        try {
            FlowDefVersion data = flowDefinationService.getFlowDefVersion(id, versionCode, businessModelCode, businessId);
            return ResponseData.operationSuccessWithData(data);
        } catch (Exception e) {
            LogUtil.error("获取流程定义出错!", e);
            throw new FlowException("获取流程定义出错,详情请查看日志！");
        }
    }

    /**
     * 流程设计保存(发布)
     * @param entityVo
     * @return
     * @throws JAXBException
     * @throws UnsupportedEncodingException
     * @throws CloneNotSupportedException
     */
    @Override
    public ResponseData save(SaveEntityVo entityVo) throws JAXBException, UnsupportedEncodingException, CloneNotSupportedException {
        String def = entityVo.getDef();
        Boolean deploy = entityVo.getDeploy();
        ResponseData responseData = new ResponseData();
        if (StringUtils.isNotEmpty(def) || deploy != null) {
            JSONObject defObj = JSONObject.fromObject(def);
            Definition definition = (Definition) JSONObject.toBean(defObj, Definition.class);
            String id = definition.getProcess().getId();
            String reg = "^[a-zA-Z][A-Za-z0-9]{5,79}$";
            if (!id.matches(reg)) {
                return ResponseData.operationFailure("流程代码以字母开头，允许数字或字母，且长度在6-80之间！");
            }
            definition.setDefJson(def);
            if (!deploy) {
                OperateResultWithData<FlowDefVersion> result = flowDefVersionService.save(definition);
                responseData.setSuccess(result.successful());
                responseData.setMessage(result.getMessage());
                responseData.setData(result.getData());
            } else {
                OperateResultWithData<FlowDefVersion> result = flowDefVersionService.save(definition);
                if (result.successful()) {
                    flowDefinationService.deployById(result.getData().getFlowDefination().getId());
                }
                responseData.setSuccess(result.successful());
                responseData.setMessage(result.getMessage());
                responseData.setData(result.getData());
            }
            return responseData;
        } else {
            return ResponseData.operationFailure("参数不能为空！");
        }
    }

    /**
     * 查询流程服务地址
     * @param busModelId
     * @return
     * @throws ParseException
     */
    @Override
    public ResponseData listAllServiceUrl(String busModelId) throws ParseException {
        if (StringUtils.isEmpty(busModelId)) {
            return ResponseData.operationFailure("参数不能为空！");
        }
        List<FlowServiceUrl> flowServiceUrlPageResult = flowServiceUrlService.findByBusinessModelId(busModelId);
        return ResponseData.operationSuccessWithData(flowServiceUrlPageResult);
    }

    /**
     * 获取工作界面
     * @param businessModelId
     * @return
     */
    @Override
    public ResponseData listAllWorkPage(String businessModelId) {
        if (StringUtils.isEmpty(businessModelId)) {
            return ResponseData.operationFailure("参数不能为空！");
        }
        List<WorkPageUrl> result = workPageUrlService.findSelectEdByBusinessModelId(businessModelId);
        return ResponseData.operationSuccessWithData(result);
    }

    /**
     * 获取岗位
     * @param searchVo
     * @return
     */
    @Override
    public PageResult<Position> listPositon(SearchVo searchVo) {
        Search search = new Search();
        search.addQuickSearchProperty("code");
        search.addQuickSearchProperty("name");
        search.addQuickSearchProperty("organization.name");
        if (StringUtils.isNotEmpty(searchVo.getQuick_value())) {
            search.setQuickSearchValue(searchVo.getQuick_value());
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(searchVo.getPage());
        pageInfo.setRows(searchVo.getRows());
        search.setPageInfo(pageInfo);

        if (StringUtils.isNotEmpty(searchVo.getSidx())) {
            if ("asc".equals(searchVo.getSord())) {
                search.addSortOrder(SearchOrder.asc(searchVo.getSidx()));
            } else {
                search.addSortOrder(SearchOrder.desc(searchVo.getSidx()));
            }
        }
        PageResult<Position> result = flowCommonUtil.getBasicPositionFindbypage(search);
        return result;
    }

    /**
     * 获取岗位类别
     * @return
     */
    @Override
    public List<PositionCategory> listPositonType() {
        return flowCommonUtil.getBasicPositioncategoryFindall();
    }

    /**
     * 获取组织维度
     * @return
     */
    @Override
    public List<OrganizationDimension> listOrganizationDimension() {
        return flowCommonUtil.getBasicOrgDimension();
    }

    /**
     * 根据流程实例获取当前流程所在节点
     * @param id
     * @param instanceId
     * @return
     */
    @Override
    public ResponseData getLookInfo(String id, String instanceId) {
        FlowDefVersion def = null;
        Map<String, Object> data = new HashedMap();
        String businessId = "";
        if(StringUtils.isNotEmpty(instanceId)){
            FlowInstance flowInstance = flowInstanceService.findOne(instanceId);
            if(flowInstance != null){
                def = flowInstance.getFlowDefVersion();
                businessId =  flowInstance.getBusinessId();
            }
        }
        if(def == null){
            def = flowDefVersionService.findOne(id);
        }
        data.put("def", def);
        if(StringUtils.isNotEmpty(instanceId)){
            Map<String,String> nodeIds = flowInstanceService.currentNodeIds(instanceId);
            data.put("currentNodes", nodeIds);
        }else{
            data.put("currentNodes", "[]");
        }
        //如果是固化流程，加载配合执行人信息
        if(def.getSolidifyFlow()!=null&& def.getSolidifyFlow()==true){
            ResponseData res =  flowSolidifyExecutorService.getExecuteInfoByBusinessId(businessId);
            if(res.getSuccess()){
                data.put("solidifyExecutorsInfo",res.getData());
            }else{
                data.put("solidifyExecutorsInfo",null);
            }
        }
        return ResponseData.operationSuccessWithData(data);
    }
}
