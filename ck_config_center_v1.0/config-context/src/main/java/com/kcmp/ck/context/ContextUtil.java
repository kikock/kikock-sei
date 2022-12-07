package com.kcmp.ck.context;


import com.kcmp.ck.config.enums.UserAuthorityPolicy;
import com.kcmp.ck.config.enums.UserType;
import com.kcmp.ck.config.util.ApiClient;
import com.kcmp.ck.config.util.EnumUtils;
import com.kcmp.ck.config.util.IdGenerator;
import com.kcmp.ck.config.util.JwtTokenUtil;
import com.kcmp.ck.context.common.ConfigConstants;
import com.kcmp.ck.vo.LoginStatus;
import com.kcmp.ck.vo.SessionUser;
import com.kcmp.ck.vo.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>kcmp平台上下文工具类</p>
 *
 * @author kikock
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public class ContextUtil extends BaseContextSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextUtil.class);

    //InheritableThreadLocal
    private static ThreadLocal<SessionUser> userTokenHold = new InheritableThreadLocal<>();

    public static final String REQUEST_TOKEN_KEY = "_s";
    public static final String AUTHORIZATION_KEY = "Authorization";

    /**
     * @param key  多语言key
     * @param args 填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @return 返回语意
     */
    public static String getMessage(String key, Object... args) {
        return getMessage(key, args, getLocale());
    }

    /**
     * @return 返回当前语言环境
     */
    public static Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * 设置当前语言环境
     *
     * @param locale 多语言环境
     */
    public static void setLocale(Locale locale) {
        if (locale != null) {
            SessionUser sessionUser = getSessionUser();
            if (sessionUser != null && Objects.nonNull(sessionUser.getAccessToken())) {
                sessionUser.setLocale(locale.getLanguage());
                setSessionUser(sessionUser);
            }
        }
    }

    /**
     * 获取当前服务器IP
     *
     * @return 当前服务器ip
     */
    public static String getHost() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前会话用ID
     *
     * @return 返回当前会话用户ID。无会话信息，则返回anonymous
     */
    public static String getUserId() {
        return getSessionUser().getUserId();
    }

    /**
     * 获取当前会话用户账号
     *
     * @return 返回当前会话用户账号。无会话信息，则返回anonymous
     */
    public static String getUserAccount() {
        return getSessionUser().getAccount();
    }

    /**
     * 获取当前会话用户名
     *
     * @return 返回当前会话用户名。无会话信息，则返回anonymous
     */
    public static String getUserName() {
        return getSessionUser().getUserName();
    }

    /**
     * 获取当前会话用户信息
     * 返回格式：
     * 租户,用户名[账号]
     *
     * @return 返回当前会话用户信息。无会话信息，则返回anonymous
     */
    public static String getUserInfo() {
        return getSessionUser().getUserInfo();
    }

    /**
     * 获取当前会话租户代码
     *
     * @return 返回当前租户代码
     */
    public static String getTenantCode() {
        return getSessionUser().getTenantCode();
    }

    /**
     * @return 返回当前会话用户
     */
    public static SessionUser getSessionUser() {
        SessionUser sessionUser = userTokenHold.get();
        if (sessionUser == null) {
            sessionUser = new SessionUser();
        }

        /*Thread currentThread = Thread.currentThread();
        LOGGER.debug(currentThread.getName() + "  |  " + currentThread.toString() + "  |  " + sessionUser);*/
        return sessionUser;
    }

    /**
     * @param user 设置用户会话信息
     */
    public static void setSessionUser(SessionUser user) {
        userTokenHold.remove();
        if (user != null) {
            userTokenHold.set(user);
        } else {
            throw new RuntimeException("设置会话用户时，SessionUser不能为空。");
        }
    }

    /**
     * 是否匿名用户
     *
     * @return 返回true，则匿名用户；反之非匿名用户
     */
    public static boolean isAnonymous() {
        boolean isAnonymous = true;
        SessionUser sessionUser = getSessionUser();
        if (sessionUser != null && !sessionUser.isAnonymous()) {
            isAnonymous = false;
        }
        return isAnonymous;
    }

    /**
     * 清楚用户token信息
     */
    public static void cleanUserToken() {
        userTokenHold.remove();
    }

    /**
     * @return 返回AccessToken
     */
    public static String getAccessToken() {
        return getSessionUser().getAccessToken();
    }

    public static String generateToken(SessionUser sessionUser) {
        JwtTokenUtil jwtTokenUtil;
        try {
            jwtTokenUtil = getBean(JwtTokenUtil.class);
        } catch (Exception e) {
            jwtTokenUtil = new JwtTokenUtil();
        }
        String randomKey = sessionUser.getSessionId();
        if (StringUtils.isBlank(randomKey)) {
            randomKey = IdGenerator.uuid();
            sessionUser.setSessionId(randomKey);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("appId", getAppId());
        claims.put("tenant", sessionUser.getTenantCode());
        claims.put("account", sessionUser.getAccount());
        claims.put("userId", sessionUser.getUserId());
        claims.put("userName", sessionUser.getUserName());
        claims.put("userType", sessionUser.getUserType().name());
        claims.put("email", sessionUser.getEmail());
        claims.put("authorityPolicy", sessionUser.getAuthorityPolicy().name());
        claims.put("ip", sessionUser.getIp());
        claims.put("logoutUrl", sessionUser.getLogoutUrl());
        String token = jwtTokenUtil.generateToken(sessionUser.getAccount(), randomKey, claims);
        sessionUser.setAccessToken(token);
        return token;
    }

    public static SessionUser getSessionUser(String token) {
        JwtTokenUtil jwtTokenUtil;
        try {
            jwtTokenUtil = getBean(JwtTokenUtil.class);
        } catch (Exception e) {
            jwtTokenUtil = new JwtTokenUtil();
        }

        SessionUser sessionUser = new SessionUser();
        try {
            Claims claims = jwtTokenUtil.getClaimFromToken(token);
            //sessionUser.setSessionId(jwtTokenUtil.getRandomKeyFromToken(token));
            sessionUser.setSessionId(claims.get(JwtTokenUtil.RANDOM_KEY, String.class));
            sessionUser.setAppId(claims.get("appId", String.class));
            sessionUser.setAccessToken(token);
            sessionUser.setTenantCode(claims.get("tenant", String.class));
            sessionUser.setAccount(claims.get("account", String.class));
            sessionUser.setUserId(claims.get("userId", String.class));
            sessionUser.setUserName(claims.get("userName", String.class));
            sessionUser.setUserType(EnumUtils.getEnum(UserType.class, (String) claims.get("userType")));
            sessionUser.setEmail(claims.get("email", String.class));
            sessionUser.setAuthorityPolicy(EnumUtils.getEnum(UserAuthorityPolicy.class, (String) claims.get("authorityPolicy")));
            sessionUser.setIp(claims.get("ip", String.class));
            sessionUser.setLogoutUrl(claims.get("logoutUrl", String.class));

            ContextUtil.setSessionUser(sessionUser);
        } catch (ExpiredJwtException e) {
            LOGGER.error("token已过期", e);
        } catch (Exception e) {
            LOGGER.error("错误的token", e);
        }
        return sessionUser;
    }

    ///////////////////////////////Mock User////////////////////////////////

    /**
     * 模拟用户
     *
     * @return 返回会话id
     */
    public static SessionUser mockUser() {
        return mockUser(IdGenerator.uuid2());
    }

    public static SessionUser mockUser(String sessionId) {
        String tenant = ContextUtil.getGlobalProperty("mock.user.tenant");
        String account = ContextUtil.getGlobalProperty("mock.user.account");
        return getSessionUser(sessionId, tenant, account);
    }

    /**
     * 供类似定时任务这样的接口调用指定租户、账号，以作接口会话检查
     *
     * @param tenant  租户代码
     * @param account 账号
     * @return 返回用户会话信息喊accessToken
     */
    public static SessionUser setSessionUser(String tenant, String account) {
        return getSessionUser(IdGenerator.uuid2(), tenant, account);
    }

    private static SessionUser getSessionUser(String sessionId, String tenant, String account) {
        if (StringUtils.isBlank(sessionId)) {
            throw new IllegalArgumentException("模拟登录用户时，会话ID不能为空！");
        }

        SessionUser sessionUser = new SessionUser();
        if (!getProperty("isConfigFile", Boolean.class)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tenantCode", StringUtils.isNotBlank(tenant) ? tenant : "");
            params.put("account", account);
            String path = "userAccount/findByAccountAndTenantCode";
            UserInfo userInfo = ApiClient.getEntityViaProxy(ConfigConstants.BASIC_API, path, UserInfo.class, params);
            if (userInfo != null) {
                //        System.out.println(userInfo);
                UserInfo.User user = userInfo.getUser();
                if (user != null) {
                    sessionUser.setUserId(user.getId());
                    sessionUser.setAccount(account);
                    sessionUser.setUserName(user.getUserName());
                    sessionUser.setTenantCode(userInfo.getTenantCode());
                    sessionUser.setUserType(user.getUserType());
                    sessionUser.setAuthorityPolicy(user.getUserAuthorityPolicy());
                    sessionUser.setLoginTime(new Date());
                    sessionUser.setSessionId(sessionId);
                    sessionUser.setLoginStatus(LoginStatus.success);
                } else {
                    throw new RuntimeException("account:[" + account + "], tenant:[" + tenant + "] 用户数据错误！");
                }
            } else {
                throw new RuntimeException("account:[" + account + "], tenant:[" + tenant + "] 用户不存在！");
            }
        } else {
            sessionUser.setUserId(account);
            sessionUser.setAccount(account);
            sessionUser.setUserName(account);
            sessionUser.setTenantCode(tenant);
            sessionUser.setUserType(UserType.Employee);
            sessionUser.setAuthorityPolicy(UserAuthorityPolicy.GlobalAdmin);
            sessionUser.setLoginTime(new Date());
            sessionUser.setSessionId(sessionId);
            sessionUser.setLoginStatus(LoginStatus.success);
        }

        String accessToken = generateToken(sessionUser);
        sessionUser.setAccessToken(accessToken);

        ContextUtil.setSessionUser(sessionUser);
        return sessionUser;
    }
}
