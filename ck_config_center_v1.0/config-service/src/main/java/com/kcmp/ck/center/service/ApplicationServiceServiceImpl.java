package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.ApplicationServiceService;
import com.kcmp.ck.center.dao.ApplicationServiceDao;
import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import com.kcmp.ck.vo.ResponseData;
import jodd.typeconverter.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by kikock
 * 应用服务服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class ApplicationServiceServiceImpl implements ApplicationServiceService {

    @Autowired
    private ApplicationServiceDao dao;

    @Autowired
    private GlobalParamServiceImpl globalParamService;

    /**
     * 获取所有数据
     *
     * @return 应用服务清单
     */
    @Override
    public List<ApplicationService> findAll() {
        return dao.findAll();
    }

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 应用服务
     */
    @Override
    public ApplicationService findOne(String id) {
        Optional<ApplicationService> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存一个应用服务
     *
     * @param applicationService 应用服务
     * @return 操作结果
     */
    @Override
    public OperateResult save(ApplicationService applicationService) {
        //判断AppId 是否变化
        if (applicationService.isNew()) {
            // 判断 平台+运行环境+模块的服务是否存在
            ApplicationService checkIsSave = dao.checkIsSave(applicationService.getPlatformId(),
                    applicationService.getRuntimeEnvironmentId(), applicationService.getApplicationModuleId());
            if (Objects.nonNull(checkIsSave)) {
                return new OperateResult(checkIsSave.getRemark() + "应用标识[" + checkIsSave.getAppId() + "]已经创建服务！");
            }
        }
        applicationService.setApplicationModule(null);
        ApplicationService entity = dao.save(applicationService);
        return new OperateResult(entity.getRemark() + "保存成功！");
    }

    /**
     * 删除一个应用服务
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @Override
    public OperateResult delete(String ids) {
        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            ApplicationService entity = findOne(id);
            if (Objects.isNull(entity)) {
                return new OperateResult(OperateStatusEnum.ERROR, "要删除的应用服务不存在!");
            }
            names.add(entity.getApplicationModule().getName());
            dao.deleteById(id);
            //删除原AppId的zookeeper节点
            globalParamService.deleteZookeeperNode(id);
        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]删除成功！");
    }

    /**
     * 发布全局参数配置
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @Override
    public OperateResult distribute(List<String> ids) {
        OperateResult result = new OperateResult("全局参数配置发布成功！");
        List<ApplicationService> applicationServices = dao.findAllById(ids);
        if (applicationServices.isEmpty()) {
            return result;
        }
        for (ApplicationService a : applicationServices) {
            try {
                globalParamService.saveZookeeperNode(a.getAppId());
            } catch (Exception e) {
                e.printStackTrace();
                result.setOperateStatusEnum(OperateStatusEnum.ERROR);
                result.setMessage(String.format("应用服务【%s】发布失败！%s", a.getRemark(), e.getMessage()));
                return result;
            }
        }
        result.setMessage(String.format("%d个应用服务的全局参数配置发布成功！", applicationServices.size()));
        return result;
    }
    /**
     * 发布全局参数配置
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */
    @Override
    public ResponseData findZookeeperData(List<String> ids) {
        ResponseData result = ResponseData.build();
        List<ApplicationService> applicationServices = dao.findAllById(ids);
        if (applicationServices.isEmpty()) {
            return result.setSuccess(false).setMessage("操作失败");
        }
        Map<String,HashMap> map = new HashMap<>();
        for (ApplicationService a : applicationServices) {
            try {
                HashMap zookeeperNode = globalParamService.getZookeeperNode(a.getAppId());
                zookeeperNode.put("appId",a.getAppId());
                map.put(a.getRemark(),zookeeperNode);
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage(String.format("应用服务【%s】查找失败！%s", a.getRemark(), e.getMessage()));
                return result;
            }
        }
        result.setMessage(String.format("找到%d个应用服务的全局参数！", applicationServices.size()));
        result.setSuccess(true);
        result.setData(map);
        return result;
    }
    /**
     * 通过平台和环境获取应用服务
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @param name 模块名称
     * @return 应用服务清单
     */
    @Override
    public List<ApplicationService> findByPlatformAndEnv(String platformId, String runtimeEnvironmentId,String name) {
        return dao.findAllByPlatformIdAndRuntimeEnvironmentIdAndAndName(platformId, runtimeEnvironmentId,name);
    }

    /**
     * 通过AppId标识获取实体
     *
     * @param appId AppId标识
     * @return 应用服务
     */
    @Override
    public ApplicationService findByAppId(String appId) {
        return dao.findFirstByAppId(appId);
    }
}
