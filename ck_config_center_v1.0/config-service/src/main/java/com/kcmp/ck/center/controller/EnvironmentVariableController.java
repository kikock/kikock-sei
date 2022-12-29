package com.kcmp.ck.center.controller;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.kcmp.ck.center.api.ApplicationModuleService;
import com.kcmp.ck.center.api.ApplicationServiceService;
import com.kcmp.ck.center.api.EnvironmentVariableService;
import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.EnvironmentVarConfig;
import com.kcmp.ck.config.entity.EnvironmentVariable;
import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.EnvVarConfig;
import com.kcmp.ck.config.entity.dto.EnvVarConfigSearch;
import com.kcmp.ck.config.entity.util.AesUtil;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
import com.kcmp.ck.config.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kikock
 * 环境变量控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/environmentVariable")
public class EnvironmentVariableController {
    @Autowired
    private EnvironmentVariableService service;
    @Autowired
    private RuntimeEnvironmentService runService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ApplicationModuleService applicationModuleService;

    @RequestMapping("/show")
    public String show(ModelMap mmap) {
        List<RuntimeEnvironment> runtimeEnvironments = runService.findAll();
        List<Platform> all = platformService.findAll();
        mmap.put("platforms", all);
        if (CollectionUtils.isNotEmpty(runtimeEnvironments) && runtimeEnvironments.size() > 0) {
            mmap.put("runtimeEnvironment", runtimeEnvironments.get(0));
        } else {
            mmap.put("runtimeEnvironment", "");
        }
        return "environmentVariable/environmentVariableView";
    }


    /**
     * 新增应用服务
     */
    @GetMapping("/add/{platformId}/{runtimeEnvironmentId}/{moduleId}")
    public String add(@PathVariable("platformId") String platformId,
                      @PathVariable("runtimeEnvironmentId") String runtimeEnvironmentId,
                      @PathVariable("moduleId") String moduleId,
                      ModelMap mmap) {
        //应用模块
        Platform platform = platformService.findOne(platformId);
        RuntimeEnvironment runtimeEnvironment = runService.findOne(runtimeEnvironmentId);
        //平台信息
        mmap.put("platform", platform);
        //运行环境
        mmap.put("runtimeEnv", runtimeEnvironment);
        mmap.put("isModule", false);
        if(!"moduleId".equals(moduleId)){
            mmap.put("isModule", true);
            ApplicationModule applicationModule = applicationModuleService.findOne(moduleId);
            mmap.put("module", applicationModule);
        }
        return "environmentVariable/add";
    }
    /**
     * 修改运行环境
     */
    @GetMapping("/edit/{environmentVariableId}/{runtimeEnvironmentId}")
    public String edit(@PathVariable("environmentVariableId") String environmentVariableId,@PathVariable("runtimeEnvironmentId")String runtimeEnvironmentId, ModelMap mmap) {
        EnvironmentVarConfig envConfig = service.findByEnvId(runtimeEnvironmentId,environmentVariableId);
        //平台信息
        mmap.put("envConfig", envConfig);
        //平台信息
        mmap.put("platform", envConfig.getEnvironmentVariable().getPlatform());
        //运行环境
        mmap.put("runtimeEnv", envConfig.getRuntimeEnvironment());
        mmap.put("isModule",false);
        mmap.put("envVariable",envConfig.getEnvironmentVariable());
        if(StringUtils.isNotBlank(envConfig.getEnvironmentVariable().getApplicationModuleId())) {
            //模块参数
            ApplicationModule applicationModule = applicationModuleService.findOne(envConfig.getEnvironmentVariable().getApplicationModuleId());
            mmap.put("isModule",true);
            mmap.put("module", applicationModule);
        }
        return "environmentVariable/edit";
    }
    /**
     * 修改运行环境
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(EnvVarConfig envVarConfig) {
        return new OperateResultVo(service.editSave(envVarConfig));
    }
    /**
     * 打开环境变量页面
     */
    @GetMapping("/add")
    public String add(EnvVarConfig envVarConfig, ModelMap mmap) {
        return "environmentVariable/add";
    }

    /**
     * 保存一个环境变量
     *
     * @param envVarConfig 环境变量
     * @return 保存结果
     */
    @ResponseBody
    @PostMapping("/add")
    public OperateResultVo addSave(EnvVarConfig envVarConfig) {
        return new OperateResultVo(service.save(envVarConfig));
    }
    /**
     * 通过平台和环境获取环境变量配置
     *
     * @param platformId           平台Id
     * @param runtimeEnvironmentId 运行环境Id
     * @return 环境变量配置清单
     */
    @ResponseBody
    @RequestMapping("/listAllByPlatformAndEnv")
    public List<EnvVarConfig> findByPlatformAndEnv(String platformId, String runtimeEnvironmentId) {
        return service.findByPlatformAndEnv(platformId, runtimeEnvironmentId);
    }

    /**
     * 通过查询参数获取环境变量配置清单
     *
     * @param search 查询参数
     * @return 环境变量配置清单
     */
    @ResponseBody
    @RequestMapping("/listAllBySearch")
    public TableDataInfo listAllBySearch(EnvVarConfigSearch search) {

        List<EnvVarConfig> envVarConfigList = service.findBySearch(search);

        return new TableDataInfo(envVarConfigList, envVarConfigList.size());
    }


    /**
     * 删除环境变量配置
     *
     * @param ids 环境变量id
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.delete(ids));
    }
}
