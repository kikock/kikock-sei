package com.kcmp.ck.center.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by kikock
 * Kafka生产者服务
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.serialnumber}")
    private String kafkaTopic;

    /**
     * 发送消息
     *
     * @param key     消息键值
     * @param message 消息
     */
    public void send(String key, String message) {
        kafkaTemplate.send(kafkaTopic, key, message);
    }
}
