package com.demo.ck.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @project_name: kikock-sei
 * @description:
 * @create_name: kikock
 * @create_date: 2022-12-06 15:06
 **/
@Component
public class HelloConsumer {
    private final static String TOPIC_NAME = "zhTest"; //topic的名称

    //kafka的监听器，topic为"zhTest"，消费者组为"zhTestGroup"
    @KafkaListener(topics = TOPIC_NAME, groupId = "zhTestGroup")
    public void listenZhugeGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        //手动提交offset
        ack.acknowledge();
    }
}


