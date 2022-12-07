package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.GlobalParamConfigService;
import com.kcmp.ck.center.dao.GlobalParamConfigDao;
import com.kcmp.ck.config.entity.GlobalParamConfig;
import com.kcmp.ck.config.entity.dto.GlobalParamConfigSearch;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.dto.ParamConfigVo;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by kikock
 * 全局参数配置服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class GlobalParamConfigServiceImpl implements GlobalParamConfigService {

    @Autowired
    private GlobalParamConfigDao dao;

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 全局参数配置
     */
    @Override
    public GlobalParamConfig findOne(String id) {
        Optional<GlobalParamConfig> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 通过查询参数获取全局参数配置清单
     *
     * @param search 查询参数
     * @return 全局参数配置清单
     */
    @Override
    public List<GlobalParamConfig> findBySearch(GlobalParamConfigSearch search) {
        List<GlobalParamConfig> globalParamConfigs;
        String platformId = search.getPlatformId();
        String applicationModuleId = search.getModuleId();
        //判断是否为平台级配置
        if (search.isPlatformParam()) {
            globalParamConfigs = dao.findAllByPlatformIdAndApplicationModuleId(platformId, null);
        } else if (StringUtils.isBlank(applicationModuleId)) {
            globalParamConfigs = dao.findAllByPlatformIdAndApplicationModuleIdIsNotNull(platformId);
        } else {
            globalParamConfigs = dao.findAllByPlatformIdAndApplicationModuleId(platformId, applicationModuleId);
        }
        return globalParamConfigs;
    }

    /**
     * 保存一个全局参数配置
     *
     * @param globalParamConfig 全局参数配置
     * @return 操作结果
     */
    @Override
    public OperateResult save(GlobalParamConfig globalParamConfig) {
        // 检查配置键是否重复
        String platId = globalParamConfig.getPlatformId();
        String moduleId = globalParamConfig.getApplicationModuleId();
        String paramKey = globalParamConfig.getParamKey();
        GlobalParamConfig existConfig = dao.findFirstByPlatformIdAndApplicationModuleIdAndParamKey(platId, moduleId, paramKey);
        if (Objects.nonNull(existConfig) && StringUtils.isNotBlank(existConfig.getId())) {
            if (!existConfig.getId().equals(globalParamConfig.getId())) {
                return new OperateResult(OperateStatusEnum.ERROR, "配置键【" + paramKey + "】在平台中已经存在！");
            }
        }
        GlobalParamConfig entity = dao.save(globalParamConfig);
        return new OperateResult(entity.getRemark() + "保存成功！");
    }

    /**
     * 删除一个全局参数配置
     *
     * @param id 全局参数配置Id
     * @return 操作结果
     */
    @Override
    public OperateResult delete(String id) {
        GlobalParamConfig entity = findOne(id);
        if (Objects.isNull(entity)) {
            return new OperateResult(OperateStatusEnum.ERROR, "要删除的全局参数配置不存在!");
        }
        dao.deleteById(id);
        return new OperateResult(entity.getRemark() + "删除成功！");
    }

    /**
     * 获取平台中可以引用的全局参数键和说明
     *
     * @param platformId 平台Id
     * @return 参数键和说明
     */
    @Override
    public List<ParamConfigVo> getCanReferenceParams(String platformId) {
        return dao.getCanReferenceParams(platformId);
    }
}
