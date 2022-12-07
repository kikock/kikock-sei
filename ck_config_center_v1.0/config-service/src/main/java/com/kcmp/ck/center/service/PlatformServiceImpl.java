package com.kcmp.ck.center.service;

import com.kcmp.ck.center.api.PlatformService;
import com.kcmp.ck.center.dao.ApplicationModuleDao;
import com.kcmp.ck.center.dao.PlatformDao;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import jodd.typeconverter.Convert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kikock
 * 平台服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformDao dao;

    @Autowired
    private ApplicationModuleDao applicationModuleDao;

    /**
     * 获取所有数据
     *
     * @return 平台清单
     */
    @Override
    public List<Platform> findAll() {
        List<Platform> platforms = dao.findAll();
        if (platforms.isEmpty()) {
            return platforms;
        }
        return platforms.stream().sorted(Comparator.comparing(Platform :: getRank)).collect(Collectors.toList());
    }


    /**
     * 获取平台名称搜索
     *
     * @return 平台清单
     */
    @Override
    public List<Platform> findAllByName(String name) {
        List<Platform> platforms =new ArrayList<>();
        if(StringUtils.isBlank(name)) {
           platforms = dao.findAll();
        }else {
            String sql ="%"+name+"%";
            platforms = dao.findByNameIsLike(sql);
        }
        if (CollectionUtils.isEmpty(platforms)) {
            return platforms;
        }
        return platforms.stream().sorted(Comparator.comparing(Platform :: getRank)).collect(Collectors.toList());
    }


    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 平台
     */
    @Override
    public Platform findOne(String id) {
        Optional<Platform> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存一个平台
     *
     * @param platform 平台
     * @return 操作结果
     */
    @Override
    public OperateResult save(Platform platform) {

        if(Objects.isNull(platform)){
            return new OperateResult(OperateStatusEnum.ERROR,"传入的平台参数不能为空!!");
        }
        String code = platform.getCode();
        if(StringUtils.isBlank(code)){
            return new OperateResult(OperateStatusEnum.ERROR,"code不能为空!!");
        }
        // 判断 是否code存在
        Platform byCode = dao.findByCode(code);
        if(Objects.nonNull(byCode) && StringUtils.isBlank(platform.getId())){
            return new OperateResult(OperateStatusEnum.ERROR,"code已经存在!!");
        }
        Platform entity = dao.save(platform);
        return new OperateResult(entity.getName() + "保存成功！");
    }

    /**
     * 删除一个平台
     *
     * @param ids 平台Id
     * @return 操作结果
     */
    @Override
    public OperateResult delete(String ids) {
        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            Platform entity = findOne(id);
            if (Objects.isNull(entity)) {
                return new OperateResult(OperateStatusEnum.ERROR, "要删除的平台不存在!");
            }
            //检查应用模块是否存在
            if (Objects.nonNull(applicationModuleDao.findFirstByPlatformId(entity.getId()))) {
                return new OperateResult(OperateStatusEnum.ERROR, entity.getName() + "已经配置了应用模块，禁止删除!");
            }
            names.add(entity.getName());
            dao.deleteById(id);
        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]删除成功！");
    }
}
