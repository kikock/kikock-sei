package com.kcmp.core.ck.dao.impl;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.exception.DataOperationDeniedException;
import com.kcmp.ck.util.IdGenerator;
import com.kcmp.core.ck.dao.BaseDao;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.IAudiTable;
import com.kcmp.core.ck.entity.ICodeUnique;
import com.kcmp.core.ck.entity.IFrozen;
import com.kcmp.core.ck.entity.IRank;
import com.kcmp.core.ck.entity.ISoftDelete;
import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.core.ck.search.PageInfo;
import com.kcmp.core.ck.search.PageResult;
import com.kcmp.core.ck.search.Search;
import com.kcmp.core.ck.search.SearchFilter;
import com.kcmp.core.ck.search.SearchOrder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by kikock
 * JPA????????????
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public class BaseDaoImpl<T extends Persistable & Serializable, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

    protected final Class<T> domainClass;
    protected final EntityManager entityManager;

    /**
     * ???????????????
     * \\b  ?????? ??????????????????  ??????  select ?????????   1select???????????????
     **/
    private static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)";
    private static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    protected boolean isValid(String str) {
        if (sqlPattern.matcher(str).find()) {
            LOGGER.error("???????????????SQL??????????????????str=" + str);
            return false;
        }
        return true;
    }

    public BaseDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    /**
     * ?????????????????????
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        boolean isNew = preSave(entity);
        if (isNew) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public void save(Collection<T> entities) {
        if (entities != null && entities.size() > 0) {
            int i = 0;
            for (T entity : entities) {
                i++;
                boolean isNew = preSave(entity);
                if (isNew) {
                    entityManager.persist(entity);
                } else {
                    entityManager.merge(entity);
                }
                if (i % 50 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
    }

    protected boolean preSave(T entity) {
        Assert.notNull(entity, "????????????????????????????????????");

        boolean isNew;
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) entity;
            ID id = (ID) baseEntity.getId();
            //id????????????
            if (Objects.isNull(id)) {
                isNew = true;
                baseEntity.setId(IdGenerator.uuid());
            } else {
//                T origin = findOne(id);
//                isNew = Objects.isNull(origin);
                if (!existsById(id)) {
                    throw new DataOperationDeniedException(ContextUtil.getMessage("kcmp_service_00004", id));
                } else {
                    isNew = false;
                }
            }
        } else {
            isNew = Objects.isNull(entity.getId());
        }
        //??????????????????????????????
        if (entity instanceof IAudiTable) {
            Date now = new Date();
            String userId = ContextUtil.getUserId();
            String userAccount = ContextUtil.getUserAccount();
            String userName = ContextUtil.getUserName();
            IAudiTable audiTableEntity = (IAudiTable) entity;
            if (isNew) {
                //??????
                audiTableEntity.setCreatorId(userId);
                audiTableEntity.setCreatorName(userName);
                audiTableEntity.setCreatorAccount(userAccount);
                audiTableEntity.setCreatedDate(now);
            }
            audiTableEntity.setLastEditorId(userId);
            audiTableEntity.setLastEditorName(userName);
            audiTableEntity.setLastEditorAccount(userAccount);
            audiTableEntity.setLastEditedDate(now);
        }
        //?????????????????????(????????????????????????????????????)
        if (entity instanceof ITenant && StringUtils.isBlank(((ITenant) entity).getTenantCode())) {
            //?????????????????????????????????
            ITenant tenant = (ITenant) entity;
            tenant.setTenantCode(ContextUtil.getTenantCode());
        }
        return isNew;
    }

    /**
     * ??????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        Specification<T> spec = (root, query, builder) -> {
            Predicate predicate = null;
            //?????????
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                predicate = builder.and(builder.equal(root.get(ISoftDelete.DELETED), 0));
            }
            //??????
            if (ITenant.class.isAssignableFrom(domainClass)) {
                if (Objects.isNull(predicate)) {
                    predicate = builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()));
                } else {
                    predicate = builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()), predicate);
                }
            }
            return predicate;
        };
        return super.findAll(spec, sort);
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        return findById(id).orElse(null);
    }

    /**
     * ??????id??????????????????
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        T entity = null;
        if (Objects.nonNull(id)) {
            entity = super.findById(id).orElse(null);
            if (Objects.nonNull(entity) && ITenant.class.isAssignableFrom(domainClass)) {
                ITenant tenantEntity = (ITenant) entity;
                if (!StringUtils.equals(ContextUtil.getTenantCode(), tenantEntity.getTenantCode())) {
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evict() {
        entityManager.getEntityManagerFactory().getCache().evict(domainClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evict(ID id) {
        if (Objects.nonNull(id)) {
            entityManager.getEntityManagerFactory().getCache().evict(domainClass, id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evictAll() {
        entityManager.getEntityManagerFactory().getCache().evictAll();
    }

    /////////////////////////////////???????????????/////////////////////////////////////

    /**
     * ????????????????????????
     * @return ??????????????????
     */
    @Override
    public Class<T> getEntityClass() {
        return domainClass;
    }

    /**
     * ??????Id????????????????????????
     * @param id ????????????id??????
     */
    @Override
    @Transactional
    public void delete(ID id) {
        //?????????
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            T entity = findOne(id);
            if (Objects.nonNull(entity)) {
                ISoftDelete softDelete = (ISoftDelete) entity;
                //???????????????????????????
                softDelete.setDeleted(System.currentTimeMillis());
                //?????????
                save(entity);
            }
        } else {
            deleteById(id);
        }
    }

    /**
     * ??????Id????????????????????????
     * @param ids ????????????id??????
     */
    @Override
    @Transactional
    public void delete(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        List<T> entities = findAllById(ids);
        //?????????
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            for (T entity : entities) {
                ISoftDelete softDelete = (ISoftDelete) entity;
                //???????????????????????????
                softDelete.setDeleted(System.currentTimeMillis());
            }
            // ?????????
            save(entities);
        } else {
            deleteInBatch(entities);
        }
    }

    /**
     * ???????????????????????????????????????????????????(????????????????????????)
     *
     * @return ???????????????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllWithDelete() {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        Specification<T> spec = (root, query, builder) -> {
            Predicate predicate = null;
            //??????
            if (ITenant.class.isAssignableFrom(domainClass)) {
                predicate = builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()));
            }
            return predicate;
        };
        return super.findAll(spec, sort);
    }

    /**
     * ????????????????????????
     * @return ??????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAllUnfrozen() {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        Specification<T> spec = (root, query, builder) -> {
            Predicate predicate = null;
            //?????????
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                predicate = builder.and(builder.equal(root.get(ISoftDelete.DELETED), 0));
            }
            //??????
            if (IFrozen.class.isAssignableFrom(domainClass)) {
                if (Objects.isNull(predicate)) {
                    predicate = builder.and(builder.equal(root.get(IFrozen.FROZEN), false));
                } else {
                    predicate = builder.and(builder.equal(root.get(IFrozen.FROZEN), false), predicate);
                }
            }
            //??????
            if (ITenant.class.isAssignableFrom(domainClass)) {
                if (Objects.isNull(predicate)) {
                    predicate = builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()));
                } else {
                    predicate = builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()), predicate);
                }
            }
            return predicate;
        };
        return super.findAll(spec, sort);
    }

    /**
     * ????????????????????????????????????????????????
     * @param property ??????????????????????????????????????????
     * @param value    ?????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findListByProperty(final String property, final Object value) {
        Specification<T> spec = (root, query, builder) -> {
            SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);
            Predicate predicate = buildPredicate(property, searchFilter, root, query, builder, false);
            // ?????????
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                predicate = builder.and(builder.equal(root.get(ISoftDelete.DELETED), 0), predicate);
            }
            //  ????????????
            if (ITenant.class.isAssignableFrom(domainClass)) {
                return builder.and(builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode()), predicate);
            }
            return predicate;
        };

        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }

        return findAll(spec, sort);
    }

    /**
     * ????????????????????????????????????????????????
     * @param property ??????????????????????????????????????????
     * @param value    ?????????
     * @return ??????????????????null?????????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    public T findByProperty(final String property, final Object value) {
        List<T> entities = findListByProperty(property, value);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1, "?????????????????????");
            return entities.get(0);
        }
    }

    /**
     * ????????????????????????????????????????????????(??????)
     * @param property ??????????????????????????????????????????
     * @param value    ?????????
     * @return ??????????????????null????????????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public T findFirstByProperty(final String property, final Object value) {
        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }
        Pageable pageable = PageRequest.of(0, 1, sort);

        SearchFilter searchFilter = new SearchFilter(property, value, SearchFilter.Operator.EQ, null);

        Specification<T> spec = (root, query, builder) -> {
            Predicate predicate = buildPredicate(property, searchFilter, root, query, builder, false);
            if (ITenant.class.isAssignableFrom(domainClass)) {
                Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                predicate = builder.and(predicate, tenantCodePredicate);
            }
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                predicate = builder.and(predicate, builder.equal(root.get(ISoftDelete.DELETED), 0));
            }
            return predicate;
        };
        Page<T> page = findAll(spec, pageable);
        T result = null;
        List<T> list = page.getContent();
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.get(0);
        }
        return result;
    }

    /**
     * ????????????????????????????????????????????????????????????
     * @param property ??????????????????????????????????????????
     * @param value    ?????????
     * @return ??????????????????false????????????????????????????????????????????????true
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByProperty(String property, Object value) {
        return Objects.nonNull(findFirstByProperty(property, value));
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findByFilter(SearchFilter searchFilter) {
        List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        searchFilters.add(searchFilter);
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            searchFilters.add(new SearchFilter(ISoftDelete.DELETED, 0));
        }
        Search searchConfig = new Search(searchFilters, null, null);
        Specification<T> spec = buildSpecification(searchConfig);

        Sort sort = Sort.unsorted();
        if (IRank.class.isAssignableFrom(domainClass)) {
            sort = Sort.by(Sort.Direction.ASC, IRank.RANK);
        }
        return findAll(spec, sort);
    }

    /**
     * ??????????????????count????????????
     */
    @Override
    @Transactional(readOnly = true)
    public long count(Search searchConfig) {
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            searchConfig.addFilter(new SearchFilter(ISoftDelete.DELETED, 0));
        }
        Specification<T> spec = buildSpecification(searchConfig);
        return count(spec);
    }

    /**
     * ??????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public T findOneByFilters(Search searchConfig) {
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            searchConfig.addFilter(new SearchFilter(ISoftDelete.DELETED, 0));
        }
        Specification<T> spec = buildSpecification(searchConfig);
        Optional<T> optional = findOne(spec);
        return optional.orElse(null);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findByFilters(Search searchConfig) {
        Sort sort = buildSort(searchConfig);
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            searchConfig.addFilter(new SearchFilter(ISoftDelete.DELETED, 0));
        }
        Specification<T> spec = buildSpecification(searchConfig);
        return findAll(spec, sort);
    }

    /**
     * ???????????????????????????????????????(?????????)????????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<T> findByPage(Search searchConfig) {
        PageInfo pageInfo = searchConfig.getPageInfo();
        //Assert.isTrue(pageInfo != null, "??????????????????");
        if (Objects.isNull(pageInfo)) {
            pageInfo = new PageInfo();
            LOGGER.warn("?????????????????????????????????[{}]???", pageInfo.toString());
        }

        Sort sort = buildSort(searchConfig);
        Pageable pageable = PageRequest.of(pageInfo.getPage() - 1, pageInfo.getRows(), sort);
        if (ISoftDelete.class.isAssignableFrom(domainClass)) {
            searchConfig.addFilter(new SearchFilter(ISoftDelete.DELETED, 0));
        }
        Specification<T> specifications = buildSpecification(searchConfig);

        Page<T> page = findAll(specifications, pageable);
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setPage(page.getNumber() + 1);
        pageResult.setRecords(page.getTotalElements());
        pageResult.setTotal(page.getTotalPages());
        pageResult.setRows(page.getContent());
        return pageResult;
    }

    /**
     * ??????????????????????????????
     * @param code ??????
     * @param id   Id??????
     * @return ??????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCodeExists(String code, String id) {
        if (ICodeUnique.class.isAssignableFrom(domainClass)) {
            String queryStr;
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                queryStr = String.format("select t.id from %s t where t.deleted=0 and t.code=:code and t.id<>:id", domainClass.getSimpleName());
            } else {
                queryStr = String.format("select t.id from %s t where t.code=:code and t.id<>:id", domainClass.getSimpleName());
            }
            Query query = entityManager.createQuery(queryStr);
            query.setParameter(ICodeUnique.CODE_FIELD, code);
            query.setParameter(BaseEntity.ID, id);
            query.setMaxResults(1);
            List result = query.getResultList();
            return !result.isEmpty();
        }
        return false;
    }

    /**
     * ???????????????????????????????????????
     * @param tenantCode ????????????
     * @param code       ??????
     * @param id         Id??????
     * @return ??????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCodeExists(String tenantCode, String code, String id) {
        if (ICodeUnique.class.isAssignableFrom(domainClass) && ITenant.class.isAssignableFrom(domainClass)) {
            String queryStr;
            if (ISoftDelete.class.isAssignableFrom(domainClass)) {
                queryStr = String.format("select t.id from %s t where t.deleted=0 and t.tenantCode=:tenantCode and t.code=:code and t.id<>:id", domainClass.getSimpleName());
            } else {
                queryStr = String.format("select t.id from %s t where t.tenantCode=:tenantCode and t.code=:code and t.id<>:id", domainClass.getSimpleName());
            }
            Query query = entityManager.createQuery(queryStr);
            query.setParameter(ITenant.TENANT_CODE, tenantCode);
            query.setParameter(ICodeUnique.CODE_FIELD, code);
            query.setParameter(BaseEntity.ID, id);
            query.setMaxResults(1);
            List result = query.getResultList();
            return !result.isEmpty();
        }
        return false;
    }

    private Sort buildSort(Search searchConfig) {
        Sort sort = Sort.unsorted();
        if (searchConfig != null) {
            List<SearchOrder> sortOrders = searchConfig.getSortOrders();
            if (sortOrders != null) {
                List<Sort.Order> orders = new LinkedList<>();
                for (SearchOrder sortOrder : sortOrders) {
                    if (SearchOrder.Direction.DESC.equals(sortOrder.getDirection())) {
                        orders.add(new Sort.Order(Sort.Direction.DESC, sortOrder.getProperty()));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.ASC, sortOrder.getProperty()));
                    }
                }
                if (IRank.class.isAssignableFrom(domainClass)) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, IRank.RANK));
                }
                sort = Sort.by(orders);
            }
        }
        return sort;
    }

    private <X> Predicate buildPredicate(String propertyName, SearchFilter filter, Root<X> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder, Boolean having) {
        Object matchValue = filter.getValue();
        if ((matchValue == null)
                || (having && !propertyName.contains("("))
                || (!having && propertyName.contains("("))) {
            return null;
        }
        if (matchValue instanceof String) {
            if (StringUtils.isBlank(String.valueOf(matchValue))) {
                return null;
            }
        } else if (matchValue.getClass().isArray()) {
            if (((Object[]) matchValue).length == 0) {
                return null;
            }
        } else if (matchValue instanceof Collection) {
            if (((Collection) matchValue).size() == 0) {
                return null;
            }
        }

        Predicate predicate = null;
        Expression expression = buildExpression(root, builder, propertyName, null);
        if (SearchFilter.NULL_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = expression.isNull();
        } else if (SearchFilter.EMPTY_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
        } else if (SearchFilter.NO_NULL_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = expression.isNotNull();
        } else if (SearchFilter.NO_EMPTY_VALUE.equalsIgnoreCase(String.valueOf(matchValue))) {
            predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
        } else {
            // logic operator
            switch (filter.getOperator()) {
                case EQ:
                    // ?????????????????????????????????????????????????????????????????????,?????????2012-01-01??????,??????????????????2010-01-01????????????????????????,
                    // ?????????????????????????????????,???????????????????????????????????????+1???,?????????<2012-01-02????????????
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.and(builder.greaterThanOrEqualTo(expression, dateTime.toDate()),
                                    builder.lessThan(expression, dateTime.plusDays(1).toDate()));
                        }
                    }
                    if (predicate == null) {
                        predicate = builder.equal(expression, matchValue);
                    }
                    break;
                case NE:
                    // ?????????????????????????????????????????????????????????????????????,?????????2012-01-01??????,??????????????????2010-01-01????????????????????????,
                    // ?????????????????????????????????,???????????????????????????????????????+1???,?????????<2012-01-02????????????
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.or(builder.lessThan(expression, dateTime.toDate()),
                                    builder.greaterThan(expression, dateTime.plusDays(1).toDate()));
                        }
                    }
                    if (predicate == null) {
                        predicate = builder.notEqual(expression, matchValue);
                    }
                    break;
                //IS NULL OR ==''
                case BK:
                    predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
                    break;
                //IS NOT NULL AND !=''
                case NB:
                    predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
                    break;
                //IS NULL
                case NU:
                    if (matchValue instanceof Boolean && !((Boolean) matchValue)) {
                        predicate = builder.isNotNull(expression);
                    } else {
                        predicate = builder.isNull(expression);
                    }
                    break;
                //IS NOT NULL
                case NN:
                    if (matchValue instanceof Boolean && !((Boolean) matchValue)) {
                        predicate = builder.isNull(expression);
                    } else {
                        predicate = builder.isNotNull(expression);
                    }
                    break;
                //LIKE %abc%
                case LK:
                    predicate = builder.like(expression, "%" + matchValue + "%");
                    break;
                //NOT LIKE %abc%
                case NC:
                    predicate = builder.notLike(expression, "%" + matchValue + "%");
                    break;
                //LIKE abc%
                case LLK:
                    predicate = builder.like(expression, matchValue + "%");
                    break;
                //NOT LIKE abc%
                case BN:
                    predicate = builder.notLike(expression, matchValue + "%");
                    break;
                //LIKE %abc
                case RLK:
                    predicate = builder.like(expression, "%" + matchValue);
                    break;
                //NOT LIKE %abc
                case EN:
                    predicate = builder.notLike(expression, "%" + matchValue);
                    break;
                //BETWEEN 1 AND 2
                case BT:
                    Assert.isTrue(matchValue.getClass().isArray(), "Match value must be array");
                    Object[] matchValues = (Object[]) matchValue;
                    Assert.isTrue(matchValues.length == 2, "Match value must have two value");
                    // ?????????????????????????????????????????????????????????????????????,?????????2012-01-01??????,??????????????????2010-01-01????????????????????????,
                    // ?????????????????????????????????,???????????????????????????????????????+1???,?????????<2012-01-02????????????
                    if (matchValues[0] instanceof Date) {
                        DateTime dateFrom = new DateTime(((Date) matchValues[0]).getTime());
                        DateTime dateTo = new DateTime(((Date) matchValues[1]).getTime());
                        if (dateFrom.getHourOfDay() == 0 && dateFrom.getMinuteOfHour() == 0 && dateFrom.getSecondOfMinute() == 0) {
                            predicate = builder.and(builder.greaterThanOrEqualTo(expression, dateFrom.toDate()),
                                    builder.lessThan(expression, dateTo.plusDays(1).toDate()));
                        } else {
                            predicate = builder.equal(expression, matchValue);
                        }
                    } else {
                        predicate = builder.between(expression, (Comparable) matchValues[0], (Comparable) matchValues[1]);
                    }
                    break;
                //>
                case GT:
                    predicate = builder.greaterThan(expression, (Comparable) matchValue);
                    break;
                //>=
                case GE:
                    predicate = builder.greaterThanOrEqualTo(expression, (Comparable) matchValue);
                    break;
                //<
                case LT:
                    // ?????????????????????????????????????????????????????????????????????,?????????2012-01-01??????,??????????????????2010-01-01????????????????????????,
                    // ?????????????????????????????????,???????????????????????????????????????+1???,?????????<2012-01-02????????????
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.lessThan(expression, dateTime.toDate());
                        }
                    }
                    if (predicate == null && matchValue instanceof Comparable) {
                        predicate = builder.lessThan(expression, (Comparable) matchValue);
                    }
                    break;
                //<=
                case LE:
                    // ?????????????????????????????????????????????????????????????????????,?????????2012-01-01??????,??????????????????2010-01-01????????????????????????,
                    // ?????????????????????????????????,???????????????????????????????????????+1???,?????????<2012-01-02????????????
                    if (matchValue instanceof Date) {
                        DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                        if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                            predicate = builder.lessThan(expression, dateTime.plusDays(1).toDate());
                        }
                    }
                    if (predicate == null && matchValue instanceof Comparable) {
                        predicate = builder.lessThanOrEqualTo(expression, (Comparable) matchValue);
                    }
                    break;
                case IN:
                    if (matchValue.getClass().isArray()) {
                        predicate = expression.in((Object[]) matchValue);
                    } else if (matchValue instanceof Collection) {
                        predicate = expression.in((Collection) matchValue);
                    } else {
                        predicate = builder.equal(expression, matchValue);
                    }
                    break;
                //Property Less Equal: <
                case PLT:
                    Expression expressionPLT = buildExpression(root, builder, (String) matchValue, null);
                    predicate = builder.lessThan(expression, expressionPLT);
                    break;
                //Property Less Than: <=
                case PLE:
                    Expression expressionPLE = buildExpression(root, builder, (String) matchValue, null);
                    predicate = builder.lessThanOrEqualTo(expression, expressionPLE);
                    break;
                default:
                    break;
            }
        }

        Assert.notNull(predicate, "Undefined match type: " + filter.getOperator());
        return predicate;
    }

    /**
     * ??????????????????????????????JPA???????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????List<Predicate>??????????????????PropertyFilter????????????????????????exist???????????????
     */
    private <X> List<Predicate> buildPredicatesFromFilters(final Collection<SearchFilter> filters, Root<X> root, CriteriaQuery<?> query,
                                                           CriteriaBuilder builder, Boolean having) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (SearchFilter filter : filters) {
                Predicate predicate = buildPredicate(filter.getFieldName(), filter, root, query, builder, having);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }
        return predicates;
    }

    private <X extends Persistable> Specification<X> buildSpecification(final Search searchConfig) {
        return (root, query, builder) -> {
            if (searchConfig != null) {
                return buildPredicatesFromFilters(searchConfig, root, query, builder);
            } else {
                return null;
            }
        };
    }

    protected Predicate buildPredicatesFromFilters(Search searchConfig, Root root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (ITenant.class.isAssignableFrom(domainClass)) {
            SearchFilter filter = new SearchFilter(ITenant.TENANT_CODE, ContextUtil.getTenantCode(), SearchFilter.Operator.EQ);
            List<SearchFilter> filters = searchConfig.getFilters();
            if (filters == null || filters.size() == 0 || filters.stream().noneMatch(filter::equals)) {
                //??????????????????
                searchConfig.addFilter(filter);
            }
        }
        return buildPredicatesFromFilters(searchConfig, root, query, builder, false);
    }

    private Predicate buildPredicatesFromFilters(Search searchConfig, Root root, CriteriaQuery<?> query, CriteriaBuilder builder, Boolean having) {
        if (searchConfig == null) {
            return null;
        }
        Predicate predicate = null;
        List<Predicate> predicates = new ArrayList<Predicate>();
        //????????????
        String quickSearchValue = searchConfig.getQuickSearchValue();
        if (StringUtils.isNotBlank(quickSearchValue)) {
            //?????????????????? ' " ?????????
            if (!isValid(quickSearchValue)) {
                return null;
            }
            Collection<String> quickSearchProperties = searchConfig.getQuickSearchProperties();
            if (CollectionUtils.isNotEmpty(quickSearchProperties)) {
                List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
                for (String property : quickSearchProperties) {
                    searchFilters.add(new SearchFilter(property, quickSearchValue, SearchFilter.Operator.LK));
                }
                List<Predicate> quickSearchPredicates = buildPredicatesFromFilters(searchFilters, root, query, builder, having);

                if (CollectionUtils.isNotEmpty(quickSearchPredicates)) {
                    //?????????????????????or??????
                    if (quickSearchPredicates.size() == 1) {
                        predicate = builder.and(quickSearchPredicates.get(0));
                    } else {
                        //??????
                        predicate = builder.or(quickSearchPredicates.toArray(new Predicate[0]));
                    }
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                }
            }
        }
        //???????????????????????????and???
        List<Predicate> andPredicates = buildPredicatesFromFilters(searchConfig.getFilters(), root, query, builder, having);
        if (CollectionUtils.isNotEmpty(andPredicates)) {
            predicates.addAll(andPredicates);
        }
        if (predicates.size() > 0) {
            predicate = builder.and(predicates.toArray(new Predicate[0]));
        }
        return predicate;
    }

    private Expression parseExpr(Root<?> root, CriteriaBuilder criteriaBuilder, String expr, Map<String, Expression<?>> parsedExprMap) {
        if (parsedExprMap == null) {
            parsedExprMap = new HashMap<String, Expression<?>>();
        }
        Expression<?> expression = null;
        if (expr.contains("(")) {
            int left = 0;
            char[] chars = expr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    left = i;
                }
            }
            String leftStr = expr.substring(0, left);
            String op = null;
            char[] leftStrs = leftStr.toCharArray();
            for (int i = leftStrs.length - 1; i > 0; i--) {
                if (leftStrs[i] == '(' || leftStrs[i] == ')' || leftStrs[i] == ',') {
                    op = leftStr.substring(i + 1);
                    break;
                }
            }
            if (op == null) {
                op = leftStr;
            }
            String rightStr = expr.substring(left + 1);
            String arg = StringUtils.substringBefore(rightStr, ")");
            String[] args = arg.split(",");
            //LOGGER.debug("op={},arg={}", op, arg);
            if ("case".equalsIgnoreCase(op)) {
                CriteriaBuilder.Case selectCase = criteriaBuilder.selectCase();

                Expression caseWhen = parsedExprMap.get(args[0]);

                String whenResultExpr = args[1];
                Object whenResult = parsedExprMap.get(whenResultExpr);
                if (whenResult == null) {
                    CriteriaBuilder.Case<Long> whenCase = selectCase.when(caseWhen, new BigDecimal(whenResultExpr));
                    selectCase = whenCase;
                } else {
                    CriteriaBuilder.Case<Expression<?>> whenCase = selectCase.when(caseWhen, whenResult);
                    selectCase = whenCase;
                }
                String otherwiseResultExpr = args[2];
                Object otherwiseResult = parsedExprMap.get(otherwiseResultExpr);
                if (otherwiseResult == null) {
                    expression = selectCase.otherwise(new BigDecimal(otherwiseResultExpr));
                } else {
                    expression = selectCase.otherwise((Expression<?>) otherwiseResult);
                }
            } else {
                Object[] subExpressions = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    subExpressions[i] = parsedExprMap.get(args[i]);
                    if (subExpressions[i] == null) {
                        String name = args[i];
                        try {
                            Path<?> item;
                            if (name.contains(".")) {
                                String[] props = StringUtils.split(name, ".");
                                item = root.get(props[0]);
                                for (int j = 1; j < props.length; j++) {
                                    item = item.get(props[j]);
                                }
                            } else {
                                item = root.get(name);
                            }
                            subExpressions[i] = item;
                        } catch (Exception e) {
                            subExpressions[i] = new BigDecimal(name);
                        }
                    }
                }
                try {
                    //criteriaBuilder.quot();
                    expression = (Expression) MethodUtils.invokeMethod(criteriaBuilder, op, subExpressions);
                } catch (Exception e) {
                    LOGGER.error("Error for aggregate  setting ", e);
                }
            }

            String exprPart = op + "(" + arg + ")";
            String exprPartConvert = exprPart.replace(op + "(", op + "_").replace(arg + ")", arg + "_").replace(",", "_");
            expr = expr.replace(exprPart, exprPartConvert);
            parsedExprMap.put(exprPartConvert, expression);

            if (expr.contains("(")) {
                expression = parseExpr(root, criteriaBuilder, expr, parsedExprMap);
            }
        } else {
            Path<?> item;
            if (expr.contains(".")) {
                String[] props = StringUtils.split(expr, ".");
                item = root.get(props[0]);
                for (int j = 1; j < props.length; j++) {
                    item = item.get(props[j]);
                }
            } else {
                item = root.get(expr);
            }
            expression = item;
        }
        return expression;
    }

    private Expression<?> buildExpression(Root<?> root, CriteriaBuilder criteriaBuilder, String name, String alias) {
        Expression<?> expr = parseExpr(root, criteriaBuilder, name, null);
        if (alias != null) {
            expr.alias(alias);
        }
        return expr;
    }
}
