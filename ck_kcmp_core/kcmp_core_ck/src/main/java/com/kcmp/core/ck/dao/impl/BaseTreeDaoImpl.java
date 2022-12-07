package com.kcmp.core.ck.dao.impl;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.core.ck.dao.BaseTreeDao;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.IFrozen;
import com.kcmp.core.ck.entity.ITenant;
import com.kcmp.core.ck.entity.TreeEntity;
import com.kcmp.core.ck.search.Search;
import com.kcmp.core.ck.search.SearchFilter;
import com.kcmp.core.ck.search.SearchOrder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kikock
 * 树形实体Dao基类
 * @email kikock@qq.com
 **/
public class BaseTreeDaoImpl<T extends BaseEntity & TreeEntity> extends BaseDaoImpl<T, String> implements BaseTreeDao<T> {

    public BaseTreeDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    /**
     * 获取所有树根节点
     * @return 返回树根节点集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getAllRootNode() {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWith(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesNoneOwn(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWithAndIdNot(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称的所有节点
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesByName(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取树
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Override
    @Transactional(readOnly = true)
    public T getTree(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = findOne(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    @Override
    public List<T> findByCodePathStartingWith(String codePath) {
        List<T> list = new ArrayList<>();
        //查询自己
        T own = findByProperty(TreeEntity.CODE_PATH, codePath);
        if (own != null) {
            list.add(own);
        }

        //查询子项
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
        setDefaultSort(search);
        List<T> childrens = findByFilters(search);
        if (childrens != null && childrens.size() > 0) {
            list.addAll(childrens);
        }
        return list;
    }

    /**
     * 获取指定代码路径下的所有子节点(不包含自己)
     * @param codePath  代码路径
     * @param nodeId    当前节点id
     * @return
     */
    @Override
    public List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 通过名称路径获取指定路径开头的集合
     * @param namePath  名称路径
     * @return
     */
    @Override
    public List<T> findByNamePathStartingWith(String namePath) {
        //查询自己
        List<T> list = findListByProperty(TreeEntity.NAME_PATH, namePath);
        if (list == null) {
            list = new ArrayList<>();
        }

        //查询子项
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
        setDefaultSort(search);
        List<T> childrens = findByFilters(search);
        if (childrens != null && childrens.size() > 0) {
            list.addAll(childrens);
        }
        return list;
    }

    /**
     * 获取指定名称路径下的所有子节点(不包含自己)
     * @param namePath  名称路径
     * @param nodeId    当前节点id
     * @return
     */
    @Override
    public List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 根据名称路径模糊获取节点
     * @param namePath  名称路径
     * @return
     */
    @Override
    public List<T> findByNamePathLike(String namePath) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LK));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 递归构造树
     */
    private T recursiveBuild(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                T treeEntity = recursiveBuild(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }

    /////////////////////////////以下为冻结特性的方法/////////////////////////
    /**
     * 根据id获取未冻结节点
     * @param id    当前节点id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public T findOne4Unfrozen(String id) {
        Assert.notNull(id, "主键不能为空");
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Specification<T> spec = (root, query, builder) -> {
                if (ITenant.class.isAssignableFrom(domainClass)) {
                    Predicate tenantCodePredicate = builder.equal(root.get(ITenant.TENANT_CODE), ContextUtil.getTenantCode());
                    Predicate idPredicate = builder.equal(root.get(BaseEntity.ID), id);
                    Predicate frozenPredicate = builder.equal(root.get(IFrozen.FROZEN), Boolean.FALSE);

                    Predicate[] predicates = new Predicate[]{frozenPredicate, tenantCodePredicate, idPredicate};
                    return builder.and(predicates);
                } else {
                    return builder.and(builder.equal(root.get(IFrozen.FROZEN), Boolean.FALSE), builder.equal(root.get(BaseEntity.ID), id));
                }
            };
            return findOne(spec).orElse(null);
        } else {
            return findOne(id);
        }
    }

    /**
     * 获取所有树未冻结根节点
     * @return 返回树根节点集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getAllRootNode4Unfrozen() {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = getAllRootNode();
        }
        return list;
    }

    /**
     * 获取指定节点下未冻结的所有子节点(包含自己)
     * @param nodeId 当前节点id
     * @return 获取指定节点下未冻结的所有子节点(包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWith4Unfrozen(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下未冻结的所有子节点(不包含自己)
     * @param nodeId 当前节点id
     * @return 获取指定节点下未冻结的所有子节点(不包含自己)
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesNoneOwn4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWithAndIdNot4Unfrozen(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称未冻结的所有节点
     * @param nodeName 当前节点名称
     * @return 获取指定节点名称未冻结的所有节点
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesByName4Unfrozen(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike4Unfrozen(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取所有未冻结的树形对象
     * @param nodeId 当前节点id
     * @return 获取所有未冻结的树形对象
     */
    @Override
    @Transactional(readOnly = true)
    public T getTree4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes4Unfrozen(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild4Unfrozen(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头未冻结的集合
     * @param codePath 代码路径
     * @return 返回指定代码路径开头未冻结的集合
     */
    @Override
    public List<T> findByCodePathStartWith4Unfrozen(String codePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            list = new ArrayList<>();
            //查询自己
            T own = findByProperty(TreeEntity.CODE_PATH, codePath);
            if (own != null) {
                list.add(own);
            }

            //查询子项
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            List<T> childrens = findByFilters(search);
            if (childrens != null && childrens.size() > 0) {
                list.addAll(childrens);
            }
        } else {
            list = findByCodePathStartingWith(codePath);
        }
        return list;
    }

    /**
     * 获取指定代码路径下的所有未冻结子节点(不包含自己)
     * @param codePath  代码路径
     * @param nodeId    当前节点id
     * @return
     */
    @Override
    public List<T> findByCodePathStartWithAndIdNot4Unfrozen(String codePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByCodePathStartingWithAndIdNot(codePath, nodeId);
        }
        return list;
    }

    /**
     * 通过名称路径获取指定路径开头未冻结的集合
     * @param namePath  名称路径
     * @return
     */
    @Override
    public List<T> findByNamePathStartWith4Unfrozen(String namePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            //查询自己
            list = findListByProperty(TreeEntity.NAME_PATH, namePath);
            if (list == null) {
                list = new ArrayList<>();
            }

            //查询子项
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            List<T> childrens = findByFilters(search);
            if (childrens != null && childrens.size() > 0) {
                list.addAll(childrens);
            }
        } else {
            list = findByNamePathStartingWith(namePath);
        }
        return list;
    }

    /**
     * 获取指定名称路径下的所有未冻结子节点(不包含自己)
     * @param namePath  名称路径
     * @param nodeId    当前节点id
     * @return
     */
    @Override
    public List<T> findByNamePathStartWithAndIdNot4Unfrozen(String namePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByNamePathStartingWithAndIdNot(namePath, nodeId);
        }
        return list;
    }

    /**
     * 根据名称路径模糊获取未冻结节点
     * @param namePath 名称路径
     * @return
     */
    @Override
    public List<T> findByNamePathLike4Unfrozen(String namePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath, SearchFilter.Operator.LK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByNamePathLike(namePath);
        }
        return list;
    }

    /**
     * 递归构造树
     */
    private T recursiveBuild4Unfrozen(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                if (treeNode instanceof IFrozen) {
                    IFrozen frozen = (IFrozen) treeNode;
                    //冻结
                    if (frozen.getFrozen()) {
                        continue;
                    }
                }

                T treeEntity = recursiveBuild4Unfrozen(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }

    /**
     * 默认排序
     * 优先使用RANK字段排序(顺序),再按CODE字段排序(顺序)
     */
    protected void setDefaultSort(Search search) {
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.CODE));
    }
}
