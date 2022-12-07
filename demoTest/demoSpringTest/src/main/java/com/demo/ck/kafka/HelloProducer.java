package com.demo.ck.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @project_name: kikock-sei
 * @description:
 * @create_name: kikock
 * @create_date: 2022-12-06 15:02
 **/
@RestController
public class HelloProducer {
    private final static String TOPIC_NAME = "zhTest"; //topic的名称
    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @PostMapping("/send/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(TOPIC_NAME, msg);
        System.out.println("发送结果：" + future.get().toString());
        return "发送成功";
    }

    @PostMapping("/send/syn/{msg}")
    public String sendMsgSyn(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {

        ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(TOPIC_NAME, msg);

        future.addCallback(new ListenableFutureCallback() {

            @Override
            public void onSuccess(Object result) {
                System.out.println("发送成功 result = " + result);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送异常");
                ex.printStackTrace();
            }
        });

        return "发送成功";
    }

}
