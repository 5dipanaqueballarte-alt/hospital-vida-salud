package com.hospital.auditoria.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_CITAS = "citas.exchange";
    public static final String COLA_AUDITORIA = "auditoria.citas.queue";
    public static final String ROUTING_KEY = "cita.*";

    @Bean
    public TopicExchange citasExchange() {
        return new TopicExchange(EXCHANGE_CITAS, true, false);
    }

    @Bean
    public Queue colaAuditoria() {
        return new Queue(COLA_AUDITORIA, true);
    }

    @Bean
    public Binding bindingAuditoria(Queue colaAuditoria, TopicExchange citasExchange) {
        return BindingBuilder.bind(colaAuditoria).to(citasExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}