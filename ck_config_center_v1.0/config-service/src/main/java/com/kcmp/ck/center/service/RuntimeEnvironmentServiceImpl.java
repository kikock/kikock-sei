package com.kcmp.ck.center.service;


import com.kcmp.ck.center.api.RuntimeEnvironmentService;
import com.kcmp.ck.center.dao.ApplicationServiceDao;
import com.kcmp.ck.center.dao.RuntimeEnvironmentDao;
import com.kcmp.ck.config.entity.ApplicationModule;
import com.kcmp.ck.config.entity.Platform;
import com.kcmp.ck.config.entity.RuntimeEnvironment;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import jodd.typeconverter.Convert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kikock
 * 运行环境服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class RuntimeEnvironmentServiceImpl implements RuntimeEnvironmentService {
    @Autowired
    private RuntimeEnvironmentDao dao;
    @Autowired
    private ApplicationServiceDao applicationServiceDao;
    @Autowired
    private EnvironmentVariableServiceImpl environmentVariableService;

    /**
     * 获取所有数据
     *
     * @return 运行环境清单
     */
    @Override
    public List<RuntimeEnvironment> findAll() {
        List<RuntimeEnvironment> runtimeEnvironments = dao.findAll();
        if (runtimeEnvironments.isEmpty()) {
            return runtimeEnvironments;
        }
        return runtimeEnvironments.stream().sorted(Comparator.comparing(RuntimeEnvironment :: getRank)).collect(Collectors.toList());
    }

    @Override
    public List<RuntimeEnvironment> findByNameAll(String name) {
        List<RuntimeEnvironment> runtimeEnvironments =new ArrayList<>();
        if(StringUtils.isBlank(name)) {
            runtimeEnvironments = dao.findAll();
        }else {
            String sql ="%"+name+"%";
            runtimeEnvironments = dao.findByNameIsLike(sql);
        }
        if (CollectionUtils.isEmpty(runtimeEnvironments)) {
            return runtimeEnvironments;
        }
        return runtimeEnvironments.stream().sorted(Comparator.comparing(RuntimeEnvironment :: getRank)).collect(Collectors.toList());
    }




    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 运行环境
     */
    @Override
    public RuntimeEnvironment findOne(String id) {
        Optional<RuntimeEnvironment> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存一个运行环境
     *
     * @param runtimeEnvironment 运行环境
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult save(RuntimeEnvironment runtimeEnvironment) {
        boolean isNew = runtimeEnvironment.isNew();
        RuntimeEnvironment entity = dao.save(runtimeEnvironment);
        //级联创建所有环境变量
        if (isNew) {
            environmentVariableService.saveAllForEnvironment(runtimeEnvironment);
        }
        return new OperateResult(entity.getName() + "保存成功！");
    }

    /**
     * 删除一个运行环境
     *
     * @param ids 运行环境Ids 集合
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult delete(String ids) {

        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            RuntimeEnvironment entity = findOne(ids);
            if (Objects.isNull(entity)) {
                return new OperateResult(OperateStatusEnum.ERROR, "要删除的运行环境不存在!");
            }
            //检查应用服务是否存在
            if (Objects.nonNull(applicationServiceDao.findFirstByRuntimeEnvironmentId(entity.getId()))) {
                return new OperateResult(OperateStatusEnum.ERROR, entity.getName() + "已经配置了应用服务，禁止删除!");
            }
            names.add(entity.getName());
            //级联删除环境变量配置值
            environmentVariableService.deleteAllForEnvironment(id);
            dao.deleteById(id);

        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]删除成功！");
    }
}
