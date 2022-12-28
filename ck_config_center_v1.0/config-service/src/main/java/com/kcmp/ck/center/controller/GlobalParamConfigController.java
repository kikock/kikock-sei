package com.kcmp.ck.center.controller;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.kcmp.ck.center.api.ApplicationModuleService;
import com.kcmp.ck.center.api.GlobalParamConfigService;
import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.util.TableDataInfo;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.GlobalParamConfigSearch;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.vo.OperateResultVo;
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
 * 全局参数控制器
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Controller
@RequestMapping("/web/globalParamConfig")
public class GlobalParamConfigController {
    @Autowired
    private PlatformService platformService;

    @Autowired
    private GlobalParamConfigService service;

    @Autowired
    private ApplicationModuleService applicationModuleService;

    @RequestMapping("/show")
    public String show(ModelMap mmap) {
        List<Platform> all = platformService.findAll();
        mmap.put("platforms", all);
        return "globalParamConfig/globalParamConfigView";
    }

    /**
     * 新增应用模块页面
     */
    @GetMapping("/add/{platformId}/{platformParam}")
    public String add(@PathVariable("platformId") String platformId ,@PathVariable("platformParam") boolean platformParam ,
                      ModelMap mmap) {
        mmap.put("platformId",platformId);
        mmap.put("platformParam",platformParam);
        if(!platformParam){
           //模块参数
            List<ApplicationModule> modules = applicationModuleService.getPlatformModule(platformId);
            mmap.put("modules",modules);
        }
        return "globalParamConfig/add";
    }
    /**
     * 通过查询参数获取全局参数配置清单
     * @param search 查询参数
     * @return 全局参数配置清单
     */
    @ResponseBody
    @RequestMapping("/listAllBySearch")
    public TableDataInfo listAllBySearch(GlobalParamConfigSearch search){
        List<GlobalParamConfig> globalParamConfigs =  service.findBySearch(search);
        return new TableDataInfo(globalParamConfigs, globalParamConfigs.size());
    }

    /**
     * 保存一个全局参数
     * @param globalParamConfig 全局参数
     * @return 保存结果
     */
    @ResponseBody
    @RequestMapping("/add")
    public OperateResultVo save(GlobalParamConfig globalParamConfig) {
        return new OperateResultVo(service.save(globalParamConfig));
    }



    /**
     * 删除一个全局参数
     * @param ids 全局参数的Id标识
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping("/remove")
    public OperateResultVo delete(String ids) {
        return new OperateResultVo(service.remove(ids));
    }
    /**
     * 修改运行环境
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        GlobalParamConfig one = service.findOne(id);
        mmap.put("globalParamConfig", one);
        mmap.put("platformParam",true);
        if(StringUtils.isNotBlank(one.getApplicationModuleId())) {
            //模块参数
            List<ApplicationModule> modules = applicationModuleService.getPlatformModule(one.getPlatformId());
            mmap.put("platformParam",false);
            mmap.put("modules", modules);
        }
        return "globalParamConfig/edit";
    }

    /**mmap = {BindingAwareModelMap@20495}  size = 1
     * 修改运行环境
     */

    @PostMapping("/edit")
    @ResponseBody
    public OperateResultVo editSave(GlobalParamConfig globalParamConfig) {
        return new OperateResultVo(service.save(globalParamConfig));
    }

    //
    // /**
    //  * 获取平台中可以引用的全局参数键和说明
    //  * @param platformId 平台Id
    //  * @return 参数键和说明
    //  */
    // @ResponseBody
    // @RequestMapping("/getCanReferenceParams")
    // public List<ParamConfigVo> getCanReferenceParams(String platformId){
    //     GlobalParamConfigService client = JAXRSClientFactory.create(ServletApplication.apiBaseAddress, GlobalParamConfigService.class, Arrays.asList(new JacksonJsonProvider()));
    //     return client.getCanReferenceParams(platformId);
    // }
}


