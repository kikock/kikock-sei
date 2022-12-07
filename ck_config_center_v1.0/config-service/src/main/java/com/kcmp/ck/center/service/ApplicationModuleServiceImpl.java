package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.ApplicationModuleService;
import com.kcmp.ck.center.dao.ApplicationModuleDao;
import com.kcmp.ck.center.dao.ApplicationServiceDao;
import com.kcmp.ck.center.dao.PlatformDao;
import com.kcmp.ck.center.dao.RuntimeEnvironmentDao;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import jodd.typeconverter.Convert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by kikock
 * 应用模块服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class ApplicationModuleServiceImpl implements ApplicationModuleService {

    @Autowired
    private ApplicationModuleDao dao;

    @Autowired
    private ApplicationServiceDao applicationServiceDao;

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private RuntimeEnvironmentDao environmentDao;

    /**
     * 获取应用模块
     * 允许platformCode为空，当为空时不按该条件查询
     *
     * @param platformCode 平台代码
     */
    @Override
    public Map<String, String> getAppModules(String platformCode) {
        Map<String, String> map = new HashMap<>();
        List<ApplicationModule> modules = dao.findAll();
        if (modules != null && modules.size() > 0) {
            Platform platform;
            for (ApplicationModule module : modules) {
                platform = module.getPlatform();
                if (StringUtils.isBlank(platformCode) ||
                        (platform != null && StringUtils.equalsIgnoreCase(platformCode, platform.getCode()))) {
                    map.put(module.getCode(), module.getName());
                }
            }
        }
        return map;
    }

    /**
     * 获取所有数据
     *
     * @return 应用模块清单
     */
    @Override
    public List<ApplicationModule> findAll() {
        return dao.findAll();
    }

    @Override
    public List<ApplicationModule> getPlatformModule(String platformId) {
        return dao.findByPlatformId(platformId);
    }

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 应用模块
     */
    @Override
    public ApplicationModule findOne(String id) {
        Optional<ApplicationModule> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存一个应用模块
     *
     * @param applicationModule 应用模块
     * @return 操作结果
     */
    @Override
    public OperateResult save(ApplicationModule applicationModule) {

        ApplicationModule entity = dao.findFirstByPlatformIdAndCode(applicationModule.getPlatformId(), applicationModule.getCode());
        if (Objects.nonNull(entity) && StringUtils.isBlank(applicationModule.getId()) ) {
            return new OperateResult(OperateStatusEnum.ERROR, "应用模块代码已经存在!");
        }
        ApplicationModule appModule = dao.save(applicationModule);

        return new OperateResult(appModule.getName() + "保存成功！");
    }

    /**
     * 删除一个应用模块
     *
     * @param ids 应用模块Id
     * @return 操作结果
     */
    @Override
    public OperateResult delete(String ids) {

        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            ApplicationModule entity = findOne(id);
            if (Objects.isNull(entity)) {
                return new OperateResult(OperateStatusEnum.ERROR, "要删除的应用模块不存在!");
            }
            //检查应用服务是否存在
            if (Objects.nonNull(applicationServiceDao.findFirstByApplicationModuleId(entity.getId()))) {
                return new OperateResult(OperateStatusEnum.ERROR, entity.getName() + "已经配置了应用服务，禁止删除!");
            }
            names.add(entity.getName());
            dao.deleteById(id);
        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]删除成功！");
    }
}
