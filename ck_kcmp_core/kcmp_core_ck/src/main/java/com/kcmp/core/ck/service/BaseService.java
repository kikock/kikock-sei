package com.kcmp.core.ck.service;

import com.google.common.collect.Lists;
import com.kcmp.ck.context.ConfigConstants;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.util.ApiClient;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.ResponseData;
import com.kcmp.core.ck.dao.BaseDao;
import com.kcmp.core.ck.entity.IDataDict;
import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.core.ck.entity.auth.IDataAuthEntity;
import com.kcmp.core.ck.entity.vo.DataDictVo;
import com.kcmp.core.ck.search.PageResult;
import com.kcmp.core.ck.search.Search;
import com.kcmp.core.ck.search.SearchFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.GenericType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by kikock
 * 业务实体API基础服务实现类
 * @email kikock@qq.com
 **/
public abstract class BaseService<T extends Persistable<ID> & Serializable, ID extends Serializable> {

    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    protected abstract BaseDao<T, ID> getDao();

    //注入缓存模板
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * @param entity 待创建数据对象
     */
    protected OperateResultWithData<T> preInsert(T entity) {
        return OperateResultWithData.operationSuccessWithData(entity, "kcmp_service_00003");
    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * @param entity 待更新数据对象
     */
    protected OperateResultWithData<T> preUpdate(T entity) {
        return OperateResultWithData.operationSuccessWithData(entity, "kcmp_service_00003");
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     * @param id 待删除数据对象主键
     */
    protected OperateResult preDelete(ID id) {
        return OperateResult.operationSuccess("kcmp_service_00003");
    }

    /**
     * 数据保存操作
     */
    @SuppressWarnings("unchecked")
    public OperateResultWithData<T> save(T entity) {
        Validation.notNull(entity, "持久化对象不能为空");
        OperateResultWithData<T> operateResultWithData;
        boolean isNew = isNew(entity);
        if (isNew) {
            // 创建前设置租户代码
            if (entity instanceof ITenant) {
                ITenant tenantEntity = (ITenant) entity;
                if (StringUtils.isBlank(tenantEntity.getTenantCode())) {
                    tenantEntity.setTenantCode(ContextUtil.getTenantCode());
                }
            }
            operateResultWithData = preInsert(entity);
        } else {
            operateResultWithData = preUpdate(entity);
        }
        if (Objects.isNull(operateResultWithData) || operateResultWithData.successful()) {
            T saveEntity = getDao().save(entity);
            if (logger.isDebugEnabled()) {
                logger.debug("Saved entity id is {}", entity.getId());
            }
            if (isNew) {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "kcmp_service_00001");
            } else {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "kcmp_service_00002");
            }
        }
        return operateResultWithData;
    }

    /**
     * 批量数据保存操作 其实现只是简单循环集合每个元素调用 {@link #save(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * @param entities 待批量操作数据集合
     */
    public void save(Collection<T> entities) {
        if (entities != null && entities.size() > 0) {
//            for (T entity : entities) {
//                save(entity);
//            }
            getDao().save(entities);
        }
    }

    /**
     * 基于主键集合查询集合数据对象
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getDao().findAll();
    }

    /**
     * 基于主键查询单一数据对象
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        Validation.notNull(id, "主键不能为空");
        return getDao().findOne(id);
    }

    /**
     * 根据id查询是否存在，存在则返回false，不存在返回true表示新值
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isNew(T entity) {
        Validation.notNull(entity, "不能为空");
        boolean isNew = true;
        ID id = entity.getId();
        if (Objects.nonNull(id) && StringUtils.isNotBlank(String.valueOf(id))) {
            //findOne有租户隔离，不适用于后台服务无租户的场景，因此注释
//            T origin = findOne(id);
//            if (Objects.nonNull(origin)) {
            isNew = false;
//            }
        }
        return isNew;
    }

    /**
     * 基于主键集合查询集合数据对象
     * @param ids 主键集合
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public List<T> findByIds(final Collection<ID> ids) {
        Validation.isTrue(ids != null, "必须提供有效查询主键集合");
        if (ids.size() > 0) {
            SearchFilter filter = new SearchFilter("id", ids, SearchFilter.Operator.IN);
            return findByFilter(filter);
        } else {
            return null;
        }
    }

    /**
     * 主键删除
     * @param id 主键
     * @return 返回操作结果对象
     */
    public OperateResult delete(final ID id) {
        OperateResult operateResult = preDelete(id);
        if (Objects.isNull(operateResult) || operateResult.successful()) {
            T entity = findOne(id);
            if (entity != null) {
                getDao().delete(entity);
                return OperateResult.operationSuccess("kcmp_service_00003");
            } else {
                return OperateResult.operationWarning("kcmp_service_00004");
            }
        }
        return operateResult;
    }

    /**
     * 批量数据删除操作 其实现只是简单循环集合每个元素调用
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * @param ids 待批量操作数据集合
     */
    public void delete(Collection<ID> ids) {
        if (ids != null && ids.size() > 0) {
            getDao().delete(ids);
        }
    }

    /**
     * 根据泛型对象属性和值查询集合对象
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     */
    @Transactional(readOnly = true)
    public List<T> findListByProperty(final String property, final Object value) {
        return getDao().findListByProperty(property, value);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    @Transactional(readOnly = true)
    public T findByProperty(final String property, final Object value) {
        return getDao().findByProperty(property, value);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    @Transactional(readOnly = true)
    public T findFirstByProperty(final String property, final Object value) {
        return getDao().findFirstByProperty(property, value);
    }

    /**
     * 根据业务对象属性的值判断业务实体是否存在
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回false，如果查询到一条或多条数据则返回true
     */
    @Transactional(readOnly = true)
    public boolean isExistsByProperty(String property, Object value) {
        return getDao().isExistsByProperty(property, value);
    }

    /**
     * 单一条件对象查询数据集合
     */
    @Transactional(readOnly = true)
    public List<T> findByFilter(SearchFilter searchFilter) {
        return getDao().findByFilter(searchFilter);
    }

    /**
     * 基于查询条件count记录数据
     */
    @Transactional(readOnly = true)
    public long count(Search searchConfig) {
        return getDao().count(searchConfig);
    }

    /**
     * 基于动态组合条件对象查询数据
     */
    @Transactional(readOnly = true)
    public T findOneByFilters(Search searchConfig) {
        return getDao().findOneByFilters(searchConfig);
    }

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(Search searchConfig) {
        return getDao().findByFilters(searchConfig);
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     */
    @Transactional(readOnly = true)
    public PageResult<T> findByPage(Search searchConfig) {
        return getDao().findByPage(searchConfig);
    }

    /**
     * 获取一般用户有权限的业务实体Id清单
     * @param featureCode 功能项代码
     * @param userId      用户Id
     * @return 业务实体Id清单
     */
    protected List<String> getNormalUserAuthorizedEntityIds(String featureCode, String userId) {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        List<String> entityIds = null;
        if (Objects.nonNull(redisTemplate)) {
            //--先从缓存中读取
            String catchKey = entityClass.getName() + "_" + featureCode + "_" + userId;
            Object catchResult = redisTemplate.opsForValue().get(catchKey);
            if (catchResult instanceof List) {
                List catchResultList = (List) catchResult;
                entityIds = new ArrayList<>();
                for (Object cache : catchResultList) {
                    if (cache instanceof String) {
                        entityIds.add((String) cache);
                    }
                }
            }
        }
        if (Objects.isNull(entityIds)) {
            //缓存不存在，调用API服务获取
            Map<String, Object> params = new HashMap<>();
            params.put("entityClassName", entityClass.getName());
            if (!StringUtils.isBlank(featureCode)) {
                params.put("featureCode", featureCode);
            }
            params.put("userId", userId);
            String path = "user/getNormalUserAuthorizedEntities";
            GenericType<List<String>> genericType = new GenericType<List<String>>() {
            };
            entityIds = ApiClient.getEntityViaProxy(ConfigConstants.BASIC_API, path, genericType, params);
        }
        return entityIds;
    }

    /**
     * 获取所有未冻结的业务实体
     * @return 业务实体清单
     */
    @Transactional(readOnly = true)
    public List<T> findAllUnfrozen() {
        return getDao().findAllUnfrozen();
    }


    /**
     * 根据字段类别代码，获取字典项目
     * @param categoryCode 字典类别代码
     * @return 返回当前类别下的字典项目
     */
    public ResponseData<List<IDataDict>> getDataDicts(String categoryCode) {
        ResponseData<List<IDataDict>> responseData = ResponseData.build();
        if (StringUtils.isBlank(categoryCode)) {
            return responseData.setSuccess(Boolean.FALSE).setMessage("字典类别代码不能为空");
        }

        List<IDataDict> dataDictVos = null;
        if (redisTemplate != null) {
            dataDictVos = getDataDictCache(categoryCode);
        }

        if (dataDictVos == null) {
            String path;
            Map<String, Object> params = new HashMap<>();
            params.put("categoryCode", categoryCode);
            path = "/dataDict/getDataDictItemsUnFrozen";
            GenericType<List<DataDictVo>> genericType = new GenericType<List<DataDictVo>>() {
            };
            try {
                List<DataDictVo> dataDicts = ApiClient.getEntityViaProxy(ConfigConstants.BASIC_API, path, genericType, params);
                dataDictVos = Lists.newArrayList(dataDicts);
            } catch (Exception e) {
                logger.warn("Failed to get [{}] from the DataDict", categoryCode);
            }
        }
        responseData.setData(dataDictVos);
        return responseData;
    }

    /**
     * 按字典类别代码和字典项目代码查询字典项目
     * @param categoryCode 字典类别代码
     * @param code         字典项目代码
     * @return 返回符合条件的字典项目
     */
    public ResponseData<IDataDict> getDataDictByCode(String categoryCode, String code) {
        ResponseData<IDataDict> result = ResponseData.build();
        if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(code)) {
            //字典类别[{0}],字典值[{1}]获取数据字典错误！
            result.setMessage(ContextUtil.getMessage("字典类别[{0}],字典代码[{1}]获取数据字典错误！", categoryCode, code));
            return result;
        }

        IDataDict dictVo = null;
        ResponseData<List<IDataDict>> responseData = getDataDicts(categoryCode);
        if (responseData.successful()) {
            List<IDataDict> dataDictVos = responseData.getData();
            if (CollectionUtils.isNotEmpty(dataDictVos)) {
                dictVo = dataDictVos.stream().filter(o -> StringUtils.equals(o.getCode(), code)).findAny().orElse(null);
            }
            result.setData(dictVo);
        } else {
            result.setSuccess(responseData.successful());
            result.setMessage(responseData.getMessage());
        }
        return result;
    }

    /**
     * 按字典类别代码和字典项目值查询字典项目
     * @param categoryCode 字典类别代码
     * @param value        字典项目值
     * @return 返回符合条件的字典项目
     */
    public ResponseData<IDataDict> getDataDictByValue(String categoryCode, String value) {
        ResponseData<IDataDict> result = ResponseData.build();
        if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(value)) {
            //字典类别[{0}],字典值[{1}]获取数据字典错误！
            result.setMessage(ContextUtil.getMessage("字典类别[{0}],字典值[{1}]获取数据字典错误！", categoryCode, value));
            return result;
        }

        IDataDict dictVo = null;
        ResponseData<List<IDataDict>> responseData = getDataDicts(categoryCode);
        if (responseData.successful()) {
            List<IDataDict> dataDictVos = responseData.getData();
            if (CollectionUtils.isNotEmpty(dataDictVos)) {
                dictVo = dataDictVos.stream().filter(o -> StringUtils.equals(o.getValue(), value)).findAny().orElse(null);
            }
            result.setData(dictVo);
        } else {
            result.setSuccess(responseData.successful());
            result.setMessage(responseData.getMessage());
        }
        return result;
    }

    /**
     * 按字典类别代码和字典项目值查询字典项目
     * @param categoryCode 字典类别代码
     * @param valueName    字典项目名称
     * @return 返回符合条件的字典项目
     */
    public ResponseData<IDataDict> getDataDictByValueName(String categoryCode, String valueName) {
        ResponseData<IDataDict> result = ResponseData.build();
        if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(valueName)) {
            //字典类别[{0}],字典值[{1}]获取数据字典错误！
            result.setMessage(ContextUtil.getMessage("字典类别[{0}],字典值描述[{1}]获取数据字典错误！", categoryCode, valueName));
            return result;
        }

        IDataDict dictVo = null;
        ResponseData<List<IDataDict>> responseData = getDataDicts(categoryCode);
        if (responseData.successful()) {
            List<IDataDict> dataDictVos = responseData.getData();
            if (CollectionUtils.isNotEmpty(dataDictVos)) {
                dictVo = dataDictVos.stream().filter(o -> StringUtils.equals(o.getValueName(), valueName)).findAny().orElse(null);
            }
            result.setData(dictVo);
        } else {
            result.setSuccess(responseData.successful());
            result.setMessage(responseData.getMessage());
        }
        return result;
    }

    /**
     * 增加数据字典缓存
     * @param categoryCode 数据字典类别
     * @param dataDicts    数据字典
     */
    @SuppressWarnings("unchecked")
    protected void putDataDictCache(String categoryCode, List<IDataDict> dataDicts) {
        if (Objects.nonNull(redisTemplate)) {
            BoundValueOperations operations = redisTemplate.boundValueOps("datadict:" + categoryCode);
            operations.set(dataDicts, 12, TimeUnit.HOURS);
        }
    }

    /**
     * 按数据字典类别移除数据字典缓存
     * @param categoryCode 字典类别
     */
    protected void removeDataDictCache(String categoryCode) {
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.delete("datadict:" + categoryCode);
        }
    }

    /**
     * 按数据字典类别获取数据字典缓存
     * @param categoryCode 字典类别
     */
    @SuppressWarnings("unchecked")
    private List<IDataDict> getDataDictCache(String categoryCode) {
        if (Objects.nonNull(redisTemplate)) {
            try {
                BoundValueOperations operations = redisTemplate.boundValueOps("datadict:" + categoryCode);
                Object obj = operations.get();
                if (obj instanceof List) {
                    return (List<IDataDict>) obj;
                }
            } catch (Exception ignored) {
                logger.warn("[{}] is not in the DataDict cache", categoryCode);
            }
        }
        return null;
    }

}
