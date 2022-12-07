package com.kcmp.ck.center.controller;

import com.kcmp.ck.center.api.ApplicationModuleService;
import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by kikock
 * 应用模块控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/module")
public class ApplicationModuleController {


    @Autowired
    private ApplicationModuleService service;

    @Autowired
    private PlatformService platformService;
    // 应用模块页面
    @RequestMapping("/show")
    public String show(ModelMap mmap) {
        List<Platform> all = platformService.findAll();
        mmap.put("platforms",all);
        return "module/applicationModuleView";
    }


    /**
     * 新增应用模块页面
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id , ModelMap mmap) {
        mmap.put("platformId",id);
        return "module/add";
    }
    /**
     * 修改应用模块
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("module", service.findOne(id));
        return "module/edit";
    }

    /**
     * 修改保存平台
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(ApplicationModule module) {
        return new OperateResultVo(service.save(module));
    }

    /**
     * 保存一个应用模块
     */
    @ResponseBody
    @RequestMapping("/add")
    public OperateResultVo addSave(ApplicationModule module) {
        return new OperateResultVo(service.save(module));
    }


    /**
     * 获取应用模块
     *
     */
    @ResponseBody
    @RequestMapping("/listAllModule")
    public TableDataInfo listAllModule() {
        List<ApplicationModule> applicationModuleList = service.findAll();
        return new TableDataInfo(applicationModuleList, applicationModuleList.size());
    }

    /**
     * 获取平台应用模块
     *
     */
    @ResponseBody
    @RequestMapping("/listPlatformModule")
    public TableDataInfo listPlatformModule(String platformId) {
        List<ApplicationModule> applicationModuleList = service.getPlatformModule(platformId);
        return new TableDataInfo(applicationModuleList, applicationModuleList.size());
    }

    /**
     * 删除一个应用模块
     * @param ids 应用模块的Id标识
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.delete(ids));
    }
}
