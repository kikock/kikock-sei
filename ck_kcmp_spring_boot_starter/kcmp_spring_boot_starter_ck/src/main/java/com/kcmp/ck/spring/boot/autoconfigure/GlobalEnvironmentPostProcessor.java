package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.ck.context.ConfigConstants;
import com.kcmp.ck.context.Version;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by kikock
 * 全局环境配置
 *
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public class GlobalEnvironmentPostProcessor extends KCMPEnvironmentPostProcessor {

    @Override
    public PropertiesPropertySource setKCMPDefaultConfig(ConfigurableEnvironment environment, Properties properties) {
        //初始化KCMP运行环境配置
        initKCMPEnvironment(environment, properties);

        //是否是本地AppId从配置中心加载配置，以此判定是否是本地开发环境(本地开发环境运行Mock User)
        properties.setProperty("isLocalConfig", String.valueOf(isLocalConfig));

        //会检查终端是否支持ANSI，是的话就采用彩色输出。设置彩色输出会让日志更具可读性
        //properties.setProperty("spring.output.ansi.enabled", "DETECT");
        //始终采用彩色输出
        properties.setProperty("spring.output.ansi.enabled", "ALWAYS");

        //开启AOP
        properties.setProperty("spring.aop.auto", "true");

        /*
            # 启动显示信息 相关配置
            #----------------------------------
            #spring.banner.charset=utf-8
            #spring.banner.location=classpath:banner.txt
            #spring.banner.image.location=classpath:static/images/canvas.png
            #spring.banner.image.width=50
            #spring.banner.image.height=50
            #spring.banner.image.margin=10
        */

        //设置当前运行环境是否是servlet 容器环境。
        MutablePropertySources sources = environment.getPropertySources();
        if (sources.contains("servletConfigInitParams")) {
            properties.setProperty("ServletContainerEnvironment", "true");
        }

        //logback-spring.xml 文件使用
        HashMap map = (HashMap) properties.get("SELF_APP_SERVICE");
        String appCode;
        String env;
        if (Objects.nonNull(map)) {
            appCode = (String) map.get("SELF_APP");
            System.setProperty("appCode", appCode);
            properties.putIfAbsent("spring.jmx.default-domain", appCode);
            //设置应用名称
            properties.setProperty("spring.application.name", appCode);

            env = (String) map.get("SELF_ENV");
            System.setProperty("envCode", env);
        }
        //版本
        System.setProperty("kcmp-version", Version.getCurrentVersion());

        if (!isLocalConfig &&
                environment.getProperty("kcmp.log.efk.enable", Boolean.class, Boolean.TRUE)) {
            //日志采集器
            String value;
            Map<String, String> data = (Map<String, String>) properties.get("KCMP_FLUENTD_HOST");
            if (data != null && !data.isEmpty()) {
                value = data.get("KCMP_FLUENTD_HOST");
                if (StringUtils.isNotBlank(value)) {
                    System.setProperty("FlentdHost", value);
                }
            }
            data = (Map<String, String>) properties.get("KCMP_FLUENTD_PORT");
            if (data != null && !data.isEmpty()) {
                value = data.get("KCMP_FLUENTD_PORT");
                if (StringUtils.isNotBlank(value)) {
                    System.setProperty("FlentdPort", value);
                }
            }
            //指定配置efk文件
            properties.setProperty("logging.config", "classpath:logback-efk.xml");
        }

        setDataSource(properties);

        setRedis(properties);

        setMongo(properties);

        setKafka(properties);

        setZipkin(properties);

        //        //开启gRPC
        //        properties.put("spring.grpc.enable", Boolean.TRUE);
        //        //服务端RPC端口
        //        properties.put("spring.grpc.port", 7070);

        //Elasticsearch  http://blog.51cto.com/shangdc/2096226

        return new PropertiesPropertySource("KCMP-Gloabl-Config", properties);
    }

    @Override
    public int getOrder() {
        return -999999;
    }

    private void setRedis(Properties properties) {
        boolean redis = properties.containsKey("spring.redis.cluster.nodes") || properties.containsKey("spring.redis.host");
        if (redis) {
            if (!properties.containsKey("spring.redis.timeout")) {
                //连接超时时间（毫秒）
                properties.setProperty("spring.redis.timeout", "3000");
            }
            if (!properties.containsKey("spring.redis.jedis.pool.max-idle")) {
                //连接池中的最大空闲连接
                properties.setProperty("spring.redis.jedis.pool.max-idle", "8");
            }
            if (!properties.containsKey("spring.redis.jedis.pool.min-idle")) {
                //连接池中的最小空闲连接
                properties.setProperty("spring.redis.jedis.pool.min-idle", "0");
            }
            if (!properties.containsKey("spring.redis.jedis.pool.max-wait")) {
                //连接池最大阻塞等待时间（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-wait", "-1");
            }
            if (!properties.containsKey("spring.redis.jedis.pool.max-active")) {
                //连接池最大连接数（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-active", "8");
            }

            //@see CacheType#REDIS
            properties.setProperty("spring.cache.type", "redis");
            //缓存的名字(可以不指定)
            //properties.setProperty("spring.cache.cache-names", "redis_cache, ehcache");
            if (!properties.containsKey("spring.cache.redis.time-to-live")) {
                //很重要，缓存的有效时间，以便缓存的过期（单位为毫秒）
                properties.setProperty("spring.cache.redis.time-to-live", "3600000");
            }
        } else {
            if (properties.containsKey(ConfigConstants.PARAM_BIZ_CACHE)) {
                Map<String, String> data = (Map<String, String>) properties.get(ConfigConstants.PARAM_BIZ_CACHE);
            /*
                Redis服务器地址。如果配置了集群，则集群的配置会覆盖host设置的地址
                spring.redis.cluster.nodes
            */
                if (data.containsKey("cluster.nodes")) {
                    properties.setProperty("spring.redis.cluster.nodes", data.get("cluster.nodes"));
                } else {
                    //对应redis所在的IP地址
                    properties.setProperty("spring.redis.host", StringUtils.isNotBlank(data.get("host")) ? data.get("host") : "localhost");
                    //redis数据库对应的端口号
                    properties.setProperty("spring.redis.port", StringUtils.isNumeric(data.get("port")) ? data.get("port") : "6379");
                }

                if (StringUtils.isNotBlank(data.get("password"))) {
                    properties.setProperty("spring.redis.password", data.get("password"));
                }
                //使用第1个数据库，一共默认有10个(0-15)
                properties.setProperty("spring.redis.database", StringUtils.isNumeric(data.get("db")) ? data.get("db") : "0");


                //连接超时时间（毫秒）
                properties.setProperty("spring.redis.timeout", "3000");
                //连接池中的最大空闲连接
                properties.setProperty("spring.redis.jedis.pool.max-idle", "8");
                //连接池中的最小空闲连接
                properties.setProperty("spring.redis.jedis.pool.min-idle", "0");
                //连接池最大阻塞等待时间（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-wait", "-1");
                //连接池最大连接数（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-active", "8");

                //@see CacheType#REDIS
                properties.setProperty("spring.cache.type", "redis");
                //缓存的名字(可以不指定)
                //properties.setProperty("spring.cache.cache-names", "redis_cache, ehcache");
                //很重要，缓存的有效时间，以便缓存的过期（单位为毫秒）
                properties.setProperty("spring.cache.redis.time-to-live", "60000");

                properties.remove(ConfigConstants.PARAM_BIZ_CACHE);
            } else if (properties.containsKey(ConfigConstants.PARAM_SESSION_CACHE)) {
                Map<String, String> data = (Map<String, String>) properties.get(ConfigConstants.PARAM_SESSION_CACHE);
            /*
                Redis服务器地址。如果配置了集群，则集群的配置会覆盖host设置的地址
                spring.redis.cluster.nodes
            */
                if (data.containsKey("cluster.nodes")) {
                    properties.setProperty("spring.redis.cluster.nodes", data.get("cluster.nodes"));
                } else {
                    //对应redis所在的IP地址
                    properties.setProperty("spring.redis.host", StringUtils.isNotBlank(data.get("host")) ? data.get("host") : "localhost");
                    //redis数据库对应的端口号
                    properties.setProperty("spring.redis.port", StringUtils.isNumeric(data.get("port")) ? data.get("port") : "6379");
                }

                if (StringUtils.isNotBlank(data.get("password"))) {
                    properties.setProperty("spring.redis.password", data.get("password"));
                }
                //使用第1个数据库，一共默认有10个(0-15)
                properties.setProperty("spring.redis.database", StringUtils.isNumeric(data.get("db")) ? data.get("db") : "0");

                //连接超时时间（毫秒）
                properties.setProperty("spring.redis.timeout", "3000");
                //连接池中的最大空闲连接
                properties.setProperty("spring.redis.jedis.pool.max-idle", "8");
                //连接池中的最小空闲连接
                properties.setProperty("spring.redis.jedis.pool.min-idle", "0");
                //连接池最大阻塞等待时间（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-wait", "-1");
                //连接池最大连接数（使用负值表示没有限制）
                properties.setProperty("spring.redis.jedis.pool.max-active", "8");

                //spring-session 使用redis实现session共享
                properties.setProperty("spring.session.store-type", "redis");
                //定义写入redis的时机
                properties.setProperty("spring.session.redis.flush-mode", "IMMEDIATE");
                properties.setProperty("spring.session.redis.namespace", "spring:session");
                //定义session的超时时间，如果不存在则默认设置3600
                properties.putIfAbsent("server.servlet.session.timeout", "3600");

                properties.remove(ConfigConstants.PARAM_SESSION_CACHE);
            }
        }
    }

    private void setMongo(Properties properties) {
        if (properties.containsKey(ConfigConstants.PARAM_EDM_MONGODB)) {
            properties.setProperty(ConfigConstants.SWITCH_EDM_KEY, "true");

            Map<String, String> data = (Map<String, String>) properties.get(ConfigConstants.PARAM_EDM_MONGODB);

            /*
                mongodb://name:pass@localhost:27017/test
                多ip集群
                mongodb://user:pwd@ip1:port1,ip2:port2/database
            */
            // 兼容集群配置
            StringBuilder uri = new StringBuilder();
            uri.append("mongodb://");
            uri.append(data.get("user")).append(":").append(data.get("password"));
            uri.append("@");
            uri.append(data.get("url"));
            uri.append("/").append(data.get("database"));
            properties.setProperty("spring.data.mongodb.uri", uri.toString());
            properties.setProperty("spring.data.mongodb.database", data.get("database"));
            //            properties.setProperty("spring.data.mongodb.username", data.get("user"));
            //            properties.setProperty("spring.data.mongodb.password", data.get("password"));

            properties.remove(ConfigConstants.PARAM_EDM_MONGODB);
        } else if (properties.containsKey("spring.data.mongodb.uri") || properties.containsKey("spring.data.mongodb.host")) {
            properties.setProperty(ConfigConstants.SWITCH_EDM_KEY, "true");
        } else {
            excludeConfigClass(MongoDataAutoConfiguration.class);
        }
    }

    private void setDataSource(Properties properties) {
        String name = properties.getProperty("spring.application.name");

        if (properties.containsKey(ConfigConstants.PARAM_DATASOURCE)) {
            Map<String, String> data = (Map<String, String>) properties.get(ConfigConstants.PARAM_DATASOURCE);
            properties.setProperty("spring.datasource.url", data.get("url"));
            properties.setProperty("spring.datasource.username", data.get("username"));
            properties.setProperty("spring.datasource.password", data.get("password"));
            properties.setProperty("spring.datasource.driver-class-name", data.get("driverClassName"));

            //连接池配置
            properties.setProperty("spring.datasource.type", "com.zaxxer.hikari.HikariDataSource");
            String val = data.get("initialSize");
            properties.setProperty("spring.datasource.hikari.minimum-idle", (StringUtils.isNumeric(val) && Integer.parseInt(val) > 10) ? val : "10");
            val = data.get("maxActive");
            properties.setProperty("spring.datasource.hikari.maximum-pool-size", (StringUtils.isNumeric(val) && Integer.parseInt(val) > 50) ? val : "50");
            properties.setProperty("spring.datasource.hikari.auto-commit", "true");
            properties.setProperty("spring.datasource.hikari.idle-timeout", "30000");
            if (StringUtils.isNotBlank(name)) {
                properties.setProperty("spring.datasource.hikari.pool-name", name + "-DS");
            } else {
                properties.setProperty("spring.datasource.hikari.pool-name", "DatebookHikariCP");
            }
            properties.setProperty("spring.datasource.hikari.max-lifetime", "1800000");
            properties.setProperty("spring.datasource.hikari.connection-timeout", "30000");
            if (StringUtils.containsIgnoreCase(data.get("url"), "oracle")) {
                properties.setProperty("spring.datasource.hikari.connection-test-query", "SELECT 1 FROM DUAL");
            } else {
                properties.setProperty("spring.datasource.hikari.connection-test-query", "SELECT 1");
            }
            properties.remove(ConfigConstants.PARAM_DATASOURCE);
            setJPA(properties);
        } else {
            String dbUrl = properties.getProperty("spring.datasource.url");
            if (StringUtils.isNotBlank(dbUrl)) {
                //连接池配置
                if (!properties.containsKey("spring.datasource.type")) {
                    properties.setProperty("spring.datasource.type", "com.zaxxer.hikari.HikariDataSource");
                }
                if (!properties.containsKey("spring.datasource.hikari.minimum-idle")) {
                    properties.setProperty("spring.datasource.hikari.minimum-idle", "10");
                }
                if (!properties.containsKey("spring.datasource.hikari.maximum-pool-size")) {
                    properties.setProperty("spring.datasource.hikari.maximum-pool-size", "10");
                }
                if (!properties.containsKey("spring.datasource.hikari.auto-commit")) {
                    properties.setProperty("spring.datasource.hikari.auto-commit", "true");
                }
                if (!properties.containsKey("spring.datasource.hikari.idle-timeout")) {
                    properties.setProperty("spring.datasource.hikari.idle-timeout", "30000");
                }
                if (!properties.containsKey("spring.datasource.hikari.max-lifetime")) {
                    properties.setProperty("spring.datasource.hikari.max-lifetime", "1800000");
                }
                if (!properties.containsKey("spring.datasource.hikari.connection-timeout")) {
                    properties.setProperty("spring.datasource.hikari.connection-timeout", "30000");
                }
                if (StringUtils.containsIgnoreCase(dbUrl, "oracle")) {
                    properties.setProperty("spring.datasource.hikari.connection-test-query", "SELECT 1 FROM DUAL");
                } else {
                    properties.setProperty("spring.datasource.hikari.connection-test-query", "SELECT 1");
                }
                if (!properties.containsKey("spring.datasource.hikari.pool-name")) {
                    if (StringUtils.isNotBlank(name)) {
                        properties.setProperty("spring.datasource.hikari.pool-name", name + "-DS");
                    }
                }
                setJPA(properties);
            }
        }
    }

    private void setJPA(Properties properties) {
        if (!properties.containsKey("spring.jpa.properties.hibernate.hbm2ddl.auto")) {
            properties.setProperty("spring.jpa.properties.hibernate.hbm2ddl.auto", "none");
        }
    }

    private void setKafka(Properties properties) {
        if (properties.containsKey(ConfigConstants.MQ_CONFIG_KEY)) {
            Map<String, String> data = (Map<String, String>) properties.get(ConfigConstants.MQ_CONFIG_KEY);
            //消费者
            properties.setProperty("spring.kafka.consumer.bootstrap-servers", data.get("bootstrap.servers"));
            properties.setProperty("spring.kafka.consumer.enable-auto-commit", "true");
            properties.setProperty("spring.kafka.consumer.session-timeout", "6000");
            properties.setProperty("spring.kafka.consumer.auto-commit-interval", "100");
            properties.setProperty("spring.kafka.consumer.auto-offset-reset", "latest");
            //properties.setProperty("spring.kafka.consumer.topic", data.get("bootstrap.servers"));
            properties.setProperty("spring.kafka.consumer.group-id", "kcmp_notify_jx");
            properties.setProperty("spring.kafka.consumer.concurrency", "10");

            //生产者
            properties.setProperty("spring.kafka.producer.bootstrap-servers", data.get("bootstrap.servers"));
            properties.setProperty("spring.kafka.producer.retries", "0");
            properties.setProperty("spring.kafka.producer.batch-size", "100");
            properties.setProperty("spring.kafka.producer.buffer-memory", "40960");
        }
    }

    private void setZipkin(Properties properties) {
        if (properties.containsKey(ConfigConstants.ZIPKIN_SERVER_KEY)) {
            properties.setProperty(ZipkinTracingConfiguration.CONFIG_KEY, "true");
        }
    }
}
