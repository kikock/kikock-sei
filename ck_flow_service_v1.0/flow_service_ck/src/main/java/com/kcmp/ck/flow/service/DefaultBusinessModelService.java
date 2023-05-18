package com.kcmp.ck.flow.service;

import com.kcmp.core.ck.dao.BaseEntityDao;
import com.kcmp.core.ck.dao.jpa.BaseDao;
import com.kcmp.core.ck.service.BaseEntityService;
import com.kcmp.core.ck.service.Validation;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.flow.api.*;
import com.kcmp.ck.flow.api.IBusinessModelService;
import com.kcmp.ck.flow.api.IDefaultBusinessModel2Service;
import com.kcmp.ck.flow.api.IDefaultBusinessModel3Service;
import com.kcmp.ck.flow.api.IDefaultBusinessModelService;
import com.kcmp.ck.flow.api.IFlowDefinationService;
import com.kcmp.ck.flow.api.IFlowInstanceService;
import com.kcmp.ck.flow.basic.vo.Employee;
import com.kcmp.ck.flow.basic.vo.Executor;
import com.kcmp.ck.flow.common.util.BusinessUtil;
import com.kcmp.ck.flow.constant.BusinessEntityAnnotaion;
import com.kcmp.ck.flow.constant.FlowStatus;
import com.kcmp.ck.flow.dao.DefaultBusinessModelDao;
import com.kcmp.ck.flow.entity.*;
import com.kcmp.ck.flow.util.CodeGenerator;
import com.kcmp.ck.flow.util.FlowCommonUtil;
import com.kcmp.ck.flow.vo.FlowInvokeParams;
import com.kcmp.ck.flow.vo.FlowOperateResult;
import com.kcmp.ck.flow.vo.FlowTaskCompleteVO;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.util.JsonUtils;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.flow.entity.BusinessModel;
import com.kcmp.ck.flow.entity.DefaultBusinessModel;
import com.kcmp.ck.flow.entity.DefaultBusinessModel2;
import com.kcmp.ck.flow.entity.DefaultBusinessModel3;
import com.kcmp.ck.flow.entity.FlowDefination;
import com.kcmp.ck.flow.entity.IBusinessFlowEntity;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by kikock
 * 默认业务表单服务
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class DefaultBusinessModelService extends BaseEntityService<DefaultBusinessModel> implements IDefaultBusinessModelService {

    @Autowired
    private DefaultBusinessModelDao defaultBusinessModelDao;

    @Autowired
    private FlowCommonUtil flowCommonUtil;

    @Override
    protected BaseEntityDao<DefaultBusinessModel> getDao() {
        return this.defaultBusinessModelDao;
    }

    /**
     * 数据保存操作
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public OperateResultWithData<DefaultBusinessModel> save(DefaultBusinessModel entity) {
        Validation.notNull(entity, "持久化对象不能为空");
//        String businessCode = NumberGenerator.getNumber(DefaultBusinessModel.class);
        String businessCode = CodeGenerator.genCodes(6, 1).get(0);
        if (StringUtils.isEmpty(entity.getBusinessCode())) {
            entity.setBusinessCode(businessCode);
        }
        return super.save(entity);
    }

    /**
     * 测试事前
     * @param id        单据id
     * @param paramJson json参数
     * @return 执行结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String changeCreateDepict(String id, String paramJson) {
        Map<String, Object> variables = new HashMap<String, Object>();

        DefaultBusinessModel entity = defaultBusinessModelDao.findOne(id);
        if (entity != null) {
//            defaultBusinessModelDao.saveAndFlush(entity);
            if (StringUtils.isNotEmpty(paramJson)) {

                try {
                    JSONObject jsonObject = JSONObject.fromObject(paramJson);
                    List<String> callActivtiySonPaths = null;
                    callActivtiySonPaths = jsonObject.getJSONArray("callActivtiySonPaths");
                    if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                        //测试跨业务实体子流程,并发多级子流程测试
                        List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                        List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                        List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                        for (String callActivityPath : callActivtiySonPaths) {
                            if (StringUtils.isNotEmpty(callActivityPath)) {
                                Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                                initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error(e.getMessage(), e);
                }
            }
            paramJson = "before";
            entity.setWorkCaption(paramJson + ":" + entity.getWorkCaption());
            defaultBusinessModelDao.save(entity);
        }
        String param = JsonUtils.toJson(variables);
        return param;
    }

    /**
     * 测试事后
     * @param id        单据id
     * @param paramJson json参数
     * @return 执行结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String changeCompletedDepict(String id, String paramJson) {
        Map<String, Object> variables = new HashMap<String, Object>();

        DefaultBusinessModel entity = defaultBusinessModelDao.findOne(id);
        if (entity != null) {
//            defaultBusinessModelDao.saveAndFlush(entity);
            if (StringUtils.isNotEmpty(paramJson)) {

                try {
                    JSONObject jsonObject = JSONObject.fromObject(paramJson);
                    List<String> callActivtiySonPaths = null;
                    callActivtiySonPaths = jsonObject.getJSONArray("callActivtiySonPaths");
                    if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                        //测试跨业务实体子流程,并发多级子流程测试
                        List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                        List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                        List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                        for (String callActivityPath : callActivtiySonPaths) {
                            if (StringUtils.isNotEmpty(callActivityPath)) {
                                Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                                initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error(e.getMessage(), e);
                }

            }
            paramJson = "after";
            entity.setWorkCaption(paramJson + ":" + entity.getWorkCaption());
            defaultBusinessModelDao.save(entity);
        }
        String param = JsonUtils.toJson(variables);
        return param;
    }

    /**
     * 测试自定义执行人选择
     * @param flowInvokeParams 流程参数
     * @return 执行结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Executor> getPersonToExecutorConfig(FlowInvokeParams flowInvokeParams) {
        String businessId = flowInvokeParams.getId();
        List<Executor> executors = new ArrayList<Executor>();
        if (StringUtils.isNotEmpty(businessId)) {
            DefaultBusinessModel defaultBusinessModel = defaultBusinessModelDao.findOne(businessId);
            if (defaultBusinessModel != null) {
                String orgid = defaultBusinessModel.getOrgId();
                //根据组织机构ID获取员工集合
                List<Employee> employeeList = flowCommonUtil.getEmployeesByOrgId(orgid);
                List<String> idList = new ArrayList<String>();
                for (Employee e : employeeList) {
                    idList.add(e.getId());
                }
                //获取执行人
                if (idList.isEmpty()) {
                    idList.add("00A0E06A-CAAF-11E7-AA54-0242C0A84202");
                    idList.add("8A6A1592-4A95-11E7-A011-960F8309DEA7");

                }
                //根据用户的id列表获取执行人
                executors = flowCommonUtil.getBasicUserExecutors(idList);
            }
        }
        return executors;
    }


    /**
     * 接收任务测试接口
     * @param id           业务单据id
     * @param changeText   参数文本
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean testReceiveCall(String id, String changeText) {
        boolean result = false;
        String receiveTaskActDefId = null;
        Map<String, Object> variables = new HashMap<String, Object>();

        DefaultBusinessModel entity = defaultBusinessModelDao.findOne(id);
        if (entity != null) {
            if (StringUtils.isNotEmpty(changeText)) {
                JSONObject jsonObject = JSONObject.fromObject(changeText);
                //HashMap<String,Object> params =   JsonUtils.fromJson(changeText, new TypeReference<HashMap<String,Object>>() {});
                receiveTaskActDefId = jsonObject.get("receiveTaskActDefId") + "";
                List<String> callActivtiySonPaths = null;
                try {
                    callActivtiySonPaths = jsonObject.getJSONArray("callActivtiySonPaths");
                } catch (Exception e) {
                    LogUtil.error(e.getMessage(), e);
                }
                if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                    //测试跨业务实体子流程,并发多级子流程测试
                    List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                    List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                    List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                    for (String callActivityPath : callActivtiySonPaths) {
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                            initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                        }
                    }
                }
            }
            changeText = "ReceiveCall";
            entity.setWorkCaption(entity.getWorkCaption() + ":" + changeText);
            defaultBusinessModelDao.save(entity);
            final String fReceiveTaskActDefId = receiveTaskActDefId;
            new Thread(new Runnable() {//模拟异步
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    IFlowInstanceService proxy = ApiClient.createProxy(IFlowInstanceService.class);
                    proxy.signalByBusinessId(id, fReceiveTaskActDefId, variables);
                }
            }).start();

            result = true;
        }
        return result;
    }

    /**
     * 任务启动检查
     * @param id 业务单据id
     * @return
     */
    @Override
    public boolean checkStartFlow(String id) {
        return true;
    }

    /**
     * 任务结束
     * @param id 业务单据id
     */
    @Override
    public void endCall(String id) {
        LogUtil.bizLog("id=" + id);
    }

    /**
     * 解析子流程绝对路径
     * @param callActivityPath 路径值
     * @return 路径值为key，子流程id为value的MAP对象
     */
    protected Map<String, String> initCallActivtiy(String callActivityPath, boolean ifStart) {
        Map<String, String> resultMap = new HashMap<String, String>();
        //  String str ="/caigouTestZhu/CallActivity_3/yewushengqing2";
        String str = callActivityPath;
        String[] resultArray = str.split("/");
        if ((resultArray.length < 4) || (resultArray.length % 2 != 0)) {
            throw new RuntimeException("子流程路径解析错误");
        }
        List<String> resultList = new ArrayList<String>();
        for (int i = 1; i < resultArray.length; i++) {
            resultList.add(resultArray[i]);
        }
        int size = resultList.size();
        for (int j = 1; j < size; ) {
            String key = resultList.get(size - j);
            int endIndex = str.lastIndexOf(key) + key.length();
            String path = str.substring(0, endIndex);
            resultMap.put(path, key);
            j += 2;
            if (!ifStart) {
                break;//只生成一条测试数据
            }
        }
        return resultMap;
    }

    /**
     * 新任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    public FlowOperateResult newServiceCall(FlowInvokeParams flowInvokeParams) {
        FlowOperateResult result = new FlowOperateResult();
        String businessId = flowInvokeParams.getId();
        DefaultBusinessModel entity = defaultBusinessModelDao.findOne(businessId);
        String changeText = "newServiceCall";
        entity.setWorkCaption(changeText + ":" + entity.getWorkCaption());
        defaultBusinessModelDao.save(entity);
        int shujishu = getShuiJiShu(0, 10);
        if (shujishu == 5) {
            throw new RuntimeException("测试随机抛出错误信息:" + new Date());
        } else if (shujishu == 4) {
            result.setSuccess(false);
            result.setMessage("测试随机业务异常信息:" + new Date());
        }
        return result;
    }

    /**
     * 异常任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    public FlowOperateResult newServiceCallFailure(FlowInvokeParams flowInvokeParams) {
        FlowOperateResult result = new FlowOperateResult();
        String businessId = flowInvokeParams.getId();
        DefaultBusinessModel entity = defaultBusinessModelDao.findOne(businessId);
        String changeText = "newServiceCall_failure";
        entity.setWorkCaption(changeText + ":" + entity.getWorkCaption());
        defaultBusinessModelDao.save(entity);
        result.setSuccess(true);
        result.setMessage("测试业务异常信息:" + new Date());
        return result;
    }

    /**
     * 获取指定范围的随机数
     * @param max
     * @param min
     * @return
     */
    private static int getShuiJiShu(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }


    /**
     * 仅针对跨业务实体子任务的测试初始化方法
     * @param defaultBusinessModelList
     * @param defaultBusinessModel2List
     * @param defaultBusinessModel3List
     * @param callActivityPathMap
     * @param variables
     * @param parentBusinessModel
     */
    protected void initCallActivityBusiness(List<DefaultBusinessModel> defaultBusinessModelList, List<DefaultBusinessModel2> defaultBusinessModel2List, List<DefaultBusinessModel3> defaultBusinessModel3List, Map<String, String> callActivityPathMap, Map<String, Object> variables, IBusinessFlowEntity parentBusinessModel) {
        IDefaultBusinessModelService defaultBusinessModelService = ApiClient.createProxy(IDefaultBusinessModelService.class);
        IDefaultBusinessModel2Service defaultBusinessModel2Service = ApiClient.createProxy(IDefaultBusinessModel2Service.class);
        IDefaultBusinessModel3Service defaultBusinessModel3Service = ApiClient.createProxy(IDefaultBusinessModel3Service.class);

        IFlowDefinationService flowDefinationService = ApiClient.createProxy(IFlowDefinationService.class);

        for (Map.Entry<String, String> entry : callActivityPathMap.entrySet()) {
            String realDefiniationKey = entry.getValue();
            String realPathKey = entry.getKey();
            FlowDefination flowDefination = flowDefinationService.findByKey(realDefiniationKey);
            String sonBusinessModelCode = flowDefination.getFlowType().getBusinessModel().getClassName();
            if ("com.kcmp.ck.flow.entity.DefaultBusinessModel".equals(sonBusinessModelCode)) {
                DefaultBusinessModel defaultBusinessModel = new DefaultBusinessModel();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel);
                String name = "temp_测试跨业务实体子流程_默认业务实体" + System.currentTimeMillis();
                defaultBusinessModel.setName(name);
                defaultBusinessModel.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel.setId(null);
                defaultBusinessModel.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel> resultWithData = defaultBusinessModelService.save(defaultBusinessModel);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel = resultWithData.getData();
                defaultBusinessModelList.add(defaultBusinessModel);
            } else if ("com.kcmp.ck.flow.entity.DefaultBusinessModel2".equals(sonBusinessModelCode)) {
                DefaultBusinessModel2 defaultBusinessModel2Son = new DefaultBusinessModel2();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel2Son);
                String name = "temp_测试跨业务实体子流程_采购实体" + System.currentTimeMillis();
                defaultBusinessModel2Son.setName(name);
                defaultBusinessModel2Son.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel2Son.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel2Son.setId(null);
                defaultBusinessModel2Son.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel2> resultWithData = defaultBusinessModel2Service.save(defaultBusinessModel2Son);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel2Son = resultWithData.getData();
                defaultBusinessModel2List.add(defaultBusinessModel2Son);
            } else if ("com.kcmp.ck.flow.entity.DefaultBusinessModel3".equals(sonBusinessModelCode)) {
                DefaultBusinessModel3 defaultBusinessModel3Son = new DefaultBusinessModel3();
                BeanUtils.copyProperties(parentBusinessModel, defaultBusinessModel3Son);
                String name = "temp_测试跨业务实体子流程_销售实体" + System.currentTimeMillis();
                defaultBusinessModel3Son.setName(name);
                defaultBusinessModel3Son.setFlowStatus(FlowStatus.INPROCESS);
                defaultBusinessModel3Son.setWorkCaption(parentBusinessModel.getWorkCaption() + "||" + name);
                defaultBusinessModel3Son.setId(null);
                defaultBusinessModel3Son.setBusinessCode(null);
                OperateResultWithData<DefaultBusinessModel3> resultWithData = defaultBusinessModel3Service.save(defaultBusinessModel3Son);
                String defaultBusinessModelId = resultWithData.getData().getId();
                variables.put(realPathKey, defaultBusinessModelId);
                defaultBusinessModel3Son = resultWithData.getData();
                defaultBusinessModel3List.add(defaultBusinessModel3Son);
            }
        }
    }

    /**
     * 测试事前（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult changeCreateDepictNew(FlowInvokeParams flowInvokeParams) {
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FlowOperateResult result = new FlowOperateResult();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                try {

                    List<String> callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                    if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                        //测试跨业务实体子流程,并发多级子流程测试
                        List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                        List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                        List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                        for (String callActivityPath : callActivtiySonPaths) {
                            if (StringUtils.isNotEmpty(callActivityPath)) {
                                Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                                initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error(e.getMessage(), e);
                }

            }
            String changeText = "before";
            if (flowInvokeParams.getNextNodeUserInfo() != null) {
                Map<String, List<String>> map = flowInvokeParams.getNextNodeUserInfo();
                Set set = map.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mapentry = (Map.Entry) iterator.next();
                    changeText += mapentry.getKey() + "/" + mapentry.getValue();
                }
//                changeText+=flowInvokeParams.getNextNodeUserInfo().get(0).get(0);
            }
            entity.setWorkCaption(changeText + ":" + entity.getWorkCaption());
            defaultBusinessModelDao.save(entity);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 测试事后（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult changeCompletedDepictNew(FlowInvokeParams flowInvokeParams) {
        Map<String, Object> variables = new HashMap<String, Object>();
        FlowOperateResult result = new FlowOperateResult();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                try {

                    List<String> callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                    if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                        //测试跨业务实体子流程,并发多级子流程测试
                        List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                        List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                        List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                        for (String callActivityPath : callActivtiySonPaths) {
                            if (StringUtils.isNotEmpty(callActivityPath)) {
                                Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                                initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error(e.getMessage(), e);
                }

            }
            String changeText = "after";
            if (flowInvokeParams.getNextNodeUserInfo() != null) {
                Map<String, List<String>> map = flowInvokeParams.getNextNodeUserInfo();
                Set set = map.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mapentry = (Map.Entry) iterator.next();
                    changeText += mapentry.getKey() + "/" + mapentry.getValue();
                }
//                changeText+=flowInvokeParams.getNextNodeUserInfo().get(0).get(0);
            }
            entity.setWorkCaption(changeText + ":" + entity.getWorkCaption());
            defaultBusinessModelDao.save(entity);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 接收任务测试接口（新）
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult testReceiveCallNew(FlowInvokeParams flowInvokeParams) {
        FlowOperateResult result = new FlowOperateResult();
        String receiveTaskActDefId = null;
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                receiveTaskActDefId = flowInvokeParams.getTaskActDefId();
                List<String> callActivtiySonPaths = null;
                callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                    //测试跨业务实体子流程,并发多级子流程测试
                    List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                    List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                    List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                    for (String callActivityPath : callActivtiySonPaths) {
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                            initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                        }
                    }
                }
                String changeText = "ReceiveCall";
                entity.setWorkCaption(entity.getWorkCaption() + ":" + changeText);
                defaultBusinessModelDao.save(entity);
                final String fReceiveTaskActDefId = receiveTaskActDefId;
                new Thread(new Runnable() {//模拟异步
                    @Override
                    public void run() {
                        long time = 20; //默认20秒
                        int index = 20;//重试20次
                        while (index > 0) {
                            try {
                                Thread.sleep(1000 * time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                IFlowInstanceService proxy = ApiClient.createProxy(IFlowInstanceService.class);
                                OperateResult resultTemp = proxy.signalByBusinessId(flowInvokeParams.getId(), fReceiveTaskActDefId, variables);
                                if (resultTemp.successful()) {
                                    return;
                                } else {
                                    time = time * 2; //加倍
                                    LogUtil.error(resultTemp.getMessage());
                                }
                            } catch (Exception e) {
                                time = time * 4; //加倍
                                LogUtil.error(e.getMessage(), e);
                            }
                            index--;
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            LogUtil.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 测试工作池任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult testPoolTaskSignal(FlowInvokeParams flowInvokeParams) {
        System.out.println(flowInvokeParams.getPoolTaskCode());
        FlowOperateResult result = new FlowOperateResult();
        String taskActDefId = null;
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                taskActDefId = flowInvokeParams.getTaskActDefId();
                List<String> callActivtiySonPaths = null;
                callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                    //测试跨业务实体子流程,并发多级子流程测试
                    List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                    List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                    List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                    for (String callActivityPath : callActivtiySonPaths) {
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                            initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                        }
                    }
                }
                entity.setWorkCaption("工作池任务：[ID:" + flowInvokeParams.getId() + ",actId:" + flowInvokeParams.getTaskActDefId() + "]");
                defaultBusinessModelDao.save(entity);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            LogUtil.error(e.getMessage(), e);
        }
        result.setSuccess(true);
        result.setMessage("test success!");
        //直接返回用户ID也可以直接进行待办添加（工作池任务添加执行人方式2）
        result.setUserId("1592D012-A330-11E7-A967-02420B99179E,1AE28F00-2FFC-11E9-AC2E-0242C0A84417");
        return result;
    }

    /**
     * 任务创建池
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult testPoolTaskCreatePool(FlowInvokeParams flowInvokeParams) {
        System.out.println(flowInvokeParams.getPoolTaskCode());
        FlowOperateResult result = new FlowOperateResult();
        String taskActDefId = null;
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                taskActDefId = flowInvokeParams.getTaskActDefId();
                List<String> callActivtiySonPaths = null;
                callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                    //测试跨业务实体子流程,并发多级子流程测试
                    List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                    List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                    List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                    for (String callActivityPath : callActivtiySonPaths) {
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                            initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                        }
                    }
                }
                String changeText = "PoolCallPoolCreate";
                entity.setWorkCaption(entity.getWorkCaption() + ":" + changeText);
                defaultBusinessModelDao.save(entity);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            LogUtil.error(e.getMessage(), e);
        }
        result.setSuccess(true);
        result.setMessage("test success!");
//        result.setUserId("8A6A1592-4A95-11E7-A011-960F8309DEA7");
        return result;
    }

    /**
     * 测试完整的工作池任务
     * @param flowInvokeParams 流程参数
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FlowOperateResult testPoolTaskComplete(FlowInvokeParams flowInvokeParams) {
        FlowOperateResult result = new FlowOperateResult();
        String taskActDefId = null;
        Map<String, Object> variables = new HashMap<String, Object>();
        try {
            DefaultBusinessModel entity = defaultBusinessModelDao.findOne(flowInvokeParams.getId());
            if (entity != null) {
                taskActDefId = flowInvokeParams.getTaskActDefId();
                List<String> callActivtiySonPaths = null;
                callActivtiySonPaths = flowInvokeParams.getCallActivitySonPaths();
                if (callActivtiySonPaths != null && !callActivtiySonPaths.isEmpty()) {
                    //测试跨业务实体子流程,并发多级子流程测试
                    List<DefaultBusinessModel> defaultBusinessModelList = new ArrayList<>();
                    List<DefaultBusinessModel2> defaultBusinessModel2List = new ArrayList<>();
                    List<DefaultBusinessModel3> defaultBusinessModel3List = new ArrayList<>();
                    for (String callActivityPath : callActivtiySonPaths) {
                        if (StringUtils.isNotEmpty(callActivityPath)) {
                            Map<String, String> callActivityPathMap = initCallActivtiy(callActivityPath, true);
                            initCallActivityBusiness(defaultBusinessModelList, defaultBusinessModel2List, defaultBusinessModel3List, callActivityPathMap, variables, entity);
                        }
                    }
                }
                String changeText = "PoolCallComplete";
                entity.setWorkCaption(entity.getWorkCaption() + ":" + changeText);
                defaultBusinessModelDao.save(entity);
                final String fTaskActDefId = taskActDefId;
                new Thread(new Runnable() {//模拟异步
                    @Override
                    public void run() {
                        long time = 200; //默认200秒
                        int index = 2;//重试20次
                        while (index > 0) {
                            try {
                                Thread.sleep(1000 * time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                IFlowInstanceService proxy = ApiClient.createProxy(IFlowInstanceService.class);
                                FlowTaskCompleteVO flowTaskCompleteVO = new FlowTaskCompleteVO();
                                OperateResultWithData<FlowStatus> resultTemp = proxy.completePoolTask(flowInvokeParams.getId(), fTaskActDefId, "8A6A1592-4A95-11E7-A011-960F8309DEA7", flowTaskCompleteVO);
                                if (resultTemp.successful()) {
                                    return;
                                } else {
                                    time = time * 2; //加倍
                                    LogUtil.error(resultTemp.getMessage());
                                }
                            } catch (Exception e) {
                                time = time * 4; //加倍
                                LogUtil.error(e.getMessage(), e);
                            }
                            index--;
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 业务属性和值
     * @param businessModelCode 业务属性编码
     * @param id                业务表单id
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> businessPropertiesAndValues(String businessModelCode, String id) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        IBusinessModelService businessModelService;
        businessModelService = ApiClient.createProxy(IBusinessModelService.class);
        BusinessModel businessModel = businessModelService.findByClassName(businessModelCode);
        String daoBeanName = null;
        if (businessModel != null) {
            daoBeanName = getDaoBeanName(businessModelCode);
        }
        ApplicationContext applicationContext = ContextUtil.getApplicationContext();
        BaseDao appModuleDao = (BaseDao) applicationContext.getBean(daoBeanName);
        DefaultBusinessModel content = (DefaultBusinessModel) appModuleDao.findOne(id);
        DefaultBusinessModel2 defaultBusinessModel2 = new DefaultBusinessModel2();
        defaultBusinessModel2.setId("testId2");
        defaultBusinessModel2.setBusinessCode("testBusinessCode2");
        defaultBusinessModel2.setWorkCaption("testWorkCaption2");
        defaultBusinessModel2.setName("testName2");
        defaultBusinessModel2.setCount(4);
        defaultBusinessModel2.setUnitPrice(3);
        defaultBusinessModel2.setSum(defaultBusinessModel2.getCount() * defaultBusinessModel2.getUnitPrice());
        content.setDefaultBusinessModel2(defaultBusinessModel2);
        return BusinessUtil.getPropertiesAndValues(content, null);
    }

    private String getDaoBeanName(String className) throws ClassNotFoundException {
        BusinessEntityAnnotaion businessEntityAnnotaion = this.getBusinessEntityAnnotaion(className);
        return businessEntityAnnotaion.daoBean();
    }

    private BusinessEntityAnnotaion getBusinessEntityAnnotaion(String className) throws ClassNotFoundException {
        if (StringUtils.isNotEmpty(className)) {
            Class sourceClass = Class.forName(className);
            BusinessEntityAnnotaion businessEntityAnnotaion = (BusinessEntityAnnotaion) sourceClass.getAnnotation(BusinessEntityAnnotaion.class);
            return businessEntityAnnotaion;
        } else {
            throw new RuntimeException("className is null!");
        }
    }

}
