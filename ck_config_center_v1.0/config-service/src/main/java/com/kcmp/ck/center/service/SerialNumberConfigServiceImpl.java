package com.kcmp.ck.center.service;


import com.kcmp.ck.center.api.SerialNumberConfigService;
import com.kcmp.ck.center.dao.SerialNumberConfigDao;
import com.kcmp.ck.config.entity.SerialNumberConfig;
import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import com.kcmp.ck.config.entity.vo.FetchNumberResult;
import com.kcmp.ck.config.exception.ServiceException;
import com.kcmp.ck.config.util.IdGenerator;
import com.kcmp.ck.config.util.JsonUtils;
import jodd.typeconverter.Convert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by kikock
 * 编号生成器配置服务逻辑实现
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class SerialNumberConfigServiceImpl implements SerialNumberConfigService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${kafka.topic.serialnumber}")
    private String kafkaTopic;

    private final static String SERIAL_CACHE_PREFIX = "SerialNumberConfig";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SerialNumberConfigDao dao;

    @Autowired
    private KafkaProducer producer;

    /**
     * 获取运行环境的所有编号生成器
     *
     * @param envCode 运行环境代码
     * @return 编号生成器配置清单
     */
    @Override
    public List<SerialNumberConfig> findByEnvCode(String envCode) {
        return dao.findByEnvCode(envCode);
    }

    @Override
    public List<SerialNumberConfig> findAllByEnvCodeAndName(String envCode, String name) {
        return dao.findAllByEnvCodeAndName(envCode,name);
    }

    /**
     * 通过Id获取实体
     *
     * @param id Id标识
     * @return 编号生成器配置
     */
    @Override
    public SerialNumberConfig findOne(String id) {
        Optional<SerialNumberConfig> entity = dao.findById(id);
        return entity.orElse(null);
    }

    /**
     * 保存一个编号生成器配置
     *
     * @param serialNumberConfig 编号生成器配置
     * @return 操作结果
     */
    @Override
    public OperateResult save(SerialNumberConfig serialNumberConfig) {
        String id = serialNumberConfig.getId();
        if (serialNumberConfig.isNew()) {
            if (StringUtils.isBlank(id)) {
                id = IdGenerator.uuid();
            }
        }
        if (dao.existsByEnvCodeAndEntityClassNameAndIsolationCodeAndIdNot(serialNumberConfig.getEnvCode(), serialNumberConfig.getEntityClassName(), serialNumberConfig.getIsolationCode(), id)) {
            return new OperateResult(OperateStatusEnum.ERROR, "存在相同配置，保存失败！");
        }
        SerialNumberConfig entity = dao.save(serialNumberConfig);
        //清除配置缓存
        clearConfigCache(entity);
        return new OperateResult(entity.getName() + "保存成功！");
    }

    /**
     * 删除一个编号生成器配置
     *
     * @param ids 编号生成器配置Ids
     * @return 操作结果
     */
    @Override
    public OperateResult delete(String ids) {
        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            SerialNumberConfig entity = findOne(id);
            if (Objects.isNull(entity)) {
                return new OperateResult(OperateStatusEnum.ERROR, "要删除的编号生成器配置不存在!");
            }
            names.add(entity.getName());
            dao.deleteById(id);
        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]删除成功！");
    }

    /**
     * 获取一个序列编号(含隔离码，可以初始化)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @param isInit          是否初始化
     * @return 序列编号
     */
    private String getNumber(String envCode, String entityClassName, String isolationCode, boolean isInit) {
        //分布式锁
        RedisLock lock = new RedisLock(redisTemplate, kafkaTopic, 10000, 20000);
        FetchNumberResult fetchNumberResult = new FetchNumberResult();
        fetchNumberResult.setKey(getConfigKey(entityClassName, isolationCode));
        try {
            if (lock.lock()) {
                getAndSetNumber(fetchNumberResult, envCode, entityClassName, isolationCode, isInit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取序列编号异常！", e);

        } finally {
            lock.unlock();
        }
        if (!StringUtils.isBlank(fetchNumberResult.getCurrentNumber()) &&
                Objects.nonNull(fetchNumberResult.getSerialNumberConfig())) {
            //发送新值到消息队列
            String message = JsonUtils.toJson(fetchNumberResult.getSerialNumberConfig());
            producer.send(fetchNumberResult.getKey(), message);
        }
        return fetchNumberResult.getCurrentNumber();
    }

    /**
     * 获取一个序列编号(无隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    @Override
    public String getNumber(String envCode, String entityClassName) {
        return getNumberWithIsolation(envCode, entityClassName, null);
    }

    /**
     * 获取一个序列编号(有隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    @Override
    public String getNumberWithIsolation(String envCode, String entityClassName, String isolationCode) {
        return getNumber(envCode, entityClassName, isolationCode, false);
    }

    /**
     * 获取一个序列编号并初始化(无隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    @Override
    public String getNumberAndInit(String envCode, String entityClassName) {
        return getNumber(envCode, entityClassName, null, true);
    }

    /**
     * 获取一个序列编号并初始化(有隔离码)
     *
     * @param envCode         运行环境代码
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    @Override
    public String getNumberWithIsolationAndInit(String envCode, String entityClassName, String isolationCode) {
        return getNumber(envCode, entityClassName, isolationCode, true);
    }

    @Override
    public String initNumber(String id) {
        SerialNumberConfig one = findOne(id);
        if(Objects.isNull(one)){
            return  "当前id不存在";
        }
        String currentNumber = getNumberWithIsolationAndInit(
                one.getEnvCode(),
                one.getEntityClassName(),
                StringUtils.isNoneBlank(one.getIsolationCode()) ? one.getIsolationCode() : null);
        //获取到序号
        one.setCurrentNumber(currentNumber);
        String currentSerial = StringUtils.isNoneBlank(one.getPrefixes()) ?
                currentNumber.replaceAll(one.getPrefixes(),"") : currentNumber ;
        one.setCurrentSerial(Integer.parseInt(currentSerial));
        one.setInitialSerial(Integer.parseInt(currentSerial));
        dao.save(one);
        return  currentNumber;
    }

    /**
     * 获取缓存键
     *
     * @param entityClassName 实体类名
     * @param isolationCode   隔离码
     * @return 缓存键
     */
    private String getConfigKey(String entityClassName, String isolationCode) {
        if (StringUtils.isBlank(isolationCode)) {
            return String.format("%s_%s", SERIAL_CACHE_PREFIX, entityClassName);
        }
        return String.format("%s_%s_%s", SERIAL_CACHE_PREFIX, entityClassName, isolationCode);
    }

    /**
     * 从数据库获取编号生成器配置
     *
     * @param envCode         运行环境代码
     * @param entityClassName 实体类名
     * @param isolationCode   隔离码
     * @return 编号生成器配置
     */
    private SerialNumberConfig getSerialNumberConfigFromDb(String envCode, String entityClassName, String isolationCode) {
        SerialNumberConfig numberConfig;
        if (StringUtils.isBlank(isolationCode)) {
            numberConfig = dao.findFirstByEnvCodeAndEntityClassName(envCode, entityClassName);
        } else {
            numberConfig = dao.findFirstByEnvCodeAndEntityClassNameAndIsolationCode(envCode, entityClassName, isolationCode);
            if (Objects.isNull(numberConfig)) {
                numberConfig = dao.findFirstByEnvCodeAndEntityClassName(envCode, entityClassName);
            }
        }
        return numberConfig;
    }

    /**
     * 生成补足前导0的编号
     *
     * @param serial 序号
     * @param length 长度
     * @return 编号
     */
    private String addZeroForNumber(int serial, int length) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(length);
        numberFormat.setMaximumIntegerDigits(length);
        return numberFormat.format(serial);
    }

    /**
     * 获取并设置新的序列编号
     */
    private void getAndSetNumber(FetchNumberResult result, String envCode, String entityClassName, String isolationCode, boolean isInit) {
        //先从缓存获取配置
        String key = result.getKey();
        SerialNumberConfig numberConfig = JsonUtils.fromJson(redisTemplate.opsForValue().get(key), SerialNumberConfig.class);
        if (numberConfig == null) {
            //从数据库获取配置
            numberConfig = getSerialNumberConfigFromDb(envCode, entityClassName, isolationCode);
            if (numberConfig == null) {
                result.setCurrentNumber(null);
                log.error("从数据库获取配置！", new ServiceException("系统没有配置类型【" + entityClassName + "】的编号生成器！"));
                return;
            }
        }
        //获取当前序号
        int currentSerial;
        String currentNumber;
        if (isInit || numberConfig.getCurrentSerial() == 0) {
            currentSerial = numberConfig.getInitialSerial();
        } else {
            currentSerial = numberConfig.getCurrentSerial();
        }
        //增加序号
        currentSerial += 1;
        //设置新的当前序号
        numberConfig.setCurrentSerial(currentSerial);
        String prefixes = numberConfig.getPrefixes();
        String numberPrefixes = StringUtils.isBlank(prefixes) ? "" : prefixes.trim();
        //构造新的编号
        if (numberConfig.getNoLeadingZero()) {
            currentNumber = String.format("%s%s", numberPrefixes, addZeroForNumber(currentSerial, numberConfig.getLength()));
        } else {
            currentNumber = String.format("%s%d", numberPrefixes, currentSerial);
        }
        //设置新的当前编号
        numberConfig.setCurrentNumber(currentNumber);
        //将新值写入缓存
        redisTemplate.opsForValue().set(key, JsonUtils.toJson(numberConfig));
        result.setKey(key);
        result.setSerialNumberConfig(numberConfig);
        result.setCurrentNumber(currentNumber);
    }

    /**
     * 清除编号生成器配置缓存
     *
     * @param ids 编号生成器配置Id
     * @return 操作结果
     */
    @Override
    public OperateResult clearConfigCache(String ids) {
        String[] array = Convert.toStringArray(ids);
        List<String> names =new ArrayList<>();
        for (String  id: array) {
            SerialNumberConfig numberConfig = findOne(id);
            clearConfigCache(numberConfig);
            names.add(numberConfig.getName());
            dao.deleteById(id);
        }
        String name = String.join(",", names);
        return new OperateResult("["+name+"]编号生成器配置缓存清除成功！");
    }

    /**
     * 清除编号生成器配置缓存
     */
    void clearConfigCache() {
        // 清除带有隔离码的编号生成器配置缓存
        String pattern = SERIAL_CACHE_PREFIX + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 清除编号生成器配置缓存
     *
     * @param numberConfig 编号生成器配置
     */
    private void clearConfigCache(SerialNumberConfig numberConfig) {
        if (Objects.nonNull(numberConfig)) {
            String isolationCode = numberConfig.getIsolationCode();
            String key = getConfigKey(numberConfig.getEntityClassName(), numberConfig.getIsolationCode());
            if (StringUtils.isBlank(isolationCode)) {
                // 清除带有隔离码的编号生成器配置缓存
                String pattern = key + "*";
                Set<String> keys = redisTemplate.keys(pattern);
                if (!keys.isEmpty()) {
                    redisTemplate.delete(keys);
                }
            } else {
                redisTemplate.delete(key);
            }
        }
    }
}
