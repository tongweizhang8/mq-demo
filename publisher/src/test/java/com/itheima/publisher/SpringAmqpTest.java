package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void testMassage2Queue() {
        String queueName = "simple.queue";
        String message = "hello spring amqp";
        rabbitTemplate.convertAndSend(queueName, message);
    }
    @Test
    void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        for (int i = 0; i <= 50; i++) {
            String message = "work queue message " + i;
            rabbitTemplate.convertAndSend(queueName, message);
            Thread.sleep(20);
        }
    }
    @Test
    void testSendFanout() {
        String exchangeName = "hmall.fanout";
        String message = "hello fanout";
        rabbitTemplate.convertAndSend(exchangeName, "", message);

    }
    @Test
    void testSendTopic() {
        String exchangeName = "hmall.topic";
        String message = "hello topic";
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", message);

    }

    @Test
    void testConfirmCallBack() {
        //1.创建cd
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        //2.添加confirmCallBack
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("发送消息失败:{}", ex.getMessage());
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                log.info("发送消息成功");
                if (result.isAck()) {
                    //消息发送成功
                    log.info("发送消息成功,收到ack");
                } else {
                    //消息发送失败
                    log.error("发送消息失败,收到nack:{}", result.getReason());
                }
            }
        });
    }

    @Test
    void testPageOut(){
        Message message = MessageBuilder
                .withBody("hello rabbitmq".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();
        for (int i = 0; i <= 1000000; i++) {
            rabbitTemplate.convertAndSend("simple.queue",message);
        }
    }

    @Test
    void testTTLMessage(){
        rabbitTemplate.convertAndSend("simple.exchange", "hi","hello", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
        log.info("发送消息成功");
    }

    @Test
    void testDelayMessage() {
        rabbitTemplate.convertAndSend("delay.direct", "hi","hello", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(10000);
                return message;
            }
        });
        log.info("发送消息成功");
    }
}
