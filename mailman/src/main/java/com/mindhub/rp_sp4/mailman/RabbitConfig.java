package com.mindhub.rp_sp4.mailman;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "orders_exchange";
    public static final String ORDERS_ROUTING_KEY = "orders_routing_key";
    public static final String ORDERS_QUEUE_NAME = "orders_queue";

    public static final String USERS_EXCHANGE = "users_exchange";
    public static final String USERS_ROUTING_KEY = "users_routing_key";
    public static final String USERS_QUEUE_NAME = "users_queue";

    // Declare exchange 1
    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // Declare exchange 2
    @Bean
    public DirectExchange usersExchange() {
        return new DirectExchange(USERS_EXCHANGE);
    }

    // Declare queue
    @Bean
    public Queue ordersQueue() {
        return new Queue(ORDERS_QUEUE_NAME);
    }

    @Bean
    public Queue usersQueue() {
        return new Queue(USERS_QUEUE_NAME);
    }

    // Bind queue to exchange 1
    @Bean
    public Binding ordersBinding() {
        return BindingBuilder.bind(ordersQueue()).to(ordersExchange()).with(ORDERS_ROUTING_KEY);
    }

    @Bean
    public Binding usersBinding() {
        return BindingBuilder.bind(usersQueue()).to(usersExchange()).with(USERS_ROUTING_KEY);
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
