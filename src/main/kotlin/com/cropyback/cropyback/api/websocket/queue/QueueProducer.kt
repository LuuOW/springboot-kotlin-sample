package com.cropyback.cropyback.api.websocket.queue

import com.cropyback.cropyback.api.websocket.ChatMessage
import org.slf4j.Logger
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage


@Component
class QueueProducer(
    private val logger: Logger
) {

    private var rabbitTemplate: RabbitTemplate? = null

    @Autowired
    fun MessageProducer(rabbitTemplate: RabbitTemplate?) {
        this.rabbitTemplate = rabbitTemplate
    }

    fun sendMessage(message: ChatMessage) {
        rabbitTemplate?.convertAndSend(RabbitMQConfig.QUEUE_NAME, message, messagePostProcessor)
    }

    private val messagePostProcessor = MessagePostProcessor { message ->
        val messageProperties = message.messageProperties
        messageProperties.setContentType("application/json")
        messageProperties.setHeader("__TypeId__", ChatMessage::class.java.name)
        message
    }

}