package com.cropyback.cropyback.api.websocket.queue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.rabbitmq.client.AMQP.Channel
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.listener.ContainerProperties


@Configuration
class RabbitMQConfig {
    @Bean
    fun queue(): Queue {
        return Queue(QUEUE_NAME, true) // Durable queue for message persistence
    }

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(EXCHANGE_NAME)
    }

    @Bean
    fun binding(queue: Queue?, exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY)
    }

    @Bean
    fun channel(connectionFactory: ConnectionFactory?): com.rabbitmq.client.Channel? {
        val connection = connectionFactory?.createConnection()
        val channel = connection?.createChannel(true)
        return channel
    }

    @Bean
    fun jackson2JsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(ObjectMapper().registerModule(kotlinModule()))
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory?): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory!!)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }



    companion object {
        const val QUEUE_NAME: String = "offline_messages"
        const val EXCHANGE_NAME: String = "offline_messages"
        const val ROUTING_KEY: String = "my-routing-key"
    }
}
