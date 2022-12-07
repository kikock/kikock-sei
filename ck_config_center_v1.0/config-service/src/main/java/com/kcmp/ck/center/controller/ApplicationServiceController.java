package com.kcmp.ck.center.controller;

import com.kcmp.ck.center.api.ApplicationModuleService;
import com.kcmp.ck.center.api.ApplicationServiceService;
import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.ApplicationService;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
import org.apache.commons.collections.CollectionUtils;
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
 * 应用服务控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/application")
public class ApplicationServiceController {


    @Autowired
    private ApplicationServiceService service;
    @Autowired
    private RuntimeEnvironmentService runService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ApplicationModuleService applicationModuleService;

    // 应用服务页面
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
        return "applicationService/appServiceView";
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
        List<ApplicationModule> applicationModuleList = applicationModuleService.getPlatformModule(platformId);
        mmap.put("platformModule", applicationModuleList);
        return "applicationService/add";
    }


    /**
     * 获取应用服务
     */
    @ResponseBody
    @RequestMapping("/listAll")
    public TableDataInfo listAll(String platformId, String runtimeEnvironmentId, String name) {
        List<ApplicationService> applicationServices = service.findByPlatformAndEnv(platformId, runtimeEnvironmentId,
                name);
        return new TableDataInfo(applicationServices, applicationServices.size());
    }

    /**
     * 保存一个应用服务
     *
     * @param applicationService 运行环境
     * @return 保存结果
     */
    @ResponseBody
    @PostMapping("/add")
    public OperateResultVo addSave(ApplicationService applicationService) {
        return new OperateResultVo(service.save(applicationService));
    }

    /**
     * 修改一个应用服务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("applicationService", service.findOne(id));
        return "applicationService/edit";
    }

    /**
     * 应用服务修改
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(ApplicationService applicationService) {
        return new OperateResultVo(service.save(applicationService));
    }


    /**
     * 删除一个应用服务
     *
     * @param ids 服务Id
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.delete(ids));
    }

    /**
     * 发布全局参数配置
     *
     * @param ids 应用服务Id
     * @return 操作结果
     */

    @ResponseBody
    @RequestMapping("/distribute")
    OperateResult distribute(String ids) {
        List<String> letters = Arrays.asList(ids);
        return new OperateResultVo(service.distribute(letters));
    }
}
