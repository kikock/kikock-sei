package com.kcmp.ck.center.service;

import com.kcmp.ck.center.dao.SerialNumberConfigDao;
import com.kcmp.ck.config.entity.SerialNumberConfig;
import com.kcmp.ck.config.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by kikock
 * Kafka消费者
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Component
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private SerialNumberConfigDao serialNumberConfigDao;

    /**
     * 处理收到的监听消息
     *
     * @param record 消息纪录
     */
    @KafkaListener(topics = "${kafka.topic.serialnumber}")
    public void processMessage(ConsumerRecord<String, String> record) {
        if (Objects.isNull(record)) {
            return;
        }
        log.info("received key='{}' content = '{}'", record.key(), record.value());
        String content = record.value();
        SerialNumberConfig numberConfig = JsonUtils.fromJson(content, SerialNumberConfig.class);
        if (Objects.isNull(numberConfig)) {
            return;
        }
        //更新当前编号到数据库
        try {
            serialNumberConfigDao.save(numberConfig);
        } catch (Exception e) {
            log.error("更新当前编号到数据库失败！", e);
        }
    }
}
