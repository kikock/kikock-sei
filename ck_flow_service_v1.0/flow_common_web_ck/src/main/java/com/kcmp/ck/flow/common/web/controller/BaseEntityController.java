package com.kcmp.ck.flow.common.web.controller;

import com.kcmp.core.ck.api.IBaseEntityService;
import com.kcmp.core.ck.api.IFindAllService;
import com.kcmp.core.ck.api.IFindByPageService;
import com.kcmp.core.ck.entity.BaseEntity;
import com.kcmp.core.ck.dto.PageResult;
import com.kcmp.core.ck.dto.Search;
import com.kcmp.ck.annotation.IgnoreCheckAuth;
import com.kcmp.ck.exception.WebException;
import com.kcmp.ck.flow.common.web.common.Contants;
import com.kcmp.ck.flow.common.web.vo.OperateStatus;
import com.kcmp.ck.log.LogUtil;
import com.kcmp.ck.util.ReflectionUtils;
import com.kcmp.ck.vo.OperateResult;
import com.kcmp.ck.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kikock
 * 基础实体控制器
 * @author
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public abstract class BaseEntityController<E extends BaseEntity> extends BaseController {

    private static Map<Class, String> entityPathMap = new HashMap<Class, String>();

    /**
     * Controller所管理的Entity类型.
     */
    protected Class<E> entityClass;

    /**
     * 构造函数.
     * 通过对泛型E的反射获得Entity class
     */
    public BaseEntityController() {
        entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 获得IBaseEntityService接口，必须在子类实现
     */
    protected abstract Object getBaseService();

    private IBaseEntityService<E> getBaseEntityService() {
        Object obj = getBaseService();
        if (obj instanceof IBaseEntityService) {
            return (IBaseEntityService<E>) obj;
        } else {
            throw new ClassCastException("不是IBaseEntityService的子类");
        }
    }

    private IFindByPageService<E> getFindByPageService() {
        Object obj = getBaseService();
        if (obj instanceof IFindByPageService) {
            return (IFindByPageService<E>) obj;
        } else {
            throw new ClassCastException("不是IFindByPageService的子类");
        }
    }

    private IFindAllService<E> getFindAllService() {
        Object obj = getBaseService();
        if (obj instanceof IFindAllService) {
            return (IFindAllService<E>) obj;
        } else {
            throw new ClassCastException("不是IFindAllService的子类");
        }
    }

    /**
     * 取得EntityClass.
     */
    protected final Class<E> getEntityClass() {
        return entityClass;
    }

    /**
     * 新建业务实体对象.
     */
    protected final E getNewEntityInstance() {
        E entity;
        try {
            entity = getEntityClass().newInstance();
        } catch (Exception e) {
            throw new WebException("Can't new Instance of Entity.");
        }
        return entity;
    }

    /**
     * 获取所管理的实体对象名.
     */
    protected final String getEntityName() {
        return ClassUtils.getShortName(getEntityClass());
    }

    /**
     * 获取所管理的实体对象名.
     * 首字母小写，如"sysUser"
     */
    protected final String getLowerEntityName() {
        return StringUtils.uncapitalize(getEntityName());
    }

    private static final Pattern PATTERN = Pattern.compile("(^com.kcmp.ck.*entity.)(.*)(.*)");

    /**
     * 默认返回实体名+View；若是com.kcmp.ck命名空间下，则返回entity下的路径.
     * @param request 请求对象
     * @return 相对页面地址
     */
    @RequestMapping(value = Contants.REQUEST_SHOW, method = RequestMethod.GET)
    public final String show(HttpServletRequest request) {
        //初始化页面时，追加数据
        onInitPageExtraData(request);

        //获取返回页面视图
        return getReturnView(request);
    }

    /**
     * 为页面显示初始化一些额外数据,根据需要在子类重载.
     * @param request 请求对象
     */
    protected void onInitPageExtraData(HttpServletRequest request) {
    }

    /**
     * 获取返回页面视图
     * @param request 请求对象
     * @return 返回页面视图
     */
    protected String getReturnView(HttpServletRequest request) {
        Class clazz = getEntityClass();
        String path = entityPathMap.get(clazz);
        if (StringUtils.isBlank(path)) {
            path = getEntityName();
            String fullName = clazz.getName();
            if (fullName.startsWith("com.kcmp.ck.")) {
                Matcher matcher = PATTERN.matcher(fullName);
                if (matcher.find() && matcher.groupCount() >= 2) {
                    path = matcher.group(2);
                    path = path.replaceAll("[.]", "/");
                }
            }
            entityPathMap.put(clazz, path);
        }
        return path + "View";
    }

    /**
     * 查询一个指定的实体对象
     * @param id      指定实体id
     * @param request 请求对象
     * @return 实体对象
     */
    @IgnoreCheckAuth
    @ResponseBody
    @RequestMapping(value = Contants.REQUEST_GET_ONE)
    public final Object getOne(@RequestParam(value = "id", required = false) String id, HttpServletRequest request) {
        E entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = getBaseEntityService().findOne(id);
        }
        return onGetOneRender(entity, request);
    }

    /**
     * 查询一个指定的实体对象,根据需要在子类重载.
     * @param entity  实体对象
     * @param request 请求对象
     */
    protected Object onGetOneRender(E entity, HttpServletRequest request) {
        return entity;
    }

    /**
     * 分页查询
     * @param request 请求对象
     * @return 分页查询结果
     */
    @IgnoreCheckAuth
    @ResponseBody
    @RequestMapping(value = Contants.REQUEST_LIST_PAGE)
    public final Object listByPage(HttpServletRequest request) {
//        //创建查询对象
//        Search search = SearchUtil.genSearch(request);
//        //追加额外查询条件
//        onInitExtraQueryData(search, request);
//        //执行查询
//        PageResult<E> pageResult = doQueryListData(search, request);
//        //
//        return onListRender(pageResult, request);
        LogUtil.error("flow-service.BaseEntityController分页查询报错！");
        return null;
    }

    /**
     * 查询所有
     * @param request 请求对象
     * @return 查询所有
     */
    @IgnoreCheckAuth
    @ResponseBody
    @RequestMapping(value = Contants.REQUEST_LIST_ALL)
    public final Object listAll(HttpServletRequest request) {
        return getFindAllService().findAll();
    }

    /**
     * 追加额外查询条件,根据需要在子类重载.
     * @param search  查询对象
     * @param request 请求对象
     */
    protected void onInitExtraQueryData(Search search, HttpServletRequest request) {

    }

    /**
     * 执行查询,根据需要在子类重载.
     * @param search  查询对象
     * @param request 请求对象
     */
    protected PageResult<E> doQueryListData(Search search, HttpServletRequest request) {
        return getFindByPageService().findByPage(search);
    }

    /**
     * 列表数据回写（默认使用json序列化值）
     * @param pageResult 分页查询结果
     * @param request    请求对象
     * @return 返回回写页面的对象
     */
    protected Object onListRender(PageResult<E> pageResult, HttpServletRequest request) {
        return pageResult;
    }

    /**
     * 保存一个实体
     * @param entity  实体对象
     * @param request 请求对象
     * @return 操作结果
     */
    @ResponseBody
    @RequestMapping(value = Contants.REQUEST_SAVE, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT})
    public final Object save(E entity, HttpServletRequest request) {
        //持久化操作前
        OperateStatus operateStatus = onBeforeDoSave(entity, request);

        if (Objects.nonNull(operateStatus) && operateStatus.isSuccess()) {
            OperateResultWithData<E> saveResult = doSubmitSave(entity, request);

            operateStatus.setSuccess(saveResult.successful());
            operateStatus.setMsg(saveResult.getMessage());
            operateStatus.setData(saveResult.getData());
        }
        return onAfterDoSave(operateStatus, request);
    }

    /**
     * 持久化操作前,根据需要在子类重载.
     * @param entity  持久化实体对象
     * @param request 请求对象
     */
    protected OperateStatus onBeforeDoSave(E entity, HttpServletRequest request) {
        return OperateStatus.defaultSuccess();
    }

    /**
     * 提交保存操作,根据需要在子类重载.
     * @param entity  持久化实体对象
     * @param request 请求对象
     * @return 操作对象OperateResultWithData
     * @see OperateResultWithData
     */
    protected OperateResultWithData<E> doSubmitSave(E entity, HttpServletRequest request) {
        return getBaseEntityService().save(entity);
    }

    /**
     * 保存后操作,根据需要在子类重载.
     * @param operateStatus 结果对象
     * @param request       请求对象
     * @return 返回前端页面接收对象
     * @see OperateResultWithData
     */
    protected Object onAfterDoSave(OperateStatus operateStatus, HttpServletRequest request) {
        if (Objects.isNull(operateStatus)) {
            operateStatus = OperateStatus.defaultFailure();
        }
        return operateStatus;
    }

    /**
     * 删除
     * @param id      欲删除实体的id
     * @param request 请求对象
     * @return 返回前端页面接收对象
     */
    @ResponseBody
    @RequestMapping(value = Contants.REQUEST_DELETE, method = {RequestMethod.POST, RequestMethod.DELETE})
    public Object delete(@RequestParam(value = "id") String id, HttpServletRequest request) {
        //删除操作前
        OperateStatus operateStatus = onBeforeDelete(id, request);

        if (Objects.nonNull(operateStatus) && operateStatus.isSuccess()) {
            //执行删除
            OperateResult delResult = doSubmitDelete(id, request);

            operateStatus.setSuccess(delResult.successful());
            operateStatus.setMsg(delResult.getMessage());
        }
        //删除后操作
        return onAfterDelete(operateStatus, request);
    }

    /**
     * 删除操作前,根据需要在子类重载.
     */
    protected OperateStatus onBeforeDelete(String id, HttpServletRequest request) {
        return OperateStatus.defaultSuccess();
    }

    /**
     * 执行删除
     * @param id      与删除实体的id
     * @param request 请求对象
     * @return 返回删除操作结果
     */
    protected OperateResult doSubmitDelete(String id, HttpServletRequest request) {
        return getBaseEntityService().delete(id);
    }

    /**
     * 删除操作后,根据需要在子类重载.
     * @param operateStatus 删除操作结果
     * @param request       请求对象
     * @return 返回前端页面接收对象
     */
    protected Object onAfterDelete(OperateStatus operateStatus, HttpServletRequest request) {
        if (Objects.isNull(operateStatus)) {
            operateStatus = OperateStatus.defaultFailure();
        }
        return operateStatus;
    }
}
