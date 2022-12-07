package com.kcmp.core.ck.service;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.enums.UserAuthorityPolicy;
import com.kcmp.ck.exception.ServiceException;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import com.kcmp.ck.vo.SessionUser;
import com.kcmp.core.ck.dao.BaseTreeDao;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.TreeEntity;
import com.kcmp.core.ck.entity.auth.AuthTreeEntityData;
import com.kcmp.core.ck.entity.auth.IDataAuthTreeEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by kikock
 * 树形实体基础服务实现
 * @email kikock@qq.com
 **/
public abstract class BaseTreeService<T extends BaseEntity & TreeEntity<T>> extends BaseService<T, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTreeService.class);

    @Override
    protected abstract BaseTreeDao<T> getDao();

    /**
     * 保存树对象
     * 通过entity.isNew()判断是否是新增，当为true时，执行创建操作；为fasle时，执行更新操作
     * 约束：不允许修改parentId
     * @param entity 树形结构实体
     * @return 返回操作对象
     */
    @Override
    public OperateResultWithData<T> save(T entity) {
        Validation.notNull(entity, "持久化对象不能为空");
        String parentId = entity.getParentId();
        OperateResultWithData<T> operateResultWithData;
        boolean isNew = isNew(entity);
        if (isNew) {
            operateResultWithData = preInsert(entity);
        } else {
            operateResultWithData = preUpdate(entity);
        }

        if (Objects.isNull(operateResultWithData) || operateResultWithData.successful()) {
            // setNodeLevel setCodePath setNamePath
            //父ID为空，则表示是树根节点
            if (StringUtils.isBlank(parentId)) {
                entity.setNodeLevel(0);
                entity.setCodePath(TreeEntity.CODE_DELIMITER + entity.getCode());
                entity.setNamePath(TreeEntity.NAME_DELIMITER + entity.getName());
            } else {
                //获取父节点
                TreeEntity parentNode = findOne(parentId);
                if (Objects.nonNull(parentNode)) {
                    //设置层级
                    entity.setNodeLevel(Objects.nonNull(parentNode.getNodeLevel()) ? parentNode.getNodeLevel() + 1 : 0);

                    StringBuilder str = new StringBuilder(128);
                    //设置代码路径
                    str.append(parentNode.getCodePath()).append(TreeEntity.CODE_DELIMITER).append(entity.getCode());
                    entity.setCodePath(str.toString());
                    str.setLength(0);

                    //设置名称路径
                    str.append(parentNode.getNamePath()).append(TreeEntity.NAME_DELIMITER).append(entity.getName());
                    entity.setNamePath(str.toString());
                    str.setLength(0);
                } else {
                    // error
                    return OperateResultWithData.operationFailureWithData(entity, "ecmp_service_00005");
                }
            }

            if (!isNew) {
                //通过当前ID获取原始数据
                String originId = entity.getId();
                T origin = findOne(originId);
                if (Objects.nonNull(origin)) {
                    //验证当前parentId是否与原parentId相同,用以控制不允许修改父节点操作
                    if (!StringUtils.equalsAny(origin.getParentId(), parentId, null, "")) {
                        return OperateResultWithData.operationFailureWithData(entity, "ecmp_service_00006");
                    }
                } else {
                    //未找到原始数据
                    return OperateResultWithData.operationFailureWithData(entity, "ecmp_service_00004");
                }

                //检查是否修改代码和名称，以便同步更新子节点的路径
                List<T> childrenList = getChildrenNodesNoneOwn(originId);
                if (CollectionUtils.isNotEmpty(childrenList)) {
                    String temp;
                    for (T item : childrenList) {
                        temp = item.getCodePath().replace(origin.getCodePath(), entity.getCodePath());
                        item.setCodePath(temp);
                        temp = item.getNamePath().replace(origin.getNamePath(), entity.getNamePath());
                        item.setNamePath(temp);

                        getDao().save(item);
                    }
                }
            }

            getDao().save(entity);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Saved entity id is {}", entity.getId());
            }

            if (isNew) {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(entity, "ecmp_service_00001");
            } else {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(entity, "ecmp_service_00002");
            }
        }
        return operateResultWithData;
    }

    @Override
    public void save(Collection<T> entities) {
        OperateResultWithData operateResult;
        if (CollectionUtils.isNotEmpty(entities)) {
            for (T entity : entities) {
                operateResult = save(entity);
                if (operateResult.notSuccessful()) {
                    throw new ServiceException(operateResult.getMessage());
                }
            }
        }
    }

    @Override
    public OperateResult delete(String id) {
        OperateResult operateResult = preDelete(id);
        if (operateResult.successful()) {
            T entity = findOne(id);
            if (Objects.nonNull(entity)) {
                List<T> childrenList = getChildrenNodesNoneOwn(id);
                if (CollectionUtils.isEmpty(childrenList)) {
                    getDao().delete(entity);
                    return OperateResult.operationSuccess("ecmp_service_00003");
                } else {
                    return OperateResult.operationFailure("ecmp_service_00007");
                }
            } else {
                return OperateResult.operationWarning("ecmp_service_00004");
            }
        } else {
            return operateResult;
        }
    }

    @Override
    public void delete(Collection<String> ids) {
        List<T> list = findByIds(ids);
        if (CollectionUtils.isNotEmpty(list)) {
            OperateResult operateResult;
            for (T t : list) {
                operateResult = delete(t.getId());
                if (operateResult.notSuccessful()) {
                    throw new ServiceException(operateResult.getMessage());
                }
            }
        }
    }

    /**
     * 移动节点
     * @param nodeId         当前节点ID
     * @param targetParentId 目标父节点ID
     * @return 返回操作结果对象
     */
    public OperateResult move(String nodeId, String targetParentId) {
        if (StringUtils.isBlank(nodeId)) {
            return OperateResult.operationFailure("ecmp_service_00008", "当前节点ID");
        }
        if (StringUtils.isBlank(targetParentId)) {
            return OperateResult.operationFailure("ecmp_service_00008", "目标父节点ID");
        }

        //获取当前节点
        T currentNode = findOne(nodeId);
        if (Objects.isNull(currentNode)) {
            return OperateResult.operationWarning("ecmp_service_00004");
        }
        OperateResult operateResult;
        //当前节点的父节点
        T currentParent = null;
        if (StringUtils.isNotBlank(currentNode.getParentId())) {
            currentParent = findOne(currentNode.getParentId());
        }
        //移动目标父节点
        T targetParent = findOne(targetParentId);
        if (Objects.nonNull(targetParent)) {
            //检查当前父id与目标父id是否不同，如果相同不执行移动
            if (Objects.nonNull(currentParent) && StringUtils.equals(currentParent.getId(), targetParent.getId())) {
                return OperateResult.operationSuccess("ecmp_service_00015");
            }
            int parentNodeLevel = 0;
            String parentCodePath = "";
            String parentNamePath = "";
            if (Objects.nonNull(currentParent)) {
                parentNodeLevel = currentParent.getNodeLevel();
                parentCodePath = currentParent.getCodePath();
                parentNamePath = currentParent.getNamePath();
            }

            //目标父层级 - 当前父层级
            int difference = targetParent.getNodeLevel() - parentNodeLevel;
            List<T> childrenList = findByCodePathStartingWith(currentNode.getCodePath());
            if (CollectionUtils.isNotEmpty(childrenList)) {
                String temp;
                for (T item : childrenList) {
                    //是否是当前节点
                    if (nodeId.equals(item.getId())) {
                        item.setParentId(targetParentId);
                    }
                    if (Objects.nonNull(currentParent)) {
                        item.setNodeLevel(item.getNodeLevel() + difference);
                        temp = targetParent.getCodePath() + item.getCodePath().substring(parentCodePath.length());
                        item.setCodePath(temp);
                        temp = targetParent.getNamePath() + item.getNamePath().substring(parentNamePath.length());
                    } else {
                        item.setNodeLevel(item.getNodeLevel() + difference + 1);
                        temp = targetParent.getCodePath() + item.getCodePath();
                        item.setCodePath(temp);
                        temp = targetParent.getNamePath() + item.getNamePath();
                    }
                    item.setNamePath(temp);
                    getDao().save(item);
                }
            }
            operateResult = OperateResult.operationSuccess("ecmp_service_00009");
        } else {
            operateResult = OperateResult.operationWarning("ecmp_service_00004");
        }
        return operateResult;
    }

    /**
     * 获取所有树根节点
     * @return 返回树根节点集合
     */
    @Transactional(readOnly = true)
    public List<T> getAllRootNode() {
        return getDao().getAllRootNode();
    }

    /**
     * 获取一个节点的所有子节点
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 子节点清单
     */
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes(String nodeId, boolean includeSelf) {
        if (includeSelf) {
            return getDao().getChildrenNodes(nodeId);
        }
        return getDao().getChildrenNodesNoneOwn(nodeId);
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Transactional(readOnly = true)
    public List<T> getChildrenNodes(String nodeId) {
        return getDao().getChildrenNodes(nodeId);
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesNoneOwn(String nodeId) {
        return getDao().getChildrenNodesNoneOwn(nodeId);
    }

    /**
     * 获取指定节点名称的所有节点
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Transactional(readOnly = true)
    public List<T> getChildrenNodesByName(String nodeName) {
        return getDao().getChildrenNodesByName(nodeName);
    }

    /**
     * 获取树
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Transactional(readOnly = true)
    public T getTree(String nodeId) {
        return getDao().getTree(nodeId);
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    public List<T> findByCodePathStartingWith(String codePath) {
        return getDao().findByCodePathStartingWith(codePath);
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     * @param codePath
     * @param nodeId
     * @return
     */
    public List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId) {
        return getDao().findByCodePathStartingWithAndIdNot(codePath, nodeId);
    }

    public List<T> findByNamePathStartingWith(String namePath) {
        return getDao().findByNamePathStartingWith(namePath);
    }

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    public List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId) {
        return getDao().findByNamePathStartingWithAndIdNot(namePath, nodeId);
    }

    /**
     * 根据名称路径模糊获取节点
     * @param namePath  名称路径
     * @return
     */
    public List<T> findByNamePathLike(String namePath) {
        return getDao().findByNamePathLike(namePath);
    }

    /**
     * 获取一个节点的父节点
     * @param node 节点
     * @return 父节点
     */
    private T getParent(T node) {
        if (node.getParentId() == null) {
            return null;
        }
        //获取父节点
        return findOne(node.getParentId());
    }

    /**
     * 获取一个节点的所有父节点
     * @param node        节点
     * @param includeSelf 返回值中是否包含节点本身
     * @return 父节点清单
     */
    public List<T> getParentNodes(T node, boolean includeSelf) {
        List<T> parents = new LinkedList<>();
        if (node == null) {
            return parents;
        }
        if (includeSelf) {
            parents.add(node);
        }
        T parent = getParent(node);
        while (parent != null) {
            parents.add(parent);
            parent = getParent(parent);
        }
        return parents;
    }

    /**
     * 获取一个节点的所有父节点
     * @param nodeId      节点Id
     * @param includeSelf 返回值中是否包含节点本身
     * @return 父节点清单
     */
    public List<T> getParentNodes(String nodeId, boolean includeSelf) {
        T node = findOne(nodeId);
        return getParentNodes(node, includeSelf);
    }

    /**
     * 递归查找子节点并设置子节点
     * @param treeNode 树形节点（顶级节点）
     * @param nodes    节点清单
     * @return 树形节点
     */
    private static <Tree extends TreeEntity<Tree>> Tree findChildren(Tree treeNode, List<Tree> nodes) {
        for (Tree node : nodes) {
            if (treeNode.getId().equals(node.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(node, nodes));
            }
        }
        return treeNode;
    }

    /**
     * 通过节点清单构建树
     * @param nodes 节点清单
     * @return 树
     */
    public static <Tree extends TreeEntity<Tree>> List<Tree> buildTree(List<Tree> nodes) {
        List<Tree> result = new ArrayList<>();
        if (nodes == null || nodes.size() == 0) {
            return result;
        }
        //将输入节点排序
        List<Tree> sordedNodes = nodes.stream().sorted((n1, n2) ->
                (Integer.compare(n1.getNodeLevel() + n1.getRank(), (n2.getNodeLevel() + n2.getRank())))).collect(Collectors.toList());
        //获取清单中的顶级节点
        for (Tree node : sordedNodes) {
            String parentId = node.getParentId();
            Tree parent = sordedNodes.stream().filter((n) -> StringUtils.equals(n.getId(), parentId)
                    && !StringUtils.equals(n.getId(), node.getId())).findAny().orElse(null);
            if (parent == null) {
                //递归构造子节点
                findChildren(node, sordedNodes);
                result.add(node);
            }
        }
        return result.stream().sorted(Comparator.comparingInt(Tree::getRank)).collect(Collectors.toList());
    }

    /**
     * 通过业务实体Id清单获取数据权限树形实体清单
     * @param ids 业务实体Id清单
     * @return 数据权限树形实体清单
     */
    @Transactional(readOnly = true)
    public List<AuthTreeEntityData> getAuthTreeEntityDataByIds(List<String> ids) {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthTreeEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取所有未冻结的业务实体
        List<T> allEntities = getDao().findAllUnfrozen();
        if (allEntities == null || allEntities.isEmpty()) {
            return Collections.emptyList();
        }
        //获取Id清单的业务实体
        List<T> entities = allEntities.stream().filter((p) -> ids.contains(p.getId())).collect(Collectors.toList());
        List<AuthTreeEntityData> dataList = new ArrayList<>();
        entities.forEach((p) -> dataList.add(new AuthTreeEntityData((IDataAuthTreeEntity) p)));
        //装配成树形结构
        return buildTree(dataList);
    }

    /**
     * 获取所有数据权限树形实体清单
     * @return 数据权限树形实体清单
     */
    @Transactional(readOnly = true)
    public List<AuthTreeEntityData> findAllAuthTreeEntityData() {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthTreeEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取所有未冻结的业务实体
        List<T> allEntities = getDao().findAllUnfrozen();
        if (allEntities == null || allEntities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AuthTreeEntityData> dataList = new ArrayList<>();
        allEntities.forEach((p) -> dataList.add(new AuthTreeEntityData((IDataAuthTreeEntity) p)));
        //装配成树形结构
        return buildTree(dataList);
    }

    /**
     * 获取当前用户有权限的树形业务实体清单
     * @param featureCode 功能项代码
     * @return 有权限的树形业务实体清单
     */
    @Transactional(readOnly = true)
    public List<T> getUserAuthorizedTreeEntities(String featureCode) {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthTreeEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取当前用户
        SessionUser sessionUser = ContextUtil.getSessionUser();
        //如果是匿名用户无数据
        if (sessionUser.isAnonymous()) {
            return Collections.emptyList();
        }

        List<T> resultList;
        List<T> allEntities;
        UserAuthorityPolicy authorityPolicy = sessionUser.getAuthorityPolicy();
        switch (authorityPolicy) {
            case GlobalAdmin:
                //如果是全局管理，无数据
                resultList = Collections.emptyList();
                break;
            case TenantAdmin:
                //如果是租户管理员，返回租户的所有数据(未冻结)
                allEntities = getDao().findAllUnfrozen();
                if (allEntities == null || allEntities.isEmpty()) {
                    resultList = Collections.emptyList();
                } else {
                    resultList = buildTree(allEntities);
                }
                break;
            case NormalUser:
            default:
                //如果是一般用户，先获取有权限的角色对应的业务实体Id清单
                List<String> entityIds = getNormalUserAuthorizedEntityIds(featureCode, sessionUser.getUserId());
                if (entityIds == null || entityIds.isEmpty()) {
                    resultList = Collections.emptyList();
                } else {
                    //先获取所有未冻结的业务实体
                    allEntities = getDao().findAllUnfrozen();
                    if (allEntities == null || allEntities.isEmpty()) {
                        resultList = Collections.emptyList();
                    } else {
                        List<T> entities = allEntities.stream().filter((p) -> entityIds.contains(p.getId())).collect(Collectors.toList());
                        resultList = buildTree(entities);
                    }
                }
                break;
        }
        return resultList;
    }
}
