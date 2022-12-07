package com.kcmp.core.ck.dao;

import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.entity.TreeEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by kikock
 * 树形实体dao
 * @email kikock@qq.com
 **/
@NoRepositoryBean
public interface BaseTreeDao<T extends BaseEntity & TreeEntity> extends BaseDao<T, String> {
    /**
     * 获取所有树根节点
     * @return 返回树根节点集合
     */
    List<T> getAllRootNode();

    /**
     * 获取指定节点下的所有子节点(包含自己)
     * @param nodeId 当前节点id
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    List<T> getChildrenNodes(String nodeId);

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     * @param nodeId 当前节点id
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    List<T> getChildrenNodesNoneOwn(String nodeId);

    /**
     * 获取指定节点名称的所有节点
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    List<T> getChildrenNodesByName(String nodeName);

    /**
     * 获取树
     * @param nodeId 当前节点id
     * @return 返回指定节点树形对象
     */
    T getTree(String nodeId);

    /**
     * 通过代码路径获取指定路径开头的集合
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    List<T> findByCodePathStartingWith(String codePath);

    /**
     * 获取指定代码路径下的所有子节点(不包含自己)
     * @param codePath  代码路径
     * @param nodeId    当前节点id
     * @return
     */
    List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId);

    /**
     * 通过名称路径获取指定路径开头的集合
     * @param namePath  名称路径
     * @return
     */
    List<T> findByNamePathStartingWith(String namePath);

    /**
     * 获取指定名称路径下的所有子节点(不包含自己)
     * @param namePath  名称路径
     * @param nodeId    当前节点id
     * @return
     */
    List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId);

    /**
     * 根据名称路径模糊获取节点
     * @param namePath  名称路径
     * @return
     */
    List<T> findByNamePathLike(String namePath);

    /////////////////////////////以下为冻结特性的方法/////////////////////////
    /**
     * 根据id获取未冻结节点
     * @param id    当前节点id
     * @return
     */
    T findOne4Unfrozen(String id);

    /**
     * 获取所有树未冻结根节点
     * @return 返回树根节点集合
     */
    List<T> getAllRootNode4Unfrozen();

    /**
     * 获取指定节点下未冻结的所有子节点(包含自己)
     * @param nodeId 当前节点id
     * @return 获取指定节点下未冻结的所有子节点(包含自己)
     */
    List<T> getChildrenNodes4Unfrozen(String nodeId);

    /**
     * 获取指定节点下未冻结的所有子节点(不包含自己)
     * @param nodeId 当前节点id
     * @return 获取指定节点下未冻结的所有子节点(不包含自己)
     */
    List<T> getChildrenNodesNoneOwn4Unfrozen(String nodeId);

    /**
     * 获取指定节点名称未冻结的所有节点
     * @param nodeName 当前节点名称
     * @return 获取指定节点名称未冻结的所有节点
     */
    List<T> getChildrenNodesByName4Unfrozen(String nodeName);

    /**
     * 获取所有未冻结的树形对象
     * @param nodeId 当前节点id
     * @return 获取所有未冻结的树形对象
     */
    T getTree4Unfrozen(String nodeId);

    /**
     * 通过代码路径获取指定路径开头未冻结的集合
     * @param codePath 代码路径
     * @return 返回指定代码路径开头未冻结的集合
     */
    List<T> findByCodePathStartWith4Unfrozen(String codePath);

    /**
     * 获取指定代码路径下的所有未冻结子节点(不包含自己)
     * @param codePath  代码路径
     * @param nodeId    当前节点id
     * @return
     */
    List<T> findByCodePathStartWithAndIdNot4Unfrozen(String codePath, String nodeId);

    /**
     * 通过名称路径获取指定路径开头未冻结的集合
     * @param namePath  名称路径
     * @return
     */
    List<T> findByNamePathStartWith4Unfrozen(String namePath);

    /**
     * 获取指定名称路径下的所有未冻结子节点(不包含自己)
     * @param namePath  名称路径
     * @param nodeId    当前节点id
     * @return
     */
    List<T> findByNamePathStartWithAndIdNot4Unfrozen(String namePath, String nodeId);

    /**
     * 根据名称路径模糊获取未冻结节点
     * @param namePath 名称路径
     * @return
     */
    List<T> findByNamePathLike4Unfrozen(String namePath);
}
