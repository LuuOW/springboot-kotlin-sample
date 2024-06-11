package com.cropyback.cropyback.api.websocket.queue

import MessageService
import com.cropyback.cropyback.api.websocket.ChatMessage
import com.cropyback.cropyback.api.websocket.SessionRegistry
import com.rabbitmq.client.Channel
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage


@Service
class QueueConsumer(
    private val sessionRegistry: SessionRegistry,
    private val messageService: MessageService,
    private val logger: Logger
){

    @RabbitListener(queues = [RabbitMQConfig.QUEUE_NAME], ackMode = "MANUAL")
    fun receiveMessage(message: ChatMessage, channel: Channel, @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long) {


        var recipientConnection = sessionRegistry.getSessionById(message.recipientId)
        var count = 0

        CoroutineScope(Dispatchers.IO + Job()).launch {
            while (count < 5) {
                if (recipientConnection == null) {
                    delay(1000)
                    recipientConnection = sessionRegistry.getSessionById(message.recipientId)
                    delay(1000)
                    recipientConnection?.sendMessage(TextMessage(message.messageContent))
                    delay(1000)
                    ++count
                } else {
                    channel.basicAck(deliveryTag, false)
                }
            }
        }
    }

}
