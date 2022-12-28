package com.kcmp.ck.center.controller;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.kcmp.ck.center.api.ApplicationServiceService;
import com.kcmp.ck.center.api.EnvironmentVariableService;
import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.EnvVarConfig;
import com.kcmp.ck.config.entity.dto.EnvVarConfigSearch;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
import com.kcmp.ck.config.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
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
    @GetMapping("/add/{platformId}/{runtimeEnvironmentId}")
    public String add(@PathVariable("platformId") String platformId,
                      @PathVariable("runtimeEnvironmentId") String runtimeEnvironmentId, ModelMap mmap) {
        //平台id
        mmap.put("platformId", platformId);
        //运行环境
        mmap.put("runtimeEnvironmentId", runtimeEnvironmentId);
        //应用模块
        // List<ApplicationModule> applicationModuleList = applicationModuleService.getPlatformModule(platformId);
        // mmap.put("platformModule", applicationModuleList);
        return "environmentVariable/add";
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
        boolean leftEnv = search.getLeftEnv();
        if (leftEnv){
            List<EnvVarConfig> envConfigList = service.findByPlatformAndEnv(search.getPlatformId(), search.getRuntimeEnvironmentId());
            return new TableDataInfo(envConfigList, envConfigList.size());

        }
        List<EnvVarConfig> envVarConfigList = service.findBySearch(search);

        return new TableDataInfo(envVarConfigList, envVarConfigList.size());
    }
    //
    // /**
    //  * 保存一个环境变量配置
    //  *
    //  * @param envVarConfig 环境变量配置
    //  * @return 保存结果
    //  */
    // @ResponseBody
    // @RequestMapping("/save")
    // public OperateResultVo save(EnvVarConfig envVarConfig) {
    //     EnvironmentVariableService client = JAXRSClientFactory.create(ServletApplication.apiBaseAddress, EnvironmentVariableService.class, Arrays.asList(new JacksonJsonProvider()));
    //     return new OperateResultVo(client.save(envVarConfig));
    // }
    //
    // /**
    //  * 删除环境变量配置
    //  *
    //  * @param environmentVariableId 环境变量id
    //  * @return 操作结果
    //  */
    // @ResponseBody
    // @RequestMapping("/delete")
    // public OperateResultVo delete(String environmentVariableId) {
    //     EnvironmentVariableService client = JAXRSClientFactory.create(ServletApplication.apiBaseAddress, EnvironmentVariableService.class, Arrays.asList(new JacksonJsonProvider()));
    //     return new OperateResultVo(client.delete(environmentVariableId));
    // }
    //
    // /**
    //  * 用平台默认的密钥加密
    //  *
    //  * @param data 待加密数据
    //  * @return 加密后的数据
    //  */
    // @ResponseBody
    // @RequestMapping("/encrypt")
    // public Object encrypt(String data) {
    //     String results = AesUtil.encrypt(data);
    //     return new Object[]{results};
    // }
    //
    // /**
    //  * 用平台默认的密钥解密
    //  *
    //  * @param data 待解密数据
    //  * @return 解密后的数据
    //  */
    // @ResponseBody
    // @RequestMapping("/decrypt")
    // public Object decrypt(String data) {
    //     String results = AesUtil.decrypt(data);
    //     return new Object[]{results};
    // }
}
