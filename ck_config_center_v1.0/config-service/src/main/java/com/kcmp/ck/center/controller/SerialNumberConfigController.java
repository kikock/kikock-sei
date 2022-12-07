package com.kcmp.ck.center.controller;


import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.api.SerialNumberConfigService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.SerialNumberConfig;
import com.kcmp.ck.config.entity.TestNum;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
import com.kcmp.ck.context.util.NumberGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

/**
 * Created by kikock
 * 生成编号控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/serialNumberConfig")
public class SerialNumberConfigController {

    @Autowired
    private RuntimeEnvironmentService runService;
    @Autowired
    private SerialNumberConfigService service;

    @RequestMapping("/show")
    public String show(ModelMap mmap) {
        List<RuntimeEnvironment> runtimeEnvironments = runService.findAll();
        mmap.put("runtimeEnvironment", runtimeEnvironments);
        return "serialNumber/serialNumberConfigView";
    }

    /**
     * 获取所有系编号生成器配置
     *
     * @return 编号生成器配置清单
     */
    @ResponseBody
    @RequestMapping("listAll")
    public TableDataInfo listAll(String envCode, String name, ModelMap mmap) {
        List<SerialNumberConfig> serialNumberConfigs = service.findAllByEnvCodeAndName(envCode, name);
        return new TableDataInfo(serialNumberConfigs, serialNumberConfigs.size());
    }


    /**
     * 新增应用服务
     */
    @GetMapping("/add/{envCode}")
    public String add(@PathVariable("envCode") String envCode,
                      ModelMap mmap) {
        //运行环境代码
        mmap.put("envCode", envCode);
        return "serialNumber/add";
    }


    /**
     * 保存一个全编号生成器配置
     *
     * @param serialNumberConfig 编号生成器配置
     * @return 保存后的编号生成器配置
     */
    @ResponseBody
    @PostMapping("/add")
    public OperateResultVo save(SerialNumberConfig serialNumberConfig) {
        return new OperateResultVo(service.save(serialNumberConfig));
    }

    /**
     * 修改编号生成器
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("serialNumberConfig", service.findOne(id));
        return "serialNumber/edit";
    }

    /**
     * 修改运行环境
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(SerialNumberConfig serialNumberConfig) {
        return new OperateResultVo(service.save(serialNumberConfig));
    }


    /**
     * 删除一个编号生成器配置
     *
     * @param ids 编号生成器配置的Id标识
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.delete(ids));
    }

    /**
     * 清除编号生成器配置缓存
     *
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/clearConfigCache")
    public OperateResultVo clearConfigCache(String ids) {
        return new OperateResultVo(service.clearConfigCache(ids));
    }

    /**
     * 获取当前行编号
     *
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/getConfigKey")
    public OperateResultVo getConfigKey(String ids) {
        String initNumber = service.initNumber(ids);
        return new OperateResultVo(StringUtils.isNoneBlank(initNumber) ? "初始化编码成功:"+initNumber :
                "初始化编码失败:"+initNumber, true);
    }

}
