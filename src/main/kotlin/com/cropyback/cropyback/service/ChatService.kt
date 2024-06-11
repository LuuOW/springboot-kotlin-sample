package com.cropyback.cropyback.service

import com.cropyback.cropyback.api.kafka.producer.MessageEventProducer
import com.cropyback.cropyback.api.websocket.ChatMessage
import com.cropyback.cropyback.database.entities.chat.ChatEntity
import com.cropyback.cropyback.database.entities.chat.MessageEntity
import com.cropyback.cropyback.database.entities.chat.MessageRequest
import com.cropyback.cropyback.repository.ChatsRepository
import com.cropyback.cropyback.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val mongoTemplate: MongoTemplate,
    private val eventProducer: MessageEventProducer
) {

    @Autowired
    lateinit var chatRepository: ChatsRepository

    fun saveMessage(message: ChatMessage) {
        val sender = message.senderId
        val receiver = message.recipientId
        val messageIt = message.messageContent

        val query = Query().addCriteria(
            Criteria()
            .orOperator(
                Criteria.where("owner").`is`(ObjectId(sender)).and("target").`is`(ObjectId(receiver)),
                Criteria.where("owner").`is`(ObjectId(receiver)).and("target").`is`(ObjectId(sender))
            )
        )

        val existingChat = mongoTemplate.findOne(query, ChatEntity::class.java)
        if (existingChat != null) {
            val updatedChat = existingChat.messages.toMutableList().also { it.add(MessageEntity(message = messageIt)) }
            chatRepository.save(existingChat.copy(messages = updatedChat))
            eventProducer.messageSentEvent(chatId = existingChat.id.toString(), owner=sender, target = receiver)
        } else {
            val newChat = ChatEntity(
                owner = ObjectId(sender),
                target = ObjectId(receiver),
                messages = listOf(MessageEntity(message = messageIt))
            )
            chatRepository.save(newChat)
            eventProducer.messageSentEvent(chatId = newChat.id.toString(), owner=sender, target = receiver)
        }
    }

    fun getChatById(id: String): ChatEntity {
        return chatRepository.findById(ObjectId(id)).get()
    }

    fun getAllChats(): List<ChatEntity> {
        return chatRepository.findAll()
    }

}