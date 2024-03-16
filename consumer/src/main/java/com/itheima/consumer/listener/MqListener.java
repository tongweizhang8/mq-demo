package com.itheima.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MqListener {

    //@RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String message) {
        log.info("simple.queue receive message: {}", message);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWork1Queue(String message) {
        log.info("work1.queue receive message: {}", message);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWork2Queue(String message) {
        log.info("work2.queue receive message: {}", message);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanout1Queue(String message) {
        log.info("fanout1.queue receive message: {}", message);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanout2Queue(String message) {
        log.info("fanout2.queue receive message: {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1",durable = "true"),
            exchange = @Exchange(name = "hmall.direct",type = "ExchangeTypes.DIRECT"),
            key = {"red","blue"}
    ))

    @RabbitListener(queues = "topic.queue1")
    public void listenTopic1Queue(String message) {
        log.info("topic1.queue receive message: {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2",durable = "true"),
            exchange = @Exchange(name = "hmall.direct",type = "ExchangeTypes.DIRECT"),
            key = {"red","yellow"}
    ))

    @RabbitListener(queues = "topic.queue2")
    public void listenTopic2Queue(String message) {
        log.info("topic2.queue receive message: {}", message);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String,Object> message) {
        log.info("object.queue receive message: {}", message);
    }

    @RabbitListener(queuesToDeclare = @Queue(
            name = "lazy.queue",
            durable = "true",
            arguments = @Argument(name = "x-queue-mode",value = "lazy")
    ))
    public void listenLazyQueue(String msg) {
        log.info("收到lazy.queue的消息：{}",msg);
    }

    @RabbitListener(queues = "dlx.queue")
    public void listenDlxQueue(String message) {
        log.info("dlx.queue receive message: {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delay.queue",durable = "true"),
            exchange = @Exchange(value = "delay.direct",delayed = "true"),
            key = "hi"
    ))
    public void listenDelayQueue(String message) {
        log.info("delay.queue receive message: {}", message);
    }
}
