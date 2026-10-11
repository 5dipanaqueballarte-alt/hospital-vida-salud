package com.hospital.notificacion.config;

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
    public static final String COLA_NOTIFICACIONES = "notificaciones.citas.queue";
    public static final String ROUTING_KEY = "cita.*";

    @Bean
    public TopicExchange citasExchange() {
        return new TopicExchange(EXCHANGE_CITAS, true, false);
    }

    @Bean
    public Queue colaNotificaciones() {
        return new Queue(COLA_NOTIFICACIONES, true);
    }

    @Bean
    public Binding bindingNotificaciones(Queue colaNotificaciones, TopicExchange citasExchange) {
        return BindingBuilder.bind(colaNotificaciones).to(citasExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}