package com.kcmp.ck.center.controller;

import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.util.TableDataInfo;
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
 * 平台控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/platform")
public class PlatformController {


    @Autowired
    private PlatformService platformService;


    // 平台页面
    @RequestMapping("/show")
    public String show() {
        return "platform/platformView";
    }


    /**
     * 新增平台页面
     */
    @GetMapping("/add")
    public String add() {
        return "platform/add";
    }

    /**
     * 保存一个平台
     *
     * @param platform 平台
     * @return 保存结果
     */
    @ResponseBody
    @PostMapping("/add")
    public OperateResultVo addSave(Platform platform) {
        return new OperateResultVo(platformService.save(platform));
    }

    /**
     * 修改平台
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("platform", platformService.findOne(id));
        return "platform/edit";
    }

    /**
     * 修改保存平台
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(Platform platform) {
        return new OperateResultVo(platformService.save(platform));
    }


    /**
     * 获取平台清单
     *
     * @return 平台清单
     */
    @ResponseBody
    @RequestMapping("/listAllPlatform")
    public TableDataInfo listAllPlatform(String name) {
        List<Platform> platformList = platformService.findAllByName(name);
        return new TableDataInfo(platformList, platformList.size());
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
        return new OperateResultVo(platformService.delete(ids));
    }
}
