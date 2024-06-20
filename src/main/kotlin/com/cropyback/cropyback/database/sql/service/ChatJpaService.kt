package com.cropyback.cropyback.database.sql.service

import com.cropyback.cropyback.api.kafka.producer.MessageEventProducer
import com.cropyback.cropyback.api.websocket.ChatMessage
import com.cropyback.cropyback.database.sql.chat.ChatModel
import com.cropyback.cropyback.database.sql.chat.MessageModel
import com.cropyback.cropyback.database.sql.jpa.ChatJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatJpaService(
    private val eventProducer: MessageEventProducer
){

    @Autowired
    lateinit var chatJpaRepository: ChatJpaRepository

    fun saveMessage(message: ChatMessage) {

        val sender = message.senderId
        val receiver = message.recipientId
        val content = message.messageContent
        val attachment = message.attachament

        val existingChat = chatJpaRepository.findChatByParticipants(sender,receiver)

        if (existingChat.isPresent) {
            val updatedChat = existingChat.get().messages.also { it.add(
                MessageModel(
                    sentBy = sender,
                    owner = receiver,
                    attachment = attachment,
                    message = content
                )
            ) }
            chatJpaRepository.save(existingChat.get().copy(messages = updatedChat))
            eventProducer.messageSentEvent(existingChat.get().id.toString(), sender,receiver)
        } else {
            val newChat = ChatModel(messages = mutableListOf(
                MessageModel(
                    sentBy = sender,
                    owner = receiver,
                    attachment = attachment,
                    message = content
                )
            ))
            chatJpaRepository.save(newChat)
            eventProducer.messageSentEvent(newChat.messages.last().id.toString(), sender,receiver)
        }




    }

}