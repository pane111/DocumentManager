package com.fhtw.ocrservice;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "documentExchange";
    public static final String QUEUE = "ocrQueue";
    public static final String ROUTING_KEY = "document.uploaded";
    public static final String CONFIRM_QUEUE = "confirmQueue";

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE).build();
    }
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

}
