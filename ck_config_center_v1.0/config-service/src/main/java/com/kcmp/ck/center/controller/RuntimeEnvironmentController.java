package com.kcmp.ck.center.controller;

import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
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

import java.util.List;

/**
 * Created by kikock
 * 运行环境控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/runEnvironment")
public class RuntimeEnvironmentController {


    @Autowired
    private RuntimeEnvironmentService service;


    // 平台页面
    @RequestMapping("/show")
    public String show() {
        return "runtimeEnvironment/runtimeEnvironmentView";
    }


    /**
     * 新增运行环境
     */
    @GetMapping("/add")
    public String add() {
        return "runtimeEnvironment/add";
    }


    /**
     * 获取运行环境
     *
     */
    @ResponseBody
    @RequestMapping("/listAll")
    public TableDataInfo listAll(String name, ModelMap mmap) {
        List<RuntimeEnvironment> runtimeEnvironments = service.findByNameAll(name);
        return new TableDataInfo(runtimeEnvironments, runtimeEnvironments.size());
    }

    /**
     * 保存一个运行环境
     *
     * @param runtimeEnvironment 运行环境
     * @return 保存结果
     */
    @ResponseBody
    @PostMapping("/add")
    public OperateResultVo addSave(RuntimeEnvironment runtimeEnvironment) {
        return new OperateResultVo(service.save(runtimeEnvironment));
    }

    /**
     * 修改运行环境
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("runEnvironment", service.findOne(id));
        return "runtimeEnvironment/edit";
    }

    /**
     * 修改运行环境
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(RuntimeEnvironment runtimeEnvironment) {
        return new OperateResultVo(service.save(runtimeEnvironment));
    }



    /**
     * 删除一个平台
     *
     * @param ids 平台的Id标识
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.delete(ids));
    }
}
