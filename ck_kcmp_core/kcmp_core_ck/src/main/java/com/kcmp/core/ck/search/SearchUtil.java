package com.kcmp.core.ck.search;

import com.kcmp.ck.context.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kikock
 * 查询工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class SearchUtil {

    /**
     * 获取查询配置（有分页信息）
     */
    public static Search genSearch(ServletRequest request) {
        List<SearchFilter> filters = getFilters(request);
        List<SearchOrder> sortOrders = getSorts(request);
        PageInfo info = getPageInfo(request);

        Search search = new Search(filters, sortOrders, info);
        String quickSearchValue = request.getParameter("Quick_value");
        if (StringUtils.isNotBlank(quickSearchValue)) {
            search.setQuickSearchValue(quickSearchValue);
        }
        return search;
    }

    /**
     * 获取查询配置（无分页信息）
     */
    public static Search genNoPageSearch(ServletRequest request) {
        List<SearchFilter> filters = getFilters(request);
        List<SearchOrder> sortOrders = getSorts(request);
        Search search = new Search(filters, sortOrders, null);
        String quickSearchValue = request.getParameter("Quick_value");
        if (StringUtils.isNotBlank(quickSearchValue)) {
            search.setQuickSearchValue(quickSearchValue);
        }
        return search;
    }

    /**
     * 根据查询参数构造查询字段Map
     * @param searchParams
     * @return 查询字段map
     */
    public static List<SearchFilter> getFilters(Map<String, Object> searchParams) {
        List<SearchFilter> filters = new ArrayList<SearchFilter>();

        SearchFilter filter;
        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            String key = entry.getKey();
            String fieldType = "String";
            if (key.contains("__")) {
                String[] tmps = key.split("__");
                key = tmps[0];
                fieldType = tmps[1];
            }
            Object value = entry.getValue();
            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length != 2) {
                //查询参数错误
                throw new IllegalArgumentException(ContextUtil.getMessage("kcmp_web_00001", key));
            }
            String filedName = names[1];
            SearchFilter.Operator operator = SearchFilter.Operator.valueOf(names[0]);

            // 创建searchFilter
            filter = new SearchFilter(filedName, value, operator, fieldType);
            filters.add(filter);
        }

        return filters;
    }

    /**
     * 根据Request请求构造查询字段Map
     * @param request 查询参数示例：Q_EQ_name
     * @return
     */
    public static List<SearchFilter> getFilters(ServletRequest request) {
        Map<String, Object> searchParams = getParametersStartingWith(request, "Q_");
        return getFilters(searchParams);
    }

    /**
     * 获取排序字段
     * @param request
     * @return
     */
    public static List<SearchOrder> getSorts(ServletRequest request) {
        List<SearchOrder> sortOrders = new ArrayList<SearchOrder>();
        //获取表格排序字段
        String sord = request.getParameter("sord");
        String sidx = request.getParameter("sidx");
        if (StringUtils.isNotBlank(sidx)) {
            if (StringUtils.isNotBlank(sord)) {
                sortOrders.add(new SearchOrder(sidx, "desc".equalsIgnoreCase(sord) ? SearchOrder.Direction.DESC : SearchOrder.Direction.ASC));
            } else {
                //默认降序排列
                sortOrders.add(new SearchOrder(sidx, SearchOrder.Direction.DESC));
            }
        }
        //获取参数中的排序字段,S_name
        Map<String, Object> sortParams = getParametersStartingWith(request, "S_");
        for (String key : sortParams.keySet()) {
            sortOrders.add(
                    new SearchOrder(key,
                            "desc".equalsIgnoreCase(sortParams.get(key).toString()) ? SearchOrder.Direction.DESC : SearchOrder.Direction.ASC));
        }
        if (sortOrders.isEmpty()) {
            return null;
        }
        return sortOrders;
    }

    /**
     * 获取分页信息
     * @param request
     * @return
     */
    public static PageInfo getPageInfo(ServletRequest request) {
        PageInfo info = new PageInfo();
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if (StringUtils.isNotBlank(page)) {
            info.setPage(Integer.parseInt(page));
        }
        if (StringUtils.isNotBlank(rows)) {
            info.setRows(Integer.parseInt(rows));
        }
        return info;
    }

    /**
     * Return a map containing all parameters with the given prefix.
     * Maps single values to String and multiple values to String array.
     * <p>For example, with a prefix of "spring_", "spring_param1" and
     * "spring_param2" result in a Map with "param1" and "param2" as keys.
     * @param request HTTP request in which to look for parameters
     * @param prefix the beginning of parameter names
     * (if this is null or the empty string, all parameters will match)
     * @return map containing request parameters <b>without the prefix</b>,
     * containing either a String or a String array as values
     * @see javax.servlet.ServletRequest#getParameterNames
     * @see javax.servlet.ServletRequest#getParameterValues
     * @see javax.servlet.ServletRequest#getParameterMap
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, @Nullable String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                }
                else if (values.length > 1) {
                    params.put(unprefixed, values);
                }
                else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }
}
