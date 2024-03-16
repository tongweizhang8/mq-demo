package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {
    @Bean
    public FanoutExchange fanoutExchange() {
        //ExchangeBuilder.fanoutExchange("").build();
        return new FanoutExchange("fanout2.exchange");
    }
    @Bean
    public Queue fanoutQueue3() {
        //QueueBuilder.durable("").build();
        return new Queue("fanout.queue3");
    }
    @Bean
    public Binding fanoutbinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange());
    }
    @Bean
    public Queue fanoutQueue4() {
        //QueueBuilder.durable("").build();
        return new Queue("fanout.queue4");
    }
    @Bean
    public Binding fanoutbinding4() {
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());
    }
}
