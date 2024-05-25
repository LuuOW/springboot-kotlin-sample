package com.cropyback.cropyback.service

import com.cropyback.cropyback.api.kafka.producer.MessageEventProducer
import com.cropyback.cropyback.database.entities.chat.ChatEntity
import com.cropyback.cropyback.database.entities.chat.MessageEntity
import com.cropyback.cropyback.database.entities.chat.MessageRequest
import com.cropyback.cropyback.repository.ChatsRepository
import com.cropyback.cropyback.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val mongoTemplate: MongoTemplate,
    private val eventProducer: MessageEventProducer
) {

    @Autowired
    lateinit var chatRepository: ChatsRepository

    fun sendMessage(message: MessageRequest) {
        val sender = message.sender
        val receiver = message.receiver
        val messageIt = message.message

        val chats = getAllChats()

        chats.forEach {
            if (usersShareChat(it, receiver, sender)) {
                val updatedChat = it.messages.toMutableList().also { it.add(MessageEntity(message = messageIt)) }
                chatRepository.save(it.copy(messages = updatedChat))
                eventProducer.messageSentEvent(chatId = it.id.toString(), owner=sender, target = receiver)
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
    }

    fun getChatById(id: String): ChatEntity {
        return chatRepository.findById(ObjectId(id)).get()
    }

    fun getAllChats(): List<ChatEntity> {
        return chatRepository.findAll()
    }

}

fun usersShareChat(p3: ChatEntity, receiver: String, sender: String): Boolean {
    return p3.owner.toString() == receiver || p3.owner.toString() == sender
            && p3.target.toString() == sender || p3.target.toString() == receiver
}